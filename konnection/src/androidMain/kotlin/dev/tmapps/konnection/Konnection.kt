package dev.tmapps.konnection

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface
import java.net.URL

actual class Konnection(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectionPublisher = MutableStateFlow<NetworkConnection?>(value = null)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            publishNetworkState(network)
        }
        override fun onLost(network: Network) {
            publishNetworkState(network)
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    actual fun isConnected(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMInternetCheck(connectivityManager)
        } else {
            preAndroidMInternetCheck(connectivityManager)
        }

    actual fun observeHasConnection(): Flow<Boolean> = connectionPublisher.map { it != null }

    actual fun getCurrentNetworkConnection(): NetworkConnection? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMNetworkConnection(connectivityManager)
        } else {
            preAndroidMNetworkConnection(connectivityManager)
        }

    actual fun observeNetworkConnection(): Flow<NetworkConnection?> = connectionPublisher

    actual suspend fun getCurrentIpInfo(): IpInfo? = getIpInfo(getCurrentNetworkConnection())

    @TargetApi(Build.VERSION_CODES.M)
    private fun postAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean =
        postAndroidMNetworkConnection(connectivityManager) != null

    @TargetApi(Build.VERSION_CODES.M)
    private fun postAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection? {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)
        return when {
            connection == null -> null
            connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI
            else -> NetworkConnection.MOBILE
        }
    }

    private fun preAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean =
        preAndroidMNetworkConnection(connectivityManager) != null

    @Suppress("DEPRECATION")
    private fun preAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection? =
        when (connectivityManager.activeNetworkInfo?.type) {
            null -> null
            ConnectivityManager.TYPE_WIFI -> NetworkConnection.WIFI
            else -> NetworkConnection.MOBILE
        }

    private fun publishNetworkState(network: Network) {
        val connection = connectivityManager.getNetworkCapabilities(network)
        connectionPublisher.value = when {
            connection == null || !isConnected() -> null
            connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI
            else -> NetworkConnection.MOBILE
        }
    }

    private suspend fun getIpInfo(networkConnection: NetworkConnection?): IpInfo? {
        if (networkConnection == null) return null
        try {
            var ipv4: String? = null
            var ipv6: String? = null

            val networks = NetworkInterface.getNetworkInterfaces()

            while (networks.hasMoreElements()) {
                val enumIpAddr = networks.nextElement().inetAddresses

                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                 // Log.d("Konnection", "getIpAddress inetAddress = $inetAddress")
                    if (!inetAddress.isLoopbackAddress) {
                        if (ipv4 == null && inetAddress is Inet4Address) ipv4 = inetAddress.hostAddress?.toString()
                        if (ipv6 == null && inetAddress is Inet6Address) ipv6 = inetAddress.hostAddress?.toString()
                    }
                }
            }

            return when (networkConnection) {
                NetworkConnection.WIFI -> IpInfo.WifiIpInfo(ipv4 = ipv4, ipv6 = ipv6)
                NetworkConnection.MOBILE ->
                    IpInfo.MobileIpInfo(hostIpv4 = ipv4, externalIpV4 = getExternalIp())
            }
        } catch (ex: Exception) {
         // Log.e("Konnection", "getIpInfo networkConnection = $networkConnection", ex)
            return null
        }
    }

    private suspend fun getExternalIp(): String? = withContext(Dispatchers.IO) {
        websitePublicApiUrls.firstNotNullOfOrNull {
            try {
                URL(it).readText()
            } catch (ex: Exception) {
             // Log.e("Konnection", "getExternalIp", ex)
                null
            }
        }
    }
}
