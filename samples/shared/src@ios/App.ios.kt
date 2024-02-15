package dev.tmapps.konnection.sample

import androidx.compose.runtime.Composable
import dev.tmapps.konnection.Konnection

@Composable
internal actual fun getKonnection(enableDebugLog: Boolean): Konnection = Konnection(enableDebugLog)
