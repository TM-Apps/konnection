package com.tmapps.konnection

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

actual class Konnection(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val connectionPublisher = ConflatedBroadcastChannel<NetworkConnection>()

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

    actual fun isConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMInternetCheck(connectivityManager)
        } else {
            preAndroidMInternetCheck(connectivityManager)
        }
    }

    actual fun observeConnection(): Flow<NetworkConnection> = connectionPublisher.asFlow()

    actual fun stopConnectionPublishing() {
        // Android doesn't need to do anything here
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun postAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)

        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    @Suppress("DEPRECATION")
    private fun preAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }

    private fun publishNetworkState(network: Network) {
        val connection = connectivityManager.getNetworkCapabilities(network)
        connectionPublisher.offer(
            when {
                connection == null || !isConnected() -> NetworkConnection.NONE
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnection.WIFI
                else -> NetworkConnection.MOBILE
            }
        )
    }
}
