package dev.tmapps.konnection.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.tmapps.konnection.sample.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(enableDebugLog = BuildConfig.DEBUG)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}