package dev.tmapps.konnection.resolvers

import dev.tmapps.konnection.IpResolver
import dev.tmapps.konnection.getUrlContent
import dev.tmapps.konnection.logError

class IPv6TestIpResolver(
    private val enableDebugLog: Boolean = false
): IpResolver {
    companion object {
        private const val TAG = "IPv6TestIpResolver"
        private const val serviceUrl = "https://v4v6.ipv6-test.com/api/myip.php"
    }

    override suspend fun get(): String? =
        try {
            getUrlContent(serviceUrl)
        } catch (error: Exception) {
            if (enableDebugLog) {
                logError(TAG, "error on get()", error)
            }
            null
        }
}
