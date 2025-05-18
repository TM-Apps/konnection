package dev.tmapps.konnection.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "Konnection",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
