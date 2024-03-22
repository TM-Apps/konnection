import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.tmapps.konnection.sample.App

fun main() = application {
    Window(
        title = "Konnection",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
