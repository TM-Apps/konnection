package dev.tmapps.konnection

import dev.tmapps.konnection.utils.IfaddrsInteractor
import dev.tmapps.konnection.utils.IfaddrsInteractorImpl
import dev.tmapps.konnection.utils.NWPathMonitorNetworkMonitor
import dev.tmapps.konnection.utils.SCNetworkReachabilityNetworkMonitor
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import platform.Foundation.NSLog
import platform.Foundation.NSProcessInfo
import kotlin.concurrent.Volatile

@OptIn(ExperimentalForeignApi::class)
actual class Konnection private constructor(
    private val enableDebugLog: Boolean,
    private val ipResolvers: List<IpResolver>
) {
    actual companion object {
        @Volatile
        private var INSTANCE: Konnection? = null

        actual val instance: Konnection
            get() = INSTANCE ?: createInstance()

        actual fun createInstance(
            enableDebugLog: Boolean,
            ipResolvers: List<IpResolver>
        ): Konnection = Konnection(enableDebugLog, ipResolvers).also {
            INSTANCE = it // require(INSTANCE == null) { "Single Instance already created!" }
        }
    }

    private fun isVersionOrLater(major: Int, minor: Int = 0, patch: Int = 0): Boolean =
        NSProcessInfo.processInfo.operatingSystemVersion.useContents {
            when {
                majorVersion.toInt() > major -> true
                majorVersion.toInt() == major && minorVersion.toInt() > minor -> true
                majorVersion.toInt() == major && minorVersion.toInt() == minor && patchVersion.toInt() >= patch -> true
                else -> false
            }
        }

    // need to be an `internal var` to allow unit tests
    internal var networkMonitor: NetworkMonitor = if (isVersionOrLater(12)) {
        NWPathMonitorNetworkMonitor(::debugLog)
    } else {
        SCNetworkReachabilityNetworkMonitor(::debugLog)
    }
    internal var ifaddrsInteractor: IfaddrsInteractor = IfaddrsInteractorImpl(::debugLog)
    // ------------------------------------------------

    actual fun isConnected(): Boolean = networkMonitor.isConnected()

    actual fun observeHasConnection(): Flow<Boolean> = observeNetworkConnection().map { it != null }

    actual fun getCurrentNetworkConnection(): NetworkConnection? = networkMonitor.getCurrentNetworkConnection()

    actual fun observeNetworkConnection(): Flow<NetworkConnection?> = networkMonitor.observeNetworkConnection()

    actual suspend fun getInfo(): ConnectionInfo? {
        val networkConnection = getCurrentNetworkConnection() ?: return null
        val networkInterfaces = networkConnection.networkInterfaces ?: return null

        val ifAddresses = ifaddrsInteractor.get(networkInterfaces)

        return ConnectionInfo(
            connection = networkConnection,
            ipv4 = ifAddresses.afInet,
            ipv6 = ifAddresses.afInet6,
            externalIp = getExternalIp()
        )
    }

    fun stop() = networkMonitor.clear()

    private val NetworkConnection.networkInterfaces: Set<String>?
        get() = when (this) {
            NetworkConnection.WIFI -> setOf("en0")
            NetworkConnection.MOBILE -> setOf("pdp_ip0", "pdp_ip1", "pdp_ip2", "pdp_ip3")
            NetworkConnection.ETHERNET -> setOf("en2", "en3", "en4") //en1
            NetworkConnection.BLUETOOTH_TETHERING,
            NetworkConnection.UNKNOWN_CONNECTION_TYPE -> null
        }

    private suspend fun getExternalIp(): String? =
        ipResolvers.firstNotNullOfOrNull { it.get() }

    private fun debugLog(message: String, error: Throwable? = null) {
        if (enableDebugLog) {
            val logMessage = message + if (error != null) "error=($error)" else ""
            NSLog("Konnection -> $logMessage")
        }
    }
}

internal interface NetworkMonitor {
    fun clear()
    fun isConnected(): Boolean
    fun getCurrentNetworkConnection(): NetworkConnection?
    fun observeNetworkConnection(): Flow<NetworkConnection?>
}
