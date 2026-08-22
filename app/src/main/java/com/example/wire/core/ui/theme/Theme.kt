package com.example.wire.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.wire.core.ui.theme.SuccessGreen

private val DarkColorScheme = darkColorScheme(
    primary = BrandCyan,        // Cyan highlight for Dark Mode
    secondary = BrandGold,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = BrandNavy,
    onBackground = TextWhite,
    onSurface = TextWhite,
    outlineVariant = Color(0xFF2C2C2E)



)

private val LightColorScheme = lightColorScheme(
    primary = BrandNavy,        // Navy Buttons/Bubbles for Light Mode
    secondary = BrandGold,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = TextWhite,
    onBackground = TextBlack,
    onSurface = TextBlack,
    outlineVariant = DividerLight,
    scrim = SuccessGreen
)

@Composable
fun WireTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme)
        DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WireTypography, // Ensure your Type.kt uses 'Inter' or 'Lexend' for Fintech
        content = content
    )
}