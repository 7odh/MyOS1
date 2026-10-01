package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.ScreenDestination
import com.example.ui.theme.BrightBlue

@Composable
fun NavigationDrawerContent(
  currentScreen: ScreenDestination,
  onScreenSelected: (ScreenDestination) -> Unit,
  onCloseDrawer: () -> Unit,
  language: AppLanguage = AppLanguage.ARABIC,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxHeight()
      .width(280.dp)
      .background(MaterialTheme.colorScheme.surface)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Drawer Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        MyOSLogoIcon(size = 32.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "MyOS",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = AppStrings.appSubtitle(language),
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 10.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      IconButton(
        onClick = onCloseDrawer,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = AppStrings.closeMenu(language),
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // Scrollable navigation list
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())
    ) {
      // Main destinations
      val mainItems = listOf(
        ScreenDestination.HOME,
        ScreenDestination.GOALS,
        ScreenDestination.HABITS,
        ScreenDestination.TASKS,
        ScreenDestination.CALENDAR,
        ScreenDestination.LISTS,
        ScreenDestination.NOTES,
        ScreenDestination.FOCUS,
        ScreenDestination.ANALYTICS
      )

      mainItems.forEach { destination ->
        DrawerMenuItem(
          title = AppStrings.getDestinationTitle(destination, language),
          icon = destination.icon,
          isSelected = currentScreen == destination,
          onClick = {
            onScreenSelected(destination)
            onCloseDrawer()
          }
        )
      }

      Spacer(modifier = Modifier.height(10.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
      Spacer(modifier = Modifier.height(10.dp))

      // Bottom utility destinations
      val bottomItems = listOf(
        ScreenDestination.SETTINGS,
        ScreenDestination.HELP
      )

      bottomItems.forEach { destination ->
        DrawerMenuItem(
          title = AppStrings.getDestinationTitle(destination, language),
          icon = destination.icon,
          isSelected = currentScreen == destination,
          onClick = {
            onScreenSelected(destination)
            onCloseDrawer()
          }
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Bottom decorative motivational mountain card
      BottomMountainCard(language = language)
    }
  }
}

@Composable
private fun DrawerMenuItem(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
  val contentColor = if (isSelected) BrightBlue else MaterialTheme.colorScheme.onSurface

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 2.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .clickable(onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = contentColor,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(14.dp))
    Text(
      text = title,
      style = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        fontSize = 14.sp
      ),
      color = contentColor
    )
  }
}

@Composable
private fun BottomMountainCard(
  language: AppLanguage = AppLanguage.ARABIC,
  modifier: Modifier = Modifier
) {
  val isDark = MaterialTheme.colorScheme.background.red < 0.2f
  val gradientColors = if (isDark) {
    listOf(Color(0xFF1E2638), Color(0xFF161E2E))
  } else {
    listOf(Color(0xFFF8FAFC), Color(0xFFEFF6FF))
  }

  val mountainColor1 = if (isDark) Color(0xFF293B5A).copy(alpha = 0.6f) else Color(0xFFDBEAFE).copy(alpha = 0.6f)
  val mountainColor2 = if (isDark) Color(0xFF3B5680).copy(alpha = 0.6f) else Color(0xFF93C5FD).copy(alpha = 0.5f)
  val flagpoleColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B)

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(100.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(brush = Brush.verticalGradient(colors = gradientColors))
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height

      // Soft mountain silhouette
      val path = Path().apply {
        moveTo(w * 0.1f, h)
        lineTo(w * 0.35f, h * 0.45f)
        lineTo(w * 0.65f, h)
        close()
      }
      drawPath(
        path = path,
        color = mountainColor1
      )

      val summitPath = Path().apply {
        moveTo(w * 0.25f, h)
        lineTo(w * 0.42f, h * 0.32f)
        lineTo(w * 0.6f, h)
        close()
      }
      drawPath(
        path = summitPath,
        color = mountainColor2
      )

      // Flag on summit
      drawLine(
        color = flagpoleColor,
        start = Offset(w * 0.42f, h * 0.32f),
        end = Offset(w * 0.42f, h * 0.20f),
        strokeWidth = 1.5.dp.toPx()
      )
      val flag = Path().apply {
        moveTo(w * 0.42f, h * 0.20f)
        lineTo(w * 0.48f, h * 0.24f)
        lineTo(w * 0.42f, h * 0.28f)
        close()
      }
      drawPath(path = flag, color = Color(0xFFEF4444))
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
      horizontalAlignment = Alignment.End,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = AppStrings.journeyQuote(language),
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          lineHeight = 15.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = "MyOS",
        style = MaterialTheme.typography.labelSmall.copy(
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
      )
    }
  }
}
