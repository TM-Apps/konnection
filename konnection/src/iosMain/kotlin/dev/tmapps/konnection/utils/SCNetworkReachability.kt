package dev.tmapps.konnection.utils

import dev.tmapps.konnection.NetworkConnection
import dev.tmapps.konnection.NetworkMonitor
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.NativePointed
import kotlinx.cinterop.alloc
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alignOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.free
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.SystemConfiguration.SCNetworkReachabilityCallBack
import platform.SystemConfiguration.SCNetworkReachabilityContext
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithAddress
import platform.SystemConfiguration.SCNetworkReachabilityFlags
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.SCNetworkReachabilityRef
import platform.SystemConfiguration.SCNetworkReachabilitySetCallback
import platform.SystemConfiguration.SCNetworkReachabilitySetDispatchQueue
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionAutomatic
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionOnDemand
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionOnTraffic
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsInterventionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsDirect
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsLocalAddress
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsWWAN
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsTransientConnection
import platform.darwin.NSObjectProtocol
import platform.darwin.dispatch_queue_attr_make_with_qos_class
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t
import platform.posix.AF_INET
import platform.posix.QOS_CLASS_DEFAULT
import platform.posix.sockaddr_in

@OptIn(ExperimentalForeignApi::class)
internal class SCNetworkReachabilityNetworkMonitor(
    private val logger: (String, Throwable?) -> Unit
) : NetworkMonitor {

    private val zeroAddress: NativePointed
    private val reachabilityRef: SCNetworkReachabilityRef
    private val reachabilitySerialQueue: dispatch_queue_t
    private val context: SCNetworkReachabilityContext

    private val notificationObserver: NSObjectProtocol
    private val selfPtr: StableRef<SCNetworkReachabilityNetworkMonitor>

    private val reachabilityBroadcaster = MutableStateFlow(TriggerEvent)

    private fun debugLog(message: String, error: Throwable? = null) = ::logger

    init {
        val sizeSockaddr = sizeOf<sockaddr_in>()
        val alignSockaddr = alignOf<sockaddr_in>()
        zeroAddress = nativeHeap.alloc(sizeSockaddr, alignSockaddr).reinterpret<sockaddr_in>()
        zeroAddress.sin_len = sizeOf<sockaddr_in>().toUByte()
        zeroAddress.sin_family = AF_INET.convert()

        reachabilityRef = SCNetworkReachabilityCreateWithAddress(null, zeroAddress.ptr.reinterpret())
            ?: throw IllegalStateException("Failed on SCNetworkReachabilityCreateWithAddress")

        val dispatchQueueAttr = dispatch_queue_attr_make_with_qos_class(null, QOS_CLASS_DEFAULT, 0)

        reachabilitySerialQueue = dispatch_queue_create("com.tmapps.konnection", dispatchQueueAttr)

        notificationObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = "ReachabilityChangedNotification",
            `object` = null,
            queue = NSOperationQueue.mainQueue,
            usingBlock = { reachabilityBroadcaster.value = TriggerEvent }
        )

        selfPtr = StableRef.create(this)

        val sizeSCNetReachCxt = sizeOf<SCNetworkReachabilityContext>()
        val alignSCNetReachCxt = alignOf<SCNetworkReachabilityContext>()
        context = nativeHeap.alloc(sizeSCNetReachCxt, alignSCNetReachCxt).reinterpret<SCNetworkReachabilityContext>()
        context.version = 0
        context.info = selfPtr.asCPointer()
        context.retain = null
        context.release = null
        context.copyDescription = null
   
        val callback: SCNetworkReachabilityCallBack = staticCFunction { _: SCNetworkReachabilityRef?, _: SCNetworkReachabilityFlags, info: COpaquePointer? ->
            // this block runs on "network_helper" thread, created few lines above
            if (info == null) return@staticCFunction

            // debugLog("SCNetworkReachabilityCallBack fired!")
            // reachabilityBroadcaster.value = TriggerEvent -> not working: EXC_BAD_ACCESS!

            try {
                NSNotificationCenter.defaultCenter.postNotificationName("ReachabilityChangedNotification", null)
            } catch (error: Throwable) {
                // can't send Reachability update, probably the instance of Konnection is dead!
                // or some unexpected error occurs ¯\_(ツ)_/¯
                // debugLog("SCNetworkReachabilityCallBack error!", error)
            }
        }

        if (!SCNetworkReachabilitySetCallback(reachabilityRef, callback, context.ptr)) {
            throw IllegalStateException("Failed on SCNetworkReachabilitySetCallback")
        }
        if (!SCNetworkReachabilitySetDispatchQueue(reachabilityRef, reachabilitySerialQueue)) {
            throw IllegalStateException("Failed on SCNetworkReachabilitySetDispatchQueue")
        }
    }

    override fun clear() {
        NSNotificationCenter.defaultCenter.removeObserver(notificationObserver)
        SCNetworkReachabilitySetCallback(reachabilityRef, null, null)
        SCNetworkReachabilitySetDispatchQueue(reachabilityRef, null)
        selfPtr.dispose()
        nativeHeap.free(context)
        nativeHeap.free(zeroAddress)
        debugLog("Konnection stopped!")
    }

    override fun isConnected(): Boolean {
        val flags = getReachabilityFlags()
        val isReachable = flags.contains(kSCNetworkReachabilityFlagsReachable)
        val needsConnection = flags.contains(kSCNetworkReachabilityFlagsConnectionRequired)
        return isReachable && !needsConnection
    }

    override fun getCurrentNetworkConnection(): NetworkConnection? {
        val flags = getReachabilityFlags()
        val isReachable = flags.contains(kSCNetworkReachabilityFlagsReachable)
        val needsConnection = flags.contains(kSCNetworkReachabilityFlagsConnectionRequired)
        val isMobileConnection = flags.contains(kSCNetworkReachabilityFlagsIsWWAN)

        return when {
            !isReachable || needsConnection -> null
            !isMobileConnection -> NetworkConnection.WIFI
            isMobileConnection -> NetworkConnection.MOBILE
            else -> NetworkConnection.UNKNOWN_CONNECTION_TYPE
        }
    }

    override fun observeNetworkConnection(): Flow<NetworkConnection?> =
        reachabilityBroadcaster.map { getCurrentNetworkConnection() }

    @OptIn(ExperimentalForeignApi::class)
    private fun getReachabilityFlags(): Array<SCNetworkReachabilityFlags> = memScoped {
        // val flags = allocArray<SCNetworkReachabilityFlagsVar>(10) -> not working
        // val flags = allocArrayOf<SCNetworkReachabilityFlagsVar>() -> not working
        val flags = alloc<SCNetworkReachabilityFlagsVar>()

        if (SCNetworkReachabilityGetFlags(reachabilityRef, flags.ptr)) return emptyArray()

        val result = arrayOf(
            kSCNetworkReachabilityFlagsTransientConnection,
            kSCNetworkReachabilityFlagsReachable,
            kSCNetworkReachabilityFlagsConnectionRequired,
            kSCNetworkReachabilityFlagsConnectionOnTraffic,
            kSCNetworkReachabilityFlagsInterventionRequired,
            kSCNetworkReachabilityFlagsConnectionOnDemand,
            kSCNetworkReachabilityFlagsIsLocalAddress,
            kSCNetworkReachabilityFlagsIsDirect,
            kSCNetworkReachabilityFlagsIsWWAN,
            kSCNetworkReachabilityFlagsConnectionAutomatic
        ).filter {
            (flags.value and it) > 0u
        }
        .toTypedArray()
        debugLog("SCNetworkReachabilityFlags: ${result.contentDeepToString()}")
        return result
    }

    override suspend fun getCurrentNetworkInterfaceNames(): Set<String>? =
        when (getCurrentNetworkConnection()) {
            NetworkConnection.WIFI -> setOf("en0")
            NetworkConnection.MOBILE -> setOf("pdp_ip0", "pdp_ip1", "pdp_ip2", "pdp_ip3")
            NetworkConnection.ETHERNET -> setOf("en1", "en2", "en3", "en4")
            else -> null
        }
}
