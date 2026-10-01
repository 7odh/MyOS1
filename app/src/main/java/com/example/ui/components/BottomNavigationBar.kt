package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.ScreenDestination
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg

data class BottomNavTab(
  val destination: ScreenDestination,
  val title: String,
  val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun MyOSBottomNavigationBar(
  currentScreen: ScreenDestination,
  onTabSelected: (ScreenDestination) -> Unit,
  destinations: List<ScreenDestination> = emptyList(),
  language: AppLanguage = AppLanguage.ARABIC,
  onMoreClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val activeDestinations = if (destinations.isNotEmpty()) {
    destinations
  } else {
    listOf(
      ScreenDestination.HOME,
      ScreenDestination.GOALS,
      ScreenDestination.HABITS,
      ScreenDestination.TASKS,
      ScreenDestination.CALENDAR
    )
  }

  val tabs = activeDestinations.map { dest ->
    BottomNavTab(
      destination = dest,
      title = AppStrings.getDestinationTitle(dest, language),
      icon = dest.icon
    )
  }

  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    tabs.forEach { tab ->
      val isSelected = currentScreen == tab.destination

      NavigationBarItem(
        selected = isSelected,
        onClick = {
          onTabSelected(tab.destination)
        },
        icon = {
          Icon(
            imageVector = tab.icon,
            contentDescription = tab.title,
            modifier = Modifier.size(24.dp)
          )
        },
        label = {
          Text(
            text = tab.title,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = BrightBlue,
          selectedTextColor = BrightBlue,
          indicatorColor = MaterialTheme.colorScheme.primaryContainer,
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }
  }
}
