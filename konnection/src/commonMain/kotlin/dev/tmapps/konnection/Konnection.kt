package dev.tmapps.konnection

import kotlinx.coroutines.flow.Flow

expect class Konnection {
    /** Returns true if has some Network Connection otherwise false. */
    fun isConnected(): Boolean
    /** Hot Flow that emits if has Network Connection or not. */
    fun observeHasConnection(): Flow<Boolean>

    /** Returns the current Network Connection. */
    fun getCurrentNetworkConnection(): NetworkConnection?
    /** Hot Flow that emits the current Network Connection. */
    fun observeNetworkConnection(): Flow<NetworkConnection?>

    /** Returns the ip info from the current Network Connection. */
    suspend fun getCurrentIpInfo(): IpInfo?
}
