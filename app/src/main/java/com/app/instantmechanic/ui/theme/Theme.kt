package com.app.instantmechanic.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val InstantMechanicColorScheme = lightColorScheme(
    primary = Color(0xFFFF6B0B),
    onPrimary = Color.White,

    background = Color(0xFFFFFBF3),
    onBackground = Color(0xFF1C1917),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1917),

    surfaceVariant = Color(0xFFFFF4EB),
    onSurfaceVariant = Color(0xFF746A63),

    outline = Color(0xFFD9CEC5),

    error = Color(0xFFC62828),
    onError = Color.White
)

@Composable
fun InstantMechanicTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = InstantMechanicColorScheme,
        typography = Typography,
        content = content
    )
}