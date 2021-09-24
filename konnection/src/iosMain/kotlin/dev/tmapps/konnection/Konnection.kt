package dev.tmapps.konnection

import dev.tmapps.konnection.utils.TriggerEvent
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.NativePointed
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alignOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.free
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSURL
import platform.Foundation.NSString
import platform.Foundation.stringWithContentsOfURL
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
import platform.darwin.dispatch_queue_t
import platform.darwin.dispatch_queue_attr_make_with_qos_class
import platform.darwin.dispatch_queue_create
import platform.darwin.getifaddrs
import platform.darwin.ifaddrs
import platform.posix.AF_INET
import platform.posix.AF_INET6
import platform.posix.QOS_CLASS_DEFAULT
import platform.posix.NI_MAXHOST
import platform.posix.NI_NUMERICHOST
import platform.posix.getnameinfo
import platform.posix.sockaddr
import platform.posix.sockaddr_in

actual class Konnection {

    private val zeroAddress: NativePointed
    private val reachabilityRef: SCNetworkReachabilityRef
    private val reachabilitySerialQueue: dispatch_queue_t
    private val context: SCNetworkReachabilityContext

    private val notificationObserver: NSObjectProtocol
    private val selfPtr: StableRef<Konnection>

    private val reachabilityBroadcaster = MutableStateFlow(TriggerEvent)

    init {
        val sizeSockaddr = sizeOf<sockaddr_in>()
        val alignSockaddr = alignOf<sockaddr_in>()
        zeroAddress = nativeHeap.alloc(sizeSockaddr, alignSockaddr).reinterpret<sockaddr_in>()
        zeroAddress.sin_len = sizeOf<sockaddr_in>().toUByte()
        zeroAddress.sin_family = AF_INET.convert()

        reachabilityRef = SCNetworkReachabilityCreateWithAddress(null, zeroAddress.ptr.reinterpret<sockaddr>())
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
            if (info == null) { return@staticCFunction }

            // println("SCNetworkReachabilityCallBack fired!")
            // reachabilityBroadcaster.value = TriggerEvent -> not working: EXC_BAD_ACCESS!

            try {
                NSNotificationCenter.defaultCenter.postNotificationName("ReachabilityChangedNotification", null)
            } catch (e: Throwable) {
                // can't send Reachability update, probably the instance of Konnection is dead!
                // or some unexpected error occurs ¯\_(ツ)_/¯
                println("SCNetworkReachabilityCallBack error!")
            }
        }

        if (!SCNetworkReachabilitySetCallback(reachabilityRef, callback, context.ptr)) {
            throw IllegalStateException("Failed on SCNetworkReachabilitySetCallback")
        }
        if (!SCNetworkReachabilitySetDispatchQueue(reachabilityRef, reachabilitySerialQueue)) {
            throw IllegalStateException("Failed on SCNetworkReachabilitySetDispatchQueue")
        }
    }

    actual fun isConnected(): Boolean {
        val flags = getReachabilityFlags()
        val isReachable = flags.contains(kSCNetworkReachabilityFlagsReachable)
        val needsConnection = flags.contains(kSCNetworkReachabilityFlagsConnectionRequired)
        return isReachable && !needsConnection
    }

    actual fun observeHasConnection(): Flow<Boolean> =
        observeNetworkConnection().map { it != null }

    actual fun getCurrentNetworkConnection(): NetworkConnection? {
        val flags = getReachabilityFlags()
        val isReachable = flags.contains(kSCNetworkReachabilityFlagsReachable)
        val needsConnection = flags.contains(kSCNetworkReachabilityFlagsConnectionRequired)
        val isMobileConnection = flags.contains(kSCNetworkReachabilityFlagsIsWWAN)

        return when {
            !isReachable || needsConnection -> null
            isMobileConnection -> NetworkConnection.MOBILE
            else -> NetworkConnection.WIFI
        }
    }

    actual fun observeNetworkConnection(): Flow<NetworkConnection?> =
        reachabilityBroadcaster.map { getCurrentNetworkConnection() }

    actual suspend fun getCurrentIpInfo(): IpInfo? {
        val networkConnection = getCurrentNetworkConnection() ?: return null

        val networkInterface = networkConnection.networkInterface
        val ipv4 = getIfAddrs(networkInterface, AF_INET)
        val ipv6 = getIfAddrs(networkInterface, AF_INET6)

        return when (networkConnection) {
            NetworkConnection.WIFI -> IpInfo.WifiIpInfo(ipv4 = ipv4, ipv6 = ipv6)
            NetworkConnection.MOBILE ->
                IpInfo.MobileIpInfo(hostIpv4 = ipv4, externalIpV4 = getExternalIp())
        }
    }

    fun stop() {
        NSNotificationCenter.defaultCenter.removeObserver(notificationObserver)
        SCNetworkReachabilitySetCallback(reachabilityRef, null, null)
        SCNetworkReachabilitySetDispatchQueue(reachabilityRef, null)
        selfPtr.dispose()
        nativeHeap.free(context)
        nativeHeap.free(zeroAddress)
    }

    private fun getReachabilityFlags(): Array<SCNetworkReachabilityFlags> = memScoped {
     // val flags = allocArray<SCNetworkReachabilityFlagsVar>(10) -> not working
     // val flags = allocArrayOf<SCNetworkReachabilityFlagsVar>() -> not working
        val flags = alloc<SCNetworkReachabilityFlagsVar>()

        if (!SCNetworkReachabilityGetFlags(reachabilityRef, flags.ptr)) return emptyArray()

        val result = arrayOf<SCNetworkReachabilityFlags>(
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
     // println("SCNetworkReachabilityFlags: ${result.contentDeepToString()}")
        return result
    }

    private fun getIfAddrs(netInterface: String, saFamily: Int): String? = memScoped {
        val ifaddr = alloc<ifaddrs>()
        if (getifaddrs(ifaddr.ptr.reinterpret()) == 0) {
            var addr = ifaddr.ifa_next?.reinterpret<ifaddrs>()

            while (addr != null) {
                val socketAddr = addr.pointed.ifa_addr?.reinterpret<sockaddr>()
                val currentSaFamily = socketAddr?.pointed?.sa_family

                if (currentSaFamily?.toInt() == saFamily) {
                    val ifaName = addr.pointed.ifa_name?.reinterpret<ByteVar>()?.toKString() ?: return@memScoped null
                 // println("socketName = $socketName")
                    if (ifaName == netInterface) {
                        val saLen = socketAddr.pointed.sa_len
                        val hostname = allocArray<ByteVar>(length = NI_MAXHOST)
                        getnameinfo(socketAddr, saLen.toUInt(), hostname, NI_MAXHOST, null, 0, NI_NUMERICHOST)
                     // println("hostname = ${hostname.pointed.value} | saLen.toUInt() = ${saLen.toUInt()}")
                     // println("ipValue = ${hostname.toKString()}")
                        return@memScoped hostname.toKString()
                    }
                }

                addr = addr.pointed.ifa_next?.reinterpret<ifaddrs>()
            }
        }
        return null
    }

    private val NetworkConnection.networkInterface: String
        get() = when (this) {
            NetworkConnection.WIFI -> "en0"
            NetworkConnection.MOBILE -> "pdp_ip0"
        }

    private suspend fun getExternalIp(): String? =
        websitePublicApiUrls.firstNotNullOfOrNull {
            try {
                val url = NSURL(string = it)
                NSString.stringWithContentsOfURL(url)?.toString()
            } catch (ex: Exception) {
             // println("getExternalIp = $ex")
                null
            }
        }
}
