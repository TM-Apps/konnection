package dev.tmapps.konnection.sample.ios

import androidx.compose.ui.window.ComposeUIViewController
import dev.tmapps.konnection.sample.App
import dev.tmapps.konnection.sample.stopKonnection

fun MainViewController() = ComposeUIViewController { App(enableDebugLog = true) }

fun stop() = stopKonnection()
