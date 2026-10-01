package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.model.ThemeMode

private val MyOSLightColorScheme = lightColorScheme(
  primary = IndigoPrimary,
  onPrimary = TextWhite,
  primaryContainer = GoalBlueBg,
  onPrimaryContainer = DeepBlue,
  secondary = BrightBlue,
  onSecondary = TextWhite,
  secondaryContainer = RestLavenderBg,
  onSecondaryContainer = RestLavenderText,
  tertiary = ElectricCyan,
  onTertiary = TextPrimary,
  background = BackgroundLight,
  onBackground = TextPrimary,
  surface = SurfaceWhite,
  onSurface = TextPrimary,
  surfaceVariant = BorderVeryLight,
  onSurfaceVariant = TextSecondary,
  outline = BorderLight
)

private val MyOSDarkColorScheme = darkColorScheme(
  primary = BrightBlue,
  onPrimary = TextWhite,
  primaryContainer = GoalBlueBgDark,
  onPrimaryContainer = ElectricCyan,
  secondary = ElectricViolet,
  onSecondary = TextWhite,
  secondaryContainer = TaskVioletBgDark,
  onSecondaryContainer = SoftViolet,
  tertiary = ElectricCyan,
  onTertiary = BackgroundDark,
  background = BackgroundDark,
  onBackground = TextPrimaryDark,
  surface = SurfaceDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = SurfaceCardDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = BorderDark
)

@Composable
fun MyApplicationTheme(
  themeMode: ThemeMode = ThemeMode.SYSTEM,
  content: @Composable () -> Unit
) {
  val isDark = when (themeMode) {
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
  }

  MaterialTheme(
    colorScheme = if (isDark) MyOSDarkColorScheme else MyOSLightColorScheme,
    typography = Typography,
    content = content
  )
}

