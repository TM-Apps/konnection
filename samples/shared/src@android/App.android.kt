package dev.tmapps.konnection.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.tmapps.konnection.Konnection

@Composable
internal actual fun getKonnection(enableDebugLog: Boolean): Konnection {
    val context = LocalContext.current
    return Konnection(context, enableDebugLog)
}
