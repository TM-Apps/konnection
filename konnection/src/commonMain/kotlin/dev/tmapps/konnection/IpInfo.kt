package dev.tmapps.konnection

/** The ip info data */
sealed class IpInfo(val connection: NetworkConnection) {
    data class WifiIpInfo(
        val ipv4: String?,
        val ipv6: String?
    ): IpInfo(connection = NetworkConnection.WIFI)

    data class MobileIpInfo(
        val hostIpv4: String?,
        val externalIpV4: String?
    ): IpInfo(connection = NetworkConnection.MOBILE)
}
