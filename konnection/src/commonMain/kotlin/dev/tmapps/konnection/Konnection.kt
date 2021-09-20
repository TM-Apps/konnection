package dev.tmapps.konnection

import kotlinx.coroutines.flow.Flow

enum class NetworkConnection {
    WIFI, MOBILE
}

expect class Konnection {
    /** Returns true if has some Network Connection otherwise false. */
    fun isConnected(): Boolean
    /** Hot Flow that emits if has Network Connection or not. */
    fun observeHasConnection(): Flow<Boolean>

    /** Returns the current Network Connection. */
    fun getCurrentNetworkConnection(): NetworkConnection?
    /** Hot Flow that emits the current Network Connection. */
    fun observeNetworkConnection(): Flow<NetworkConnection?>

    /** Returns the IPv4 from the current Network Connection. */
    fun getCurrentIpv4(): String?
    /** Hot Flow that emits the IPv4 from the current Network Connection. */
    fun observeIpv4(): Flow<String?>

    /** Returns the IPv6 from the current Network Connection. */
    fun getCurrentIpv6(): String?
    /** Hot Flow that emits the IPv6 from the current Network Connection. */
    fun observeIpv6(): Flow<String?>
}
