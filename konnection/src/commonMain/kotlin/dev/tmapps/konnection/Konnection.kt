package dev.tmapps.konnection

import kotlinx.coroutines.flow.Flow

enum class NetworkConnection {
    WIFI, MOBILE
}

sealed class IpInfo(val networkConnection: NetworkConnection) {
    data class WifiIpInfo(
        val ipv4: String?,
        val ipv6: String?
    ): IpInfo(networkConnection = NetworkConnection.WIFI)

    data class MobileIpInfo(
        val hostIpv4: String?,
        val externalIpV4: String?
    ): IpInfo(networkConnection = NetworkConnection.MOBILE)
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

    /** Returns the ip info from the current Network Connection. */
    suspend fun getCurrentIpInfo(): IpInfo?
}

internal val websitePublicApiUrls: List<String>
    get() = listOf(
        "https://myexternalip.com/raw",
        "https://v4v6.ipv6-test.com/api/myip.php"
        //"http://whatismyip.akamai.com/"
    )
