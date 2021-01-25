package dev.tmapps.konnection

import kotlinx.coroutines.flow.Flow

enum class NetworkConnection {
    WIFI, MOBILE, NONE
}

expect class Konnection {
    fun isConnected(): Boolean
    fun observeConnection(): Flow<NetworkConnection>
    /** stops the emission of the connection state. */
    fun stopConnectionPublishing()
}
