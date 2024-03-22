package dev.tmapps.konnection

import java.net.URL
import java.util.logging.Logger

internal actual fun getUrlContent(url: String): String? = URL(url).readText()

internal actual fun logError(tag: String, message: String, error: Throwable) {
    logger.severe("$tag - $message\n$error")
}

private val logger by lazy { Logger.getLogger("Konnection") }
