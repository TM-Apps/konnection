package dev.tmapps.konnection.sample.android.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/*
private val fonts = fontFamily(
    font(R.font.rubik_regular),
    font(R.font.rubik_medium, FontWeight.W500),
    font(R.font.rubik_bold, FontWeight.Bold)
)
*/
val typography = typographyFromDefaults(
    displayLarge = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    displayMedium = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    displaySmall = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    headlineLarge = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp,
        color = Color.White
    ),
    headlineMedium = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    headlineSmall = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.W500,
        lineHeight = 28.sp,
        color = Color.White
    ),
    titleLarge = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.W500,
        lineHeight = 25.sp,
        color = Color.White
    ),
    titleMedium = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.W500,
        lineHeight = 22.sp,
        color = Color.White
    ),
    titleSmall = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.W500,
        color = Color.White
    ),
    bodyLarge = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp,
        color = Color.White
    ),
    bodyMedium = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        color = Color.White
    ),
    bodySmall = TextStyle(
     // fontFamily = fonts,
        color = Color.White
    ),
    labelLarge = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    labelSmall = TextStyle(
        letterSpacing = 0.08.em,
        color = Color.White
    )
)

private fun typographyFromDefaults(
    displayLarge: TextStyle?,
    displayMedium: TextStyle?,
    displaySmall: TextStyle?,
    headlineLarge: TextStyle?,
    headlineMedium: TextStyle?,
    headlineSmall: TextStyle?,
    titleLarge: TextStyle?,
    titleMedium: TextStyle?,
    titleSmall: TextStyle?,
    bodyLarge: TextStyle?,
    bodyMedium: TextStyle?,
    bodySmall: TextStyle?,
    labelLarge: TextStyle?,
    labelSmall: TextStyle?
): Typography {
    val defaults = Typography()
    return Typography(
        displayLarge = defaults.displayLarge.merge(displayLarge),
        displayMedium = defaults.displayMedium.merge(displayMedium),
        displaySmall = defaults.displaySmall.merge(displaySmall),
        headlineLarge = defaults.headlineLarge.merge(headlineLarge),
        headlineMedium = defaults.headlineMedium.merge(headlineMedium),
        headlineSmall = defaults.headlineSmall.merge(headlineSmall),
        titleLarge = defaults.titleMedium.merge(titleLarge),
        titleMedium = defaults.titleMedium.merge(titleMedium),
        titleSmall = defaults.titleSmall.merge(titleSmall),
        bodyLarge = defaults.bodyLarge.merge(bodyLarge),
        bodyMedium = defaults.bodyMedium.merge(bodyMedium),
        bodySmall = defaults.bodySmall.merge(bodySmall),
        labelLarge = defaults.labelLarge.merge(labelLarge),
        labelSmall = defaults.labelSmall.merge(labelSmall)
    )
}
