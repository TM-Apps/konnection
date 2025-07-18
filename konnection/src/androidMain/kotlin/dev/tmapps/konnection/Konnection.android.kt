package dev.tmapps.konnection

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface
import kotlin.concurrent.Volatile

actual class Konnection private constructor(
    private val context: Context,
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
        ): Konnection = createInstance( // require(INSTANCE == null) { "Single Instance already created!" }
            context = KonnectionConfig.getInstance().context,
            enableDebugLog = enableDebugLog,
            ipResolvers = ipResolvers
        )

        fun createInstance(
            context: Context,
            enableDebugLog: Boolean = false,
            ipResolvers: List<IpResolver> = listOf(
                MyExternalIpResolver(enableDebugLog),
                IPv6TestIpResolver(enableDebugLog)
            )
        ): Konnection = Konnection(context, enableDebugLog, ipResolvers).also {
            INSTANCE = it
        }
    }

    @VisibleForTesting
    internal var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectionPublisher = MutableStateFlow(getCurrentNetworkConnection())

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            debugLog("NetworkCallback -> onAvailable: network=($network)")
            // need this only for Android API < 23
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                connectionPublisher.value = getNetworkConnection(network)
            }
        }
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            val connection = getNetworkConnection(networkCapabilities)
            debugLog("NetworkCallback -> onCapabilitiesChanged: connection=($connection)")
            connectionPublisher.value = connection
        }
        override fun onLost(network: Network) {
            debugLog("NetworkCallback -> onLost: network=($network)")
            connectionPublisher.value = null
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    actual fun isConnected(): Boolean = getCurrentNetworkConnection() != null

    actual fun observeHasConnection(): Flow<Boolean> = connectionPublisher.map { it != null }

    actual fun getCurrentNetworkConnection(): NetworkConnection? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMNetworkConnection(connectivityManager)
        } else {
            preAndroidMNetworkConnection(connectivityManager)
        }

    actual fun observeNetworkConnection(): Flow<NetworkConnection?> = connectionPublisher

    actual suspend fun getInfo(): ConnectionInfo? = withContext(Dispatchers.IO) {
        val networkConnection = getCurrentNetworkConnection() ?: return@withContext null
        try {
            debugLog("getIpInfo networkConnection = $networkConnection")

            var ipv4: String? = null
            var ipv6: String? = null

            for (networkInterface in NetworkInterface.getNetworkInterfaces()) {
                debugLog("getIpInfo networkInterface = $networkInterface")
                if (
                    networkInterface.isUp &&
                    ((networkConnection == NetworkConnection.WIFI && networkInterface.name.startsWith("wlan")) ||
                     (networkConnection == NetworkConnection.MOBILE && networkInterface.name.startsWith("rmnet")) ||
                     (networkConnection == NetworkConnection.ETHERNET && networkInterface.name.startsWith("eth")) ||
                     (networkConnection == NetworkConnection.BLUETOOTH_TETHERING && networkInterface.name.startsWith("bt")) ||
                     (networkConnection == NetworkConnection.UNKNOWN_CONNECTION_TYPE && networkInterface.name.startsWith("ap")) ||
                     (networkConnection == NetworkConnection.UNKNOWN_CONNECTION_TYPE && networkInterface.name.startsWith("p2p")) ||
                     (networkConnection == NetworkConnection.UNKNOWN_CONNECTION_TYPE && networkInterface.name.startsWith("tun")) ||
                     (networkConnection == NetworkConnection.UNKNOWN_CONNECTION_TYPE && networkInterface.name.startsWith("ppp")) ||
                     (networkConnection == NetworkConnection.UNKNOWN_CONNECTION_TYPE && networkInterface.name.startsWith("wigig")) ||
                     (networkConnection == NetworkConnection.UNKNOWN_CONNECTION_TYPE && networkInterface.name.startsWith("fxa")))
                ) {
                    debugLog("getIpAddress networkConnectionInterface = $networkInterface")
                    val inetAddresses = networkInterface.inetAddresses

                    while (inetAddresses.hasMoreElements()) {
                        val inetAddress = inetAddresses.nextElement()
                        debugLog("getIpAddress inetAddress = $inetAddress")
                        if (!inetAddress.isLoopbackAddress) {
                            if (ipv4 == null && inetAddress is Inet4Address) ipv4 = inetAddress.hostAddress?.toString()
                            if (ipv6 == null && inetAddress is Inet6Address) ipv6 = inetAddress.hostAddress?.toString()
                        }
                    }
                }
            }

            return@withContext ConnectionInfo(
                connection = networkConnection,
                ipv4 = ipv4,
                ipv6 = ipv6,
                externalIp = getExternalIp()
            )
        } catch (error: Exception) {
            ensureActive()
            debugLog("getIpInfo networkConnection = $networkConnection", error)
            return@withContext null
        }
    }

    private fun getNetworkConnection(network: Network): NetworkConnection? {
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return getNetworkConnection(capabilities)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun postAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection? {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return getNetworkConnection(capabilities)
    }

    @Suppress("DEPRECATION")
    private fun preAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection? =
        when (connectivityManager.activeNetworkInfo?.type) {
            null -> null
            ConnectivityManager.TYPE_WIFI -> NetworkConnection.WIFI
            ConnectivityManager.TYPE_BLUETOOTH -> NetworkConnection.BLUETOOTH_TETHERING
            ConnectivityManager.TYPE_MOBILE -> NetworkConnection.MOBILE
            ConnectivityManager.TYPE_ETHERNET -> NetworkConnection.ETHERNET
            else -> NetworkConnection.UNKNOWN_CONNECTION_TYPE
        }

    private fun getNetworkConnection(capabilities: NetworkCapabilities?): NetworkConnection? =
        when {
            capabilities == null -> null
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                && !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> null
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) -> null
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> NetworkConnection.BLUETOOTH_TETHERING
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnection.MOBILE
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkConnection.ETHERNET
            else -> NetworkConnection.UNKNOWN_CONNECTION_TYPE
        }

    private suspend fun getExternalIp(): String? = withContext(Dispatchers.IO) {
        ipResolvers.firstNotNullOfOrNull { it.get() }
    }

    private fun debugLog(message: String, error: Throwable? = null) {
        if (enableDebugLog) {
            Log.d("Konnection", message, error)
        }
    }
}
