package dev.tmapps.konnection.sample

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.outlined.NetworkCell
import androidx.compose.material.icons.outlined.SignalCellular4Bar
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import dev.tmapps.konnection.IpInfo
import dev.tmapps.konnection.NetworkConnection

val NetworkConnection?.icon: ImageVector
    get() = when (this) {
        NetworkConnection.WIFI -> Icons.Outlined.Wifi
        NetworkConnection.MOBILE -> Icons.Outlined.SignalCellular4Bar
        NetworkConnection.ETHERNET -> Icons.Outlined.NetworkCell
        null -> Icons.Filled.CloudOff
    }

val NetworkConnection?.message: String
    get() = when (this) {
        NetworkConnection.WIFI -> "Connected by Wifi"
        NetworkConnection.MOBILE -> "Connected by Mobile Network"
        NetworkConnection.ETHERNET -> "Connected by Ethernet"
        null -> "No Connection"
    }

val IpInfo.ipInfo1: String?
    get() = when (this) {
        is IpInfo.WifiIpInfo -> this.ipv4?.let { "IPv4: $it" }
        is IpInfo.MobileIpInfo -> this.hostIpv4?.let { "Host IP: $it" }
        else -> null
    }

val IpInfo.ipInfo2: String?
    get() = when (this) {
        is IpInfo.WifiIpInfo -> this.ipv6?.let { "Host IP: $it" }
        is IpInfo.MobileIpInfo -> this.externalIpV4?.let { "External IP: $it" }
        else -> null
    }
