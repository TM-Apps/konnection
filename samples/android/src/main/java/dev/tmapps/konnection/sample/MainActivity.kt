package dev.tmapps.konnection.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.setContent
import androidx.core.view.WindowCompat
import dev.tmapps.konnection.sample.ui.Home
import dev.tmapps.konnection.sample.ui.theme.SampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SampleTheme {
                Home()
            }
        }
    }
}
