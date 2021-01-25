package dev.tmapps.konnection.sample.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.outlined.SignalCellular4Bar
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import dev.tmapps.konnection.NetworkConnection
import com.tmapps.konnection.sample.R

val NetworkConnection.icon: ImageVector
    get() = when (this) {
        NetworkConnection.WIFI -> Icons.Outlined.Wifi
        NetworkConnection.MOBILE -> Icons.Outlined.SignalCellular4Bar
        NetworkConnection.NONE -> Icons.Filled.CloudOff
    }

val NetworkConnection.message: Int
    @StringRes get() = when (this) {
        NetworkConnection.WIFI -> R.string.network_connection_wifi
        NetworkConnection.MOBILE -> R.string.network_connection_mobile
        NetworkConnection.NONE -> R.string.network_connection_none
    }
