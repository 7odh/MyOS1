package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

// Primary brand colors from MyOS specification
val ElectricCyan = Color(0xFF22D3EE)
val BrightBlue = Color(0xFF3B82F6)
val DeepBlue = Color(0xFF2563EB)
val IndigoPrimary = Color(0xFF6366F1)
val ElectricViolet = Color(0xFF8B5CF6)
val SoftViolet = Color(0xFFC084FC)

// Category specific colors
val HabitEmerald = Color(0xFF10B981)
val HabitEmeraldTrack = Color(0xFFD1FAE5)

val TaskViolet = Color(0xFF8B5CF6)
val TaskVioletTrack = Color(0xFFEDE9FE)

val GoalBlue = Color(0xFF3B82F6)
val GoalBlueTrack = Color(0xFFDBEAFE)

val RestLavenderActive = Color(0xFF6366F1)

// Priority colors
val PriorityHigh = Color(0xFFEF4444)
val PriorityHighBg = Color(0xFFFEF2F2)
val PriorityMedium = Color(0xFF3B82F6)
val PriorityMediumBg = Color(0xFFEFF6FF)
val PriorityLow = Color(0xFF64748B)
val PriorityLowBg = Color(0xFFF1F5F9)

// Light static constants for Theme.kt
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceCard = Color(0xFFFFFFFF)
val LightBorder = Color(0xFFE2E8F0)
val LightBorderVeryLight = Color(0xFFF1F5F9)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF64748B)
val LightTextMuted = Color(0xFF94A3B8)

val LightGoalBlueBg = Color(0xFFEFF6FF)
val LightHabitEmeraldBg = Color(0xFFECFDF5)
val LightTaskVioletBg = Color(0xFFF5F3FF)
val LightRestLavenderBg = Color(0xFFF3F0FF)
val LightRestLavenderText = Color(0xFF6366F1)

// Analytics Semantic Colors
val AnalyticsProgressEmerald = Color(0xFF10B981)
val AnalyticsProgressEmeraldBg = Color(0xFFECFDF5)
val AnalyticsShortfallRed = Color(0xFFEF4444)
val AnalyticsShortfallRedBg = Color(0xFFFEF2F2)
val AnalyticsRestAmber = Color(0xFFF59E0B)
val AnalyticsRestAmberBg = Color(0xFFFFFBEB)
val AnalyticsPostponeSky = Color(0xFF0284C7)
val AnalyticsPostponeSkyBg = Color(0xFFE0F2FE)
val AnalyticsFocusViolet = Color(0xFF8B5CF6)
val AnalyticsFocusVioletBg = Color(0xFFF5F3FF)

val TextWhite = Color(0xFFFFFFFF)

// Dark Theme Colors - Refined, modern dark slate palette (TickTick/Linear inspired)
val BackgroundDark = Color(0xFF0F141E)
val SurfaceDark = Color(0xFF181F2E)
val SurfaceCardDark = Color(0xFF1E2638)
val SurfaceCardElevatedDark = Color(0xFF253046)
val BorderDark = Color(0xFF2E394E)
val BorderVeryLightDark = Color(0xFF222B3D)
val TextPrimaryDark = Color(0xFFF1F5F9)
val TextSecondaryDark = Color(0xFF94A3B8)
val TextMutedDark = Color(0xFF64748B)

val GoalBlueBgDark = Color(0xFF192A4A)
val HabitEmeraldBgDark = Color(0xFF122E25)
val TaskVioletBgDark = Color(0xFF271C3F)
val RestLavenderBgDark = Color(0xFF241D38)
val RestLavenderTextDark = Color(0xFFA78BFA)

// Dynamic theme-aware color properties for automatic dark mode support:
val BackgroundLight: Color
  @Composable get() = MaterialTheme.colorScheme.background

val SurfaceWhite: Color
  @Composable get() = MaterialTheme.colorScheme.surface

val SurfaceCard: Color
  @Composable get() = MaterialTheme.colorScheme.surface

val BorderLight: Color
  @Composable get() = MaterialTheme.colorScheme.outline

val BorderVeryLight: Color
  @Composable get() = MaterialTheme.colorScheme.outlineVariant

