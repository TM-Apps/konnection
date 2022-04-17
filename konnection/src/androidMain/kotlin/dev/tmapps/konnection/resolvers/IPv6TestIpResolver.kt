package dev.tmapps.konnection.resolvers

import android.util.Log
import dev.tmapps.konnection.ExternalIpResolver
import java.net.URL

class IPv6TestIpResolver(
    private val enableDebugLog: Boolean = false
): ExternalIpResolver {
    companion object {
        private const val TAG = "Konnection"
        private const val serviceUrl = "https://v4v6.ipv6-test.com/api/myip.php"
    }

    override suspend fun get(): String? =
        try {
            @Suppress("BlockingMethodInNonBlockingContext")
            URL(serviceUrl).readText()
        } catch (error: Exception) {
            if (enableDebugLog) {
                Log.e(TAG, "IPv6TestIpResolver -> error on get()", error)
            }
            null
        }
}
