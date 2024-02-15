package dev.tmapps.konnection

import android.util.Log
import java.net.URL

actual fun getUrlContent(url: String): String? = URL(url).readText()

actual fun logError(tag: String, message: String, error: Throwable) {
    Log.e(tag, message, error)
}