val TextPrimary: Color
  @Composable get() = MaterialTheme.colorScheme.onSurface

val TextSecondary: Color
  @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

val TextMuted: Color
  @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

val GoalBlueBg: Color
  @Composable get() = if (MaterialTheme.colorScheme.background.red < 0.2f) GoalBlueBgDark else LightGoalBlueBg

val HabitEmeraldBg: Color
  @Composable get() = if (MaterialTheme.colorScheme.background.red < 0.2f) HabitEmeraldBgDark else LightHabitEmeraldBg

val TaskVioletBg: Color
  @Composable get() = if (MaterialTheme.colorScheme.background.red < 0.2f) TaskVioletBgDark else LightTaskVioletBg

val RestLavenderBg: Color
  @Composable get() = if (MaterialTheme.colorScheme.background.red < 0.2f) RestLavenderBgDark else LightRestLavenderBg

val RestLavenderText: Color
  @Composable get() = if (MaterialTheme.colorScheme.background.red < 0.2f) RestLavenderTextDark else LightRestLavenderText

val isAppInDarkTheme: Boolean
  @Composable get() = MaterialTheme.colorScheme.background.red < 0.2f || MaterialTheme.colorScheme.surface.luminance() < 0.5f

// Theme-aware analytics backgrounds
val AnalyticsProgressEmeraldBgTheme: Color
  @Composable get() = if (isAppInDarkTheme) Color(0xFF132D22) else AnalyticsProgressEmeraldBg

val AnalyticsShortfallRedBgTheme: Color
  @Composable get() = if (isAppInDarkTheme) Color(0xFF3B1A1A) else AnalyticsShortfallRedBg

val AnalyticsRestAmberBgTheme: Color
  @Composable get() = if (isAppInDarkTheme) Color(0xFF382A13) else AnalyticsRestAmberBg

val AnalyticsPostponeSkyBgTheme: Color
  @Composable get() = if (isAppInDarkTheme) Color(0xFF14243B) else AnalyticsPostponeSkyBg

val AnalyticsFocusVioletBgTheme: Color
  @Composable get() = if (isAppInDarkTheme) Color(0xFF271C3F) else AnalyticsFocusVioletBg

// Note card colors for Light vs Dark theme
fun getNoteCardBackgroundColor(colorLong: Long, isDark: Boolean): Color {
  if (!isDark) return Color(colorLong)
  return when (colorLong) {
    0xFFFFFBEB -> Color(0xFF282210) // Warm Yellow -> Deep Warm Amber
    0xFFEFF6FF -> Color(0xFF132338) // Sky Blue -> Deep Navy Blue
    0xFFECFDF5 -> Color(0xFF11291E) // Mint/Emerald -> Deep Emerald Pine
    0xFFFAF5FF -> Color(0xFF231836) // Soft Violet -> Deep Violet Night
    0xFFFFF1F2 -> Color(0xFF2E1522) // Soft Rose -> Deep Rose Wine
    0xFFFFF7ED -> Color(0xFF2B1B12) // Peach -> Deep Warm Bronze
    0xFFF8FAFC -> Color(0xFF182030) // Light Slate -> Dark Slate Card
    else -> Color(0xFF1E2638)
  }
}

fun getNoteCardBorderColor(colorLong: Long, isDark: Boolean): Color {
  if (!isDark) return Color(0xFFE2E8F0).copy(alpha = 0.8f)
  return when (colorLong) {
    0xFFFFFBEB -> Color(0xFFF59E0B).copy(alpha = 0.4f)
    0xFFEFF6FF -> Color(0xFF3B82F6).copy(alpha = 0.4f)
    0xFFECFDF5 -> Color(0xFF10B981).copy(alpha = 0.4f)
    0xFFFAF5FF -> Color(0xFF8B5CF6).copy(alpha = 0.4f)
    0xFFFFF1F2 -> Color(0xFFF43F5E).copy(alpha = 0.4f)
    0xFFFFF7ED -> Color(0xFFF97316).copy(alpha = 0.4f)
    0xFFF8FAFC -> Color(0xFF2E394E)
    else -> Color(0xFF2E394E)
  }
}
