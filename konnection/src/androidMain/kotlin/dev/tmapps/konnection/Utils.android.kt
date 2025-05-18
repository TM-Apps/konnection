package dev.tmapps.konnection

import android.content.Context
import androidx.startup.Initializer
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

internal actual suspend fun getUrlContent(url: String): String? = withContext(Dispatchers.IO) {
    URL(url).readText()
}

internal actual fun logError(tag: String, message: String, error: Throwable) {
    Log.e(tag, message, error)
}

internal class KonnectionConfigInitializer : Initializer<KonnectionConfig> {
    override fun create(context: Context): KonnectionConfig =
        KonnectionConfig.newInstance(context)

    // No dependencies on other libraries.
    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

internal class KonnectionConfig(val context: Context) {
    companion object {
        @Suppress("StaticFieldLeak")
        @Volatile
        private var INSTANCE : KonnectionConfig? = null

        fun newInstance(context: Context): KonnectionConfig =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: KonnectionConfig(context).also { INSTANCE = it }
            }

        fun getInstance(): KonnectionConfig =
            INSTANCE ?: throw IllegalStateException("KonnectionConfig is not initialized")
    }
}
