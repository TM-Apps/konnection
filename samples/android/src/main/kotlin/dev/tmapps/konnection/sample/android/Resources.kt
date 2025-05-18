package dev.tmapps.konnection.sample.android

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.SettingsEthernet
import androidx.compose.material.icons.outlined.SignalCellular4Bar
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import dev.tmapps.konnection.ConnectionInfo
import dev.tmapps.konnection.NetworkConnection

val NetworkConnection?.icon: ImageVector
    get() = when (this) {
        NetworkConnection.WIFI -> Icons.Outlined.Wifi
        NetworkConnection.MOBILE -> Icons.Outlined.SignalCellular4Bar
        NetworkConnection.ETHERNET -> Icons.Outlined.SettingsEthernet
        NetworkConnection.BLUETOOTH_TETHERING -> Icons.Outlined.Bluetooth
        NetworkConnection.UNKNOWN_CONNECTION_TYPE -> Icons.Outlined.Cloud
        null -> Icons.Filled.CloudOff
    }

val NetworkConnection?.message: String
    get() = when (this) {
        NetworkConnection.WIFI -> "Connected by Wifi"
        NetworkConnection.MOBILE -> "Connected by Mobile Network"
        NetworkConnection.ETHERNET -> "Connected by Ethernet"
        NetworkConnection.BLUETOOTH_TETHERING -> "Connected by Bluetooth TETHERING"
        NetworkConnection.UNKNOWN_CONNECTION_TYPE -> "Connected by Unknown Network"
        null -> "No Connection"
    }

val ConnectionInfo.ipV4Info: String?
    get() = this.ipv4?.let { "IPv4: $it" }

val ConnectionInfo.ipV6Info: String?
    get() = this.ipv6?.let { "IPv6: $it" }

val ConnectionInfo.externalIpInfo: String?
    get() = this.externalIp?.let { "External IP: $it" }
