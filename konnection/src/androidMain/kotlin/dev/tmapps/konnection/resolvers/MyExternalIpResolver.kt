package dev.tmapps.konnection.resolvers

import android.util.Log
import dev.tmapps.konnection.ExternalIpResolver
import java.net.URL

class MyExternalIpResolver(
    private val enableDebugLog: Boolean = false
): ExternalIpResolver {
    companion object {
        private const val TAG = "Konnection"
        private const val serviceUrl = "https://myexternalip.com/raw"
    }

    override suspend fun get(): String? =
        try {
            @Suppress("BlockingMethodInNonBlockingContext")
            URL(serviceUrl).readText()
        } catch (error: Exception) {
            if (enableDebugLog) {
                Log.e(TAG, "MyExternalIpResolver -> error on get()", error)
            }
            null
        }
}
