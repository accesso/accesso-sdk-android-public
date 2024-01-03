package com.accesso.sdk_android_experience_promoter_sample.view.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun ExperiencePromoterTheme(content: @Composable () -> Unit) {
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}