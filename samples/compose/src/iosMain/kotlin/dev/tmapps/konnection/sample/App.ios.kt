package dev.tmapps.konnection.sample

import androidx.compose.ui.window.ComposeUIViewController
import dev.tmapps.konnection.Konnection

fun MainViewController() = ComposeUIViewController { App(enableDebugLog = true) }

fun stopKonnection() = Konnection.instance.stop()
