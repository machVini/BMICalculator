package com.mach.apps.imccalculatorapp.android.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta de cores para o tema claro
private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF1976D2),         // Azul confiável
    onPrimary = Color.White,
    primaryContainer = Color(0xFF95d0ff), // Azul claro para containers
    onPrimaryContainer = Color(0xFF004BA0),
    secondary = Color(0xFF43A047),       // Verde saúde
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC8E6C9),
    onSecondaryContainer = Color(0xFF1B5E20),
    tertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFDFDFD),
    background = Color(0XFFFDFDFD),
    onBackground = Color(0xFF212121),
    surface = Color.White,
    surfaceVariant = Color(0xFFF9FAFB),
    onSurface = Color(0xFF212121),
    onSurfaceVariant = Color(0xFF6B7280),
    error = Color(0xFFD32F2F)            // Vermelho para erros
)

// Paleta de cores para o tema escuro
private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF1976D2),         // Azul mais claro para tema escuro
    onPrimary = Color.White,
    primaryContainer = Color(0xFF043e7f),
    onPrimaryContainer = Color(0xFFE3F2FD),
    secondary = Color(0xFF81C784),       // Verde mais claro para tema escuro
    onSecondary = Color(0xFF1B5E20),
    secondaryContainer = Color(0xFF2E7D32),
    onSecondaryContainer = Color(0xFFE8F5E9),
    tertiary = Color(0xFF0F172A),
    tertiaryContainer = Color(0xFF1E293B),// Verde-azulado mais claro
    background = Color(0xFF0F172A),      // Fundo escuro padrão
    onBackground = Color(0xFFEEEEEE),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF6B7280),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEEEEEE),
    error = Color(0xFFEF5350)            // Vermelho mais claro para erros
)

@Composable
fun BMICalculatorAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    )

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(12.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}