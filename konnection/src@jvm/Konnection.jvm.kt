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
import kotlin.concurrent.Volatile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

actual class Konnection private constructor(
    private val connectionCheckTime: Duration,
    private val enableDebugLog: Boolean,
    private val ipResolvers: List<IpResolver>,
    private val pingHostCheckers: List<String>,
    private val wifiNetworkInterfaces: List<String>,
    private val ethernetNetworkInterfaces: List<String>
) {
    actual companion object {
        @Volatile
        private var INSTANCE: Konnection? = null

        actual val instance: Konnection
            get() = INSTANCE ?: createInstance()

        actual fun createInstance(
            enableDebugLog: Boolean,
            ipResolvers: List<IpResolver>
        ): Konnection = createInstance( // require(INSTANCE == null) { "Single Instance already created!" }
            connectionCheckTime = 3.seconds,
            enableDebugLog = enableDebugLog,
            ipResolvers = ipResolvers,
        )

        fun createInstance(
            connectionCheckTime: Duration,
            enableDebugLog: Boolean = false,
            ipResolvers: List<IpResolver> = listOf(
                MyExternalIpResolver(enableDebugLog),
                IPv6TestIpResolver(enableDebugLog)
            ),
            pingHostCheckers: List<String> = listOf(
                "google.com", "amazon.com", "facebook.com", "apple.com"
            ),
            wifiNetworkInterfaces: List<String> = listOf("wi-fi", "wireless", "en0", "wlan0", "ap0"),
            ethernetNetworkInterfaces: List<String> = listOf("ethernet", "lan", "en1", "eth0", "eth1")
        ): Konnection = Konnection(
            connectionCheckTime = connectionCheckTime,
            enableDebugLog = enableDebugLog,
            ipResolvers = ipResolvers,
            pingHostCheckers = pingHostCheckers,
            wifiNetworkInterfaces = wifiNetworkInterfaces,
            ethernetNetworkInterfaces = ethernetNetworkInterfaces
        ).also {
            INSTANCE = it
        }
    }

    private val logger = if (enableDebugLog) Logger.getLogger("Konnection") else null

    private val connectionPublisher = MutableStateFlow(getCurrentNetworkConnection())

    init {
        Executors.newScheduledThreadPool(1)
            .scheduleAtFixedRate({
                connectionPublisher.value = getCurrentNetworkConnection()
            }, 0, connectionCheckTime.inWholeSeconds, TimeUnit.SECONDS)
    }

    actual fun isConnected(): Boolean = pingHostCheckers.firstNotNullOfOrNull {
        if (!it.isHostAvailable) null else true
    } ?: false

    actual fun observeHasConnection(): Flow<Boolean> = connectionPublisher.map { it != null }

    actual fun getCurrentNetworkConnection(): NetworkConnection? = try {
        NetworkInterface.getNetworkInterfaces().toList()
            .mapNotNull { networkInterface ->
                if (networkInterface.isLoopback || !networkInterface.isUp) null
                else {
                    debugLog("getCurrentNetworkConnection -> network interface: ${networkInterface.name} - ${networkInterface.displayName}")
                    with(networkInterface.name.lowercase()) {
                        when {
                            wifiNetworkInterfaces.any { contains(it) } -> NetworkConnection.WIFI
                            ethernetNetworkInterfaces.any { contains(it) } -> NetworkConnection.ETHERNET
                            else -> null
                        }
                    }
                }
            }.let {
                when {
                    it.isEmpty() -> null
                    it.contains(NetworkConnection.WIFI) -> NetworkConnection.WIFI
                    it.contains(NetworkConnection.ETHERNET) -> NetworkConnection.ETHERNET
                    else -> NetworkConnection.UNKNOWN_CONNECTION_TYPE
                }
            }
    } catch (error: Throwable) {
        debugLog("getCurrentNetworkConnection -> $error")
        null
    }

    actual fun observeNetworkConnection(): Flow<NetworkConnection?> = connectionPublisher

    actual suspend fun getInfo(): ConnectionInfo? = withContext(Dispatchers.IO) {
        val networkConnection = getCurrentNetworkConnection() ?: return@withContext null
        try {
            val networkInterface = getNetworkInterface(networkConnection) ?: return@withContext null

            var ipv4: String? = null
            var ipv6: String? = null

            for (inetAddress in networkInterface.inetAddresses) {
                debugLog("getIpAddress inetAddress = $inetAddress")
                if (!inetAddress.isLoopbackAddress && networkInterface.isUp) {
                    if (ipv4 == null && inetAddress is Inet4Address) ipv4 = inetAddress.hostAddress?.toString()
                    if (ipv6 == null && inetAddress is Inet6Address) ipv6 = inetAddress.hostAddress?.toString()
                }
            }

            return@withContext ConnectionInfo(
                connection = networkConnection,
                ipv4 = ipv4,
                ipv6 = ipv6,
                externalIp = getExternalIp()
            )
        } catch (ex: Exception) {
            debugLog("getIpInfo networkConnection = $networkConnection", ex)
            return@withContext null
        }
    }

    private fun getNetworkInterface(networkConnection: NetworkConnection): NetworkInterface? = try {
        NetworkInterface.getNetworkInterfaces().toList()
            .firstNotNullOfOrNull { networkInterface ->
                with(networkInterface.name.lowercase()) {
                    when (networkConnection) {
                        NetworkConnection.WIFI -> {
                            if (wifiNetworkInterfaces.any { contains(it) }) networkInterface else null
                        }
                        NetworkConnection.ETHERNET -> {
                            if (ethernetNetworkInterfaces.any { contains(it) }) networkInterface else null
                        }
                        else -> null
                    }
                }
            }
    } catch (error: Throwable) {
        null
    }

    private val String.isHostAvailable: Boolean
        get() = runCatching {
            val isReachable = InetAddress.getByName(this).isReachable(3000)
            return@runCatching if (!isReachable) throw RuntimeException("not connected!") else true
            // Socket().connect(InetSocketAddress(this, 80), 3000)
        }.isSuccess

    private suspend fun getExternalIp(): String? = withContext(Dispatchers.IO) {
        ipResolvers.firstNotNullOfOrNull { it.get() }
    }

    private fun debugLog(message: String, error: Throwable? = null) {
        logger?.info("$message${error?.let { "\n$it" }.orEmpty()}")
    }
}
