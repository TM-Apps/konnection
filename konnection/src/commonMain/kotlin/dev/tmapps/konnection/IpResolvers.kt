package dev.tmapps.konnection

import kotlinx.coroutines.CancellationException

abstract class BaseIpResolverImpl(
    private val enableDebugLog: Boolean,
    private val tag: String,
    private val serviceUrl: String
): IpResolver {
    @Suppress("TooGenericExceptionCaught")
    override suspend fun get(): String? =
        try {
            getUrlContent(serviceUrl)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Throwable) {
            if (enableDebugLog) {
                logError(tag, "error on get()", error)
            }
            null
        }
}

class MyExternalIpResolver(enableDebugLog: Boolean) : BaseIpResolverImpl(
    enableDebugLog = enableDebugLog,
    tag = "MyExternalIpResolver",
    serviceUrl = "https://myexternalip.com/raw"
)

class IPv6TestIpResolver(enableDebugLog: Boolean) : BaseIpResolverImpl(
    enableDebugLog = enableDebugLog,
    tag = "IPv6TestIpResolver",
    serviceUrl = "https://v4v6.ipv6-test.com/api/myip.php"
)
