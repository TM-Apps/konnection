package dev.tmapps.konnection

internal expect suspend fun getUrlContent(url: String): String?

internal expect fun logError(tag: String, message: String, error: Throwable)
