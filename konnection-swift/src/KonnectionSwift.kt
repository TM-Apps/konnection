import dev.tmapps.konnection.Konnection
import dev.tmapps.konnection.resolvers.IPv6TestIpResolver
import dev.tmapps.konnection.resolvers.MyExternalIpResolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "Konnection")
object KonnectionSwift {
    private lateinit var konnection: Konnection

    /** Initialize a Konnection instance on your Swift code. */
    fun start() {
        require(!this::konnection.isInitialized) { "Konnection already initialized!" }
        konnection = Konnection.createInstance(
            enableDebugLog = false,
            ipResolvers = listOf(
                MyExternalIpResolver(false),
                IPv6TestIpResolver(false)
            )
        )
    }

    /** Initialize a Konnection instance on your Swift code. */
    fun start(enableDebugLog: Boolean) {
        require(!this::konnection.isInitialized) { "Konnection already initialized!" }
        konnection = Konnection.createInstance(
            enableDebugLog = enableDebugLog,
            ipResolvers = listOf(
                MyExternalIpResolver(enableDebugLog),
                IPv6TestIpResolver(enableDebugLog)
            )
        )
    }

    /** Initialize a Konnection instance on your Swift code. */
    fun start(
        enableDebugLog: Boolean,
        ipResolvers: List<IpResolver>
    ) {
        require(!this::konnection.isInitialized) { "Konnection already initialized!" }
        konnection = Konnection.createInstance(
            enableDebugLog = enableDebugLog,
            ipResolvers = ipResolvers.map { it.toIpResolver() }
        )
    }

    fun isConnected(): Boolean = requireKonnectionInitialized { isConnected() }

    fun observeHasConnection(): FlowWrapper<Boolean> = requireKonnectionInitialized {
        observeHasConnection().wrap()
    }

    fun getCurrentNetworkConnection(): NetworkConnection? = requireKonnectionInitialized {
        getCurrentNetworkConnection()?.toNetworkConnection()
    }

    fun observeNetworkConnection() : FlowWrapper<NetworkConnection?> = requireKonnectionInitialized {
        observeNetworkConnection().map { it?.toNetworkConnection() }.wrap()
    }

    suspend fun getInfo(): ConnectionInfo? = requireKonnectionInitializedSuspend {
        getInfo()?.toConnectionInfo()
    }

    /** Call this function on the `applicationWillTerminate` of your AppDelegate. */
    fun stop() {
        if (!this::konnection.isInitialized) {
            return
        }
        konnection.stop()
    }

    private fun <T> requireKonnectionInitialized(block: Konnection.() -> T): T {
        require(this::konnection.isInitialized) { "Konnection is not initialized!" }
        return block(konnection)
    }

    private suspend fun <T> requireKonnectionInitializedSuspend(block: suspend Konnection.() -> T): T {
        require(this::konnection.isInitialized) { "Konnection is not initialized!" }
        return block(konnection)
    }
}

enum class NetworkConnection {
    WIFI,
    MOBILE,
    ETHERNET,
    BLUETOOTH_TETHERING,
    UNKNOWN_CONNECTION_TYPE
}

interface IpResolver {
    suspend fun get(): String?
}

data class ConnectionInfo(
    val connection: NetworkConnection,
    val ipv4: String? = null,
    val ipv6: String? = null,
    val externalIp: String? = null
)

class FlowWrapper<out T> internal constructor(
    private val scope: CoroutineScope,
    private val flow: Flow<T & Any>
) {
    private var job: Job? = null
    private var isCancelled = false

    /**
     *  Cancels the flow
     */
    fun cancel() {
        isCancelled = true
        job?.cancel()
    }

    /**
     * Starts the flow
     * @param onEach callback called on each emission
     * @param onCompletion callback called when flow completes. It will be provided with a non
     * nullable Throwable if it completes abnormally
     */
    fun collect(
        onEach: (T & Any) -> Unit,
        onCompletion: (Throwable?) -> Unit
    ) {
        if (isCancelled) return
        job = scope.launch {
            flow.onEach(onEach)
                .onCompletion { cause: Throwable? -> onCompletion(cause) }
                .collect()
        }
    }
}

internal fun <T> Flow<T & Any>.wrap(scope: CoroutineScope = MainScope()) =
    FlowWrapper(scope, this)

private fun dev.tmapps.konnection.IpResolver.toIpResolver(): IpResolver =
    object : IpResolver {
        override suspend fun get(): String? = this@toIpResolver.get()
    }

private fun IpResolver.toIpResolver(): dev.tmapps.konnection.IpResolver =
    object : dev.tmapps.konnection.IpResolver {
        override suspend fun get(): String? = this@toIpResolver.get()
    }

private fun dev.tmapps.konnection.NetworkConnection.toNetworkConnection(): NetworkConnection = when (this) {
    dev.tmapps.konnection.NetworkConnection.WIFI -> NetworkConnection.WIFI
    dev.tmapps.konnection.NetworkConnection.MOBILE -> NetworkConnection.MOBILE
    dev.tmapps.konnection.NetworkConnection.ETHERNET -> NetworkConnection.ETHERNET
    dev.tmapps.konnection.NetworkConnection.BLUETOOTH_TETHERING -> NetworkConnection.BLUETOOTH_TETHERING
    dev.tmapps.konnection.NetworkConnection.UNKNOWN_CONNECTION_TYPE -> NetworkConnection.UNKNOWN_CONNECTION_TYPE
}

private fun dev.tmapps.konnection.ConnectionInfo.toConnectionInfo(): ConnectionInfo =
    ConnectionInfo(
        connection = this.connection.toNetworkConnection(),
        ipv4 = this.ipv4,
        ipv6 = this.ipv6,
        externalIp = this.externalIp
    )
