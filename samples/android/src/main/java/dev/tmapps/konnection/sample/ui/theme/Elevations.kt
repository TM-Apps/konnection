package dev.tmapps.konnection.sample.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Elevation values that can be themed.
 */
@Immutable
data class Elevations(val card: Dp = 0.dp)

internal val AmbientElevations = staticAmbientOf { Elevations() }
