package dev.tmapps.konnection.utils

import dev.tmapps.konnection.NetworkConnection
import dev.tmapps.konnection.NetworkMonitor
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Network.nw_interface_type_cellular
import platform.Network.nw_interface_type_other
import platform.Network.nw_interface_type_wifi
import platform.Network.nw_interface_type_wired
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_t
import platform.Network.nw_path_uses_interface_type
import platform.NetworkExtension.NWPath
import platform.darwin.dispatch_get_global_queue
import platform.posix.QOS_CLASS_BACKGROUND

internal class NWPathMonitorNetworkMonitor(
    private val logger: (String, Throwable?) -> Unit
) : NetworkMonitor {

    private val monitor = nw_path_monitor_create()

    private fun debugLog(message: String, error: Throwable? = null) = ::logger

    private val connectionBroadcaster = MutableStateFlow<NetworkConnection?>(null)

    private var path: nw_path_t = null

    init {
        // import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
        @OptIn(ExperimentalForeignApi::class)
        val queue = dispatch_get_global_queue(QOS_CLASS_BACKGROUND.convert(), 0.convert())
        nw_path_monitor_set_update_handler(monitor) { path ->
            this.path = path
            debugLog("path == $path, path.status == ${(path as? NWPath)?.status}")
            connectionBroadcaster.value = getNetworkConnectionFromPath(path)
        }
        nw_path_monitor_set_queue(monitor, queue)
        nw_path_monitor_start(monitor)
    }

    override fun clear() {
        nw_path_monitor_cancel(monitor)
        debugLog("Konnection stopped!")
    }

    override fun isConnected(): Boolean = nw_path_get_status(path) == nw_path_status_satisfied

    override fun getCurrentNetworkConnection(): NetworkConnection? = getNetworkConnectionFromPath(path)

    private fun getNetworkConnectionFromPath(path: nw_path_t): NetworkConnection? = when {
        // The network interface type used for communication over Wi-Fi networks.
        nw_path_uses_interface_type(path, nw_interface_type_wifi) ->  NetworkConnection.WIFI
        // The network interface type used for communication over cellular networks.
        nw_path_uses_interface_type(path, nw_interface_type_cellular) -> NetworkConnection.MOBILE
        // The network interface type used for communication over wired Ethernet networks.
        nw_path_uses_interface_type(path, nw_interface_type_wired) -> NetworkConnection.ETHERNET
        // The network interface type used for communication over virtual networks or networks of unknown types.
        nw_path_uses_interface_type(path, nw_interface_type_other) -> NetworkConnection.UNKNOWN_CONNECTION_TYPE
        else -> null
    }

    override fun observeNetworkConnection(): Flow<NetworkConnection?> = connectionBroadcaster
}
