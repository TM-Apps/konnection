package dev.tmapps.konnection

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface
import kotlin.reflect.KClass

actual class Konnection(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectionPublisher = MutableStateFlow<NetworkConnection?>(null)

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

    actual fun observeHasConnection(): Flow<Boolean> =
        connectionPublisher.map { it != null }

    actual fun observeNetworkConnection(): Flow<NetworkConnection?> = connectionPublisher

    actual fun getCurrentNetworkConnection(): NetworkConnection? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMNetworkConnection(connectivityManager)
        } else {
            preAndroidMNetworkConnection(connectivityManager)
        }

    actual fun getCurrentIpv4(): String? =getIpAddress(Inet4Address::class)

    actual fun observeIpv4(): Flow<String?> =
        connectionPublisher.map { getCurrentIpv4() }

    actual fun getCurrentIpv6(): String? = getIpAddress(Inet6Address::class)

    actual fun observeIpv6(): Flow<String?> =
        connectionPublisher.map { getCurrentIpv6() }

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

    private fun getIpAddress(inetType: KClass<*>): String? {
        try {
            val networks = NetworkInterface.getNetworkInterfaces()
            while (networks.hasMoreElements()) {
                val enumIpAddr = networks.nextElement().inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress::class == inetType) {
                        return inetAddress.hostAddress?.toString()
                    }
                }
            }
            return null
        } catch (ex: Exception) {
            // Log.e("Konnection", "getIpAddress inetType = $inetType", ex)
            return null
        }
    }
}
