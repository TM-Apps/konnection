@file:Suppress("MagicNumber")

package dev.tmapps.konnection.sample.android.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import kotlin.math.ln

val gray200 = Color(0xffeeeeee)
val gray500 = Color(0xff9e9e9e)
val gray600 = Color(0xff757575)
val gray700 = Color(0xff616161)
val grayDarkPrimary = Color(0xff4f5b66)

/**
 * Return the fully opaque color that results from compositing [onSurface] atop [surface] with the
 * given [alpha]. Useful for situations where semi-transparent colors are undesirable.
 */
@Composable
fun ColorScheme.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}

/**
 * Elevation overlay logic copied from [Surface] — https://issuetracker.google.com/155181601
 */
@Composable
fun ColorScheme.elevatedSurface(elevation: Dp): Color {
    if (this.background.luminance() > 0.5) return surface
    val foreground = calculateForeground(elevation)
    return foreground.compositeOver(surface)
}

private fun calculateForeground(elevation: Dp): Color {
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return Color.White.copy(alpha = alpha)
}
