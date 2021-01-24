package com.tmapps.konnection.sample.ui.theme

import androidx.compose.material.Typography
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
    h1 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    h2 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    h3 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    h4 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp,
        color = Color.White
    ),
    h5 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    h6 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.W500,
        lineHeight = 28.sp,
        color = Color.White
    ),
    subtitle1 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.W500,
        lineHeight = 22.sp,
        color = Color.White
    ),
    subtitle2 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.W500,
        color = Color.White
    ),
    body1 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp,
        color = Color.White
    ),
    body2 = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        color = Color.White
    ),
    button = TextStyle(
     // fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    caption = TextStyle(
     // fontFamily = fonts,
        color = Color.White
    ),
    overline = TextStyle(
        letterSpacing = 0.08.em,
        color = Color.White
    )
)

private fun typographyFromDefaults(
    h1: TextStyle?,
    h2: TextStyle?,
    h3: TextStyle?,
    h4: TextStyle?,
    h5: TextStyle?,
    h6: TextStyle?,
    subtitle1: TextStyle?,
    subtitle2: TextStyle?,
    body1: TextStyle?,
    body2: TextStyle?,
    button: TextStyle?,
    caption: TextStyle?,
    overline: TextStyle?
): Typography {
    val defaults = Typography()
    return Typography(
        h1 = defaults.h1.merge(h1),
        h2 = defaults.h2.merge(h2),
        h3 = defaults.h3.merge(h3),
        h4 = defaults.h4.merge(h4),
        h5 = defaults.h5.merge(h5),
        h6 = defaults.h6.merge(h6),
        subtitle1 = defaults.subtitle1.merge(subtitle1),
        subtitle2 = defaults.subtitle2.merge(subtitle2),
        body1 = defaults.body1.merge(body1),
        body2 = defaults.body2.merge(body2),
        button = defaults.button.merge(button),
        caption = defaults.caption.merge(caption),
        overline = defaults.overline.merge(overline)
    )
}
