package dev.tmapps.konnection

import kotlinx.coroutines.flow.Flow

enum class NetworkConnection {
    WIFI, MOBILE, NONE
}

expect class Konnection {
    /** Returns true if has some Connection otherwise false. */
    fun isConnected(): Boolean
    /** Hot Flow that emits if has Connection or not. */
    fun observeHasConnection(): Flow<Boolean>

    /** Returns the current Network Connection. */
    fun getCurrentNetworkConnection(): NetworkConnection
    /** Hot Flow that emits the Network Connection. */
    fun observeNetworkConnection(): Flow<NetworkConnection>
}
