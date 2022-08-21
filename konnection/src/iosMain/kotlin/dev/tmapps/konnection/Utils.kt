package dev.tmapps.konnection

import platform.Foundation.NSLog
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.stringWithContentsOfURL

actual fun getUrlContent(url: String): String? =
    NSString.stringWithContentsOfURL(NSURL(string = url))?.toString()

actual fun logError(tag: String, message: String, error: Throwable) {
    NSLog("$tag -> $message. error=($error)")
}
