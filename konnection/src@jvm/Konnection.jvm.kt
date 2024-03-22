package dev.tmapps.konnection

import dev.tmapps.konnection.resolvers.IPv6TestIpResolver
import dev.tmapps.konnection.resolvers.MyExternalIpResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

actual class Konnection(
    private val enableDebugLog: Boolean = false,
    private val connectionCheckTime: Duration = 3.seconds,
    private val ipResolvers: List<IpResolver> = listOf(
        MyExternalIpResolver(enableDebugLog),
        IPv6TestIpResolver(enableDebugLog)
    )
) {
    private val logger = if (enableDebugLog) Logger.getLogger("Konnection") else null

    private val connectionPublisher = MutableStateFlow(getCurrentNetworkConnection())

    init {
        Executors.newScheduledThreadPool(1)
            .scheduleAtFixedRate({
                connectionPublisher.value = getCurrentNetworkConnection()
            }, 0, connectionCheckTime.inWholeSeconds, TimeUnit.SECONDS)
    }

    actual fun isConnected(): Boolean =
        "google.com".isHostAvailable
        || "amazon.com".isHostAvailable
        || "facebook.com".isHostAvailable
        || "apple.com".isHostAvailable

    actual fun observeHasConnection(): Flow<Boolean> = connectionPublisher.map { it != null }

    actual fun getCurrentNetworkConnection(): NetworkConnection? = try {
        NetworkInterface.getNetworkInterfaces().toList()
            .firstNotNullOfOrNull {
                if (it.isLoopback || !it.isUp) null
                else with(it.displayName.lowercase()) {
                    when {
                        contains("wi-fi|wireless|en0".toRegex()) -> NetworkConnection.WIFI
                        contains("ethernet|lan|en1".toRegex()) -> NetworkConnection.ETHERNET
                        else -> null
                    }
                }
            }
    } catch (error: Throwable) {
        debugLog("getCurrentNetworkConnection -> $error")
        null
    }

    actual fun observeNetworkConnection(): Flow<NetworkConnection?> = connectionPublisher

    actual suspend fun getCurrentIpInfo(): IpInfo? = getIpInfo(getCurrentNetworkConnection())

    private val String.isHostAvailable: Boolean
        get() = runCatching {
            val isReachable = InetAddress.getByName(this).isReachable(3000)
            return@runCatching if (!isReachable) throw RuntimeException("not connected!") else true 
            // Socket().connect(InetSocketAddress(this, 80), 3000)
        }.isSuccess
    
    private suspend fun getIpInfo(networkConnection: NetworkConnection?): IpInfo? = withContext(Dispatchers.IO) {
        if (networkConnection == null) return@withContext null
        try {
            var ipv4: String? = null
            var ipv6: String? = null

            val networks = NetworkInterface.getNetworkInterfaces()

            while (networks.hasMoreElements()) {
                val enumIpAddr = networks.nextElement().inetAddresses

                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    debugLog("getIpAddress inetAddress = $inetAddress")
                    if (!inetAddress.isLoopbackAddress) {
                        if (ipv4 == null && inetAddress is Inet4Address) ipv4 = inetAddress.hostAddress?.toString()
                        if (ipv6 == null && inetAddress is Inet6Address) ipv6 = inetAddress.hostAddress?.toString()
                    }
                }
            }

            return@withContext when (networkConnection) {
                NetworkConnection.WIFI -> IpInfo.WifiIpInfo(ipv4 = ipv4, ipv6 = ipv6)
                NetworkConnection.MOBILE -> IpInfo.MobileIpInfo(hostIpv4 = ipv4, externalIpV4 = getExternalIp())
                NetworkConnection.ETHERNET -> IpInfo.EthernetIpInfo(ipv4 = ipv4, ipv6 = ipv6)
            }
        } catch (ex: Exception) {
            debugLog("getIpInfo networkConnection = $networkConnection", ex)
            return@withContext null
        }
    }

    private suspend fun getExternalIp(): String? = withContext(Dispatchers.IO) {
        ipResolvers.firstNotNullOfOrNull { it.get() }
    }

    private fun debugLog(message: String, error: Throwable? = null) {
        logger?.info("$message${error?.let { "\n$it" }.orEmpty()}")
    }
}
