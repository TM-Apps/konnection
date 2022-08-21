package dev.tmapps.konnection.resolvers

import dev.tmapps.konnection.IpResolver
import dev.tmapps.konnection.getUrlContent
import dev.tmapps.konnection.logError

class MyExternalIpResolver(
    private val enableDebugLog: Boolean = false
): IpResolver {
    companion object {
        private const val TAG = "MyExternalIpResolver"
        private const val serviceUrl = "https://myexternalip.com/raw"
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
