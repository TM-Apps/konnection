package dev.tmapps.konnection.resolvers

import dev.tmapps.konnection.IpResolver
import dev.tmapps.konnection.getUrlContent
import dev.tmapps.konnection.logError

class MyExternalIpResolver(
    private val enableDebugLog: Boolean = false
): IpResolver {
    companion object {
        private const val TAG = "MyExternalIpResolver"
        private const val SERVICE_URL = "https://myexternalip.com/raw"
    }

    override suspend fun get(): String? =
        try {
            getUrlContent(SERVICE_URL)
        } catch (error: Exception) {
            if (enableDebugLog) {
                logError(TAG, "error on get()", error)
            }
            null
        }
}
