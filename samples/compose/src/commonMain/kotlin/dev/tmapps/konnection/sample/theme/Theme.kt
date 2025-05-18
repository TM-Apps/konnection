package dev.tmapps.konnection.sample.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ThemeColors = darkColorScheme(
    primary = Color.White,
    secondary = gray700,
    background = grayDarkPrimary,
    surface = grayDarkPrimary
)

private val ThemeElevation = Elevations(card = 4.dp)

@Composable
fun SampleTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalElevations provides ThemeElevation
    ) {
        MaterialTheme(
            colorScheme = ThemeColors,
            typography = typography,
         // shapes = shapes,
            content = content
        )
    }
}
