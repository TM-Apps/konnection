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
import dev.tmapps.konnection.resolvers.IPv6TestIpResolver
import dev.tmapps.konnection.resolvers.MyExternalIpResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface

actual class Konnection(
    context: Context,
    private val enableDebugLog: Boolean = false,
    private val ipResolvers: List<IpResolver> = listOf(
        MyExternalIpResolver(enableDebugLog),
        IPv6TestIpResolver(enableDebugLog)
    )
) {
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

    actual suspend fun getCurrentIpInfo(): IpInfo? = getIpInfo(getCurrentNetworkConnection())

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
            else -> NetworkConnection.MOBILE
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
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnection.MOBILE
            else -> null
        }

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
        if (enableDebugLog) {
            Log.d("Konnection", message, error)
        }
    }
}
