package com.example.wire.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define your colors (Color.kt)
val CyanPrimary = Color(0xFF00B4D8)
val BackgroundWhite = Color(0xFFFFFFFF)
val SurfaceWhite = Color(0xFFF8F9FA)

// In Theme.kt
private val LightColorScheme = lightColorScheme(
    primary = CyanPrimary,
    onPrimary = Color.White,
    background = BackgroundWhite,
    surface = SurfaceWhite,
    onBackground = Color.Black,
    onSurface = Color.Black,
    // This will handle the text fields if we use MaterialTheme.colorScheme
    surfaceVariant = Color(0xFFF1F3F4)
)

// The DarkColorScheme stays as your initial "Design" (Violet/Black)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7B2CBF), // Your Violet
    background = Color.Black,
    surface = Color(0xFF121212)
)


@Composable
fun WireTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = WireTypography,
        shapes = WireShapes,
        content = content
    )
}