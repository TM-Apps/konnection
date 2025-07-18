package dev.tmapps.konnection

import kotlinx.coroutines.flow.Flow

expect class Konnection {
    companion object {
        /**
         * Returns the current [Konnection] instance or instantiate with a default config.
         */
        val instance: Konnection

        /**
         * Creates and returns a [Konnection] instance.
         *
         * @param enableDebugLog if true, the internal Konnection debug logs will be enabled.
         * @param ipResolvers a list of [IpResolver] to find for IP info when `getCurrentIpInfo()`.
         *
         * **NOTE**: It is strongly recommended to work with only one [Konnection] instance on the App.
         */
        fun createInstance(
            enableDebugLog: Boolean = false,
            ipResolvers: List<IpResolver> = listOf(
                MyExternalIpResolver(enableDebugLog),
                IPv6TestIpResolver(enableDebugLog)
            )
        ): Konnection
    }

    /** Indicates whether a network connection is available (true) or not (false). */
    fun isConnected(): Boolean
    /** Hot Flow that emits if there is Network Connection (true) or not (false). */
    fun observeHasConnection(): Flow<Boolean>

    /** Returns the current Network Connection. */
    fun getCurrentNetworkConnection(): NetworkConnection?
    /** Hot Flow that emits the current Network Connection. */
    fun observeNetworkConnection(): Flow<NetworkConnection?>

    /** Returns the ip info from the current Network Connection. */
    suspend fun getInfo(): ConnectionInfo?
}

enum class NetworkConnection {
    WIFI,
    MOBILE,
    ETHERNET,
    BLUETOOTH_TETHERING,
    UNKNOWN_CONNECTION_TYPE
}

/** The connection info */
data class ConnectionInfo(
    val connection: NetworkConnection,
    val ipv4: String? = null,
    val ipv6: String? = null,
    val externalIp: String? = null
)

/** IP resolver contract */
interface IpResolver {
    suspend fun get(): String?
}
