package dev.tmapps.konnection.resolvers

import dev.tmapps.konnection.ExternalIpResolver
import platform.Foundation.NSLog
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.stringWithContentsOfURL

class IPv6TestIpResolver(
    private val enableDebugLog: Boolean = false
): ExternalIpResolver {
    companion object {
        private const val TAG = "Konnection -> IPv6TestIpResolver"
        private const val serviceUrl = "https://v4v6.ipv6-test.com/api/myip.php"
    }

    override suspend fun get(): String? =
        try {
            val url = NSURL(string = serviceUrl)
            NSString.stringWithContentsOfURL(url)?.toString()
        } catch (error: Exception) {
            if (enableDebugLog) {
                NSLog("$TAG -> error on get(). error=($error)")
            }
            null
        }
}
