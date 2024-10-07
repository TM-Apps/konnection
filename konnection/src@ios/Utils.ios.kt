package dev.tmapps.konnection

import kotlinx.cinterop.BetaInteropApi
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSLog
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLSession
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataTaskWithRequest

@OptIn(BetaInteropApi::class)
internal actual suspend fun getUrlContent(url: String): String? = suspendCancellableCoroutine { continuation ->
    val request = NSURLRequest.requestWithURL(NSURL(string = url))

    val dataTask = NSURLSession.sharedSession.dataTaskWithRequest(request) { data, _, error ->
        when {
            error != null -> continuation.resumeWithException(Exception(error.localizedDescription))
            data != null -> {
                val result = NSString.create(data, encoding = NSUTF8StringEncoding)
                @Suppress("CAST_NEVER_SUCCEEDS")
                continuation.resume(result as String)
            }
            else -> continuation.resumeWithException(Exception("Unknown error"))
        }
    }

    continuation.invokeOnCancellation {
        dataTask.cancel()
    }

    dataTask.resume()
}

internal actual fun logError(tag: String, message: String, error: Throwable) {
    NSLog("$tag -> $message\nerror=($error)")
}

internal data class IfAddresses(
    val afInet: String? = null,
    val afInet6: String? = null
)
