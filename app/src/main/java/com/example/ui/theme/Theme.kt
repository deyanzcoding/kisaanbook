package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

private val DarkColorScheme =
  darkColorScheme(
    primary = GreenDarkPrimary,
    onPrimary = Color(0xFF00390E),
    primaryContainer = GreenPrimaryDark,
    onPrimaryContainer = Color(0xFFA5D6A7),
    secondary = GreenDarkSecondary,
    onSecondary = Color(0xFF0D3813),
    tertiary = GreenDarkTertiary,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF263829),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF4C6650)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenPrimaryLight,
    onPrimaryContainer = GreenPrimaryDark,
    secondary = GreenSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCEDC8),
    onSecondaryContainer = GreenPrimaryDark,
    tertiary = GreenTertiary,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5EF),
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFD0DCD0)
  )

@Composable
fun KisaanBookTheme(
  themeMode: ThemeMode = ThemeMode.SYSTEM,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val isDark = when (themeMode) {
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
  }

  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      isDark -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun KisaanBhaiTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = KisaanBookTheme(
  themeMode = if (darkTheme) ThemeMode.DARK else ThemeMode.LIGHT,
  dynamicColor = dynamicColor,
  content = content
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = KisaanBookTheme(
  themeMode = if (darkTheme) ThemeMode.DARK else ThemeMode.LIGHT,
  dynamicColor = dynamicColor,
  content = content
)
