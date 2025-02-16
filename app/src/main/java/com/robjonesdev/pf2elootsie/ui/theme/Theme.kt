package com.robjonesdev.pf2elootsie.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

val Green500 = Color(0xFF4CAF50) // Muted green
val Green700 = Color(0xFF388E3C) // Darker green
val Orange200 = Color(0xFFFFB74D) // Warm orange accent

val LightBackground = Color(0xFFF5F5F5) // Light gray
val DarkBackground = Color(0xFF121212)  // Dark gray

val LightSurface = Color(0xFFFFFFFF) // White
val DarkSurface = Color(0xFF1E1E1E)  // Dark surface

val ErrorColor = Color(0xFFE57373) // Calmer error red

private val DarkColorPalette = darkColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Orange200,
    background = DarkBackground,
    surface = DarkSurface,
    error = ErrorColor,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Orange200,
    background = LightBackground,
    surface = LightSurface,
    error = ErrorColor,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun LootsieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}