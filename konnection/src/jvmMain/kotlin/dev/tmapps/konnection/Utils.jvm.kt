package dev.tmapps.konnection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.logging.Logger

internal actual suspend fun getUrlContent(url: String): String? = withContext(Dispatchers.IO) {
    URL(url).readText()
}

internal actual fun logError(tag: String, message: String, error: Throwable) {
    logger.severe("$tag - $message\n$error")
}

private val logger by lazy { Logger.getLogger("Konnection") }
