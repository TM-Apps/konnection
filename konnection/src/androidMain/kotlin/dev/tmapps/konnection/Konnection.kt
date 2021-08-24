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

actual class Konnection(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectionPublisher = MutableStateFlow(NetworkConnection.NONE)

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
        connectionPublisher.map { it != NetworkConnection.NONE }

    actual fun observeNetworkConnection(): Flow<NetworkConnection> = connectionPublisher

    actual fun getCurrentNetworkConnection(): NetworkConnection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMNetworkConnection(connectivityManager)
        } else {
            preAndroidMNetworkConnection(connectivityManager)
        }

    @TargetApi(Build.VERSION_CODES.M)
    private fun postAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean =
        postAndroidMNetworkConnection(connectivityManager) != NetworkConnection.NONE

    @TargetApi(Build.VERSION_CODES.M)
    private fun postAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)
        return when {
            connection == null -> NetworkConnection.NONE
            connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI
            else -> NetworkConnection.MOBILE
        }
    }

    @Suppress("DEPRECATION")
    private fun preAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean =
        preAndroidMNetworkConnection(connectivityManager) != NetworkConnection.NONE

    @Suppress("DEPRECATION")
    private fun preAndroidMNetworkConnection(connectivityManager: ConnectivityManager): NetworkConnection =
        when (connectivityManager.activeNetworkInfo?.type) {
            null -> NetworkConnection.NONE
            ConnectivityManager.TYPE_WIFI -> NetworkConnection.WIFI
            else -> NetworkConnection.MOBILE
        }

    private fun publishNetworkState(network: Network) {
        val connection = connectivityManager.getNetworkCapabilities(network)
        connectionPublisher.value = when {
            connection == null || !isConnected() -> NetworkConnection.NONE
            connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI
            else -> NetworkConnection.MOBILE
        }
    }
}
