package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ResolveXLightColorScheme = lightColorScheme(
    primary = ResolveXPrimary,
    onPrimary = Color.White,
    primaryContainer = ResolveXPrimaryContainer,
    onPrimaryContainer = ResolveXOnPrimaryContainer,
    secondary = ResolveXSecondary,
    onSecondary = Color.White,
    secondaryContainer = ResolveXSecondaryContainer,
    onSecondaryContainer = ResolveXOnSecondaryContainer,
    tertiary = ResolveXTertiary,
    onTertiary = Color.White,
    tertiaryContainer = ResolveXTertiaryContainer,
    background = ResolveXBackground,
    onBackground = ResolveXTextPrimary,
    surface = ResolveXSurface,
    onSurface = ResolveXTextPrimary,
    surfaceVariant = ResolveXSurfaceVariant,
    onSurfaceVariant = ResolveXTextSecondary,
    outline = ResolveXOutline,
    outlineVariant = ResolveXOutlineVariant
)

private val ResolveXDarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF2DD4BF),
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = Color(0xFF115E59),
    onSecondaryContainer = Color(0xFFCCFBF1),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep clean brand colors for demo consistency
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ResolveXDarkColorScheme else ResolveXLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
