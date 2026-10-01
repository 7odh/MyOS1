package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FreeBreakfast
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.RestLavenderActive
import com.example.ui.theme.RestLavenderBg
import com.example.ui.theme.RestLavenderBgDark
import com.example.ui.theme.RestLavenderText
import com.example.ui.theme.RestLavenderTextDark
import com.example.ui.theme.TextWhite

@Composable
fun MyOSHeader(
  isRestModeActive: Boolean,
  onMenuClick: () -> Unit,
  onRestModeToggle: () -> Unit,
  onSearchClick: () -> Unit = {},
  language: AppLanguage = AppLanguage.ARABIC,
  modifier: Modifier = Modifier
) {
  val isDark = MaterialTheme.colorScheme.background.red < 0.2f

  val targetRestBg = if (isRestModeActive) {
    RestLavenderActive
  } else {
    if (isDark) RestLavenderBgDark else RestLavenderBg
  }

  val targetRestText = if (isRestModeActive) {
    TextWhite
  } else {
    if (isDark) RestLavenderTextDark else RestLavenderText
  }

  val restBgColor by animateColorAsState(
    targetValue = targetRestBg,
    animationSpec = tween(durationMillis = 300),
    label = "restBgColor"
  )

  val restTextColor by animateColorAsState(
    targetValue = targetRestText,
    animationSpec = tween(durationMillis = 300),
    label = "restTextColor"
  )

  // Status bar safe container
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Menu / Hamburger button on the side
      IconButton(
        onClick = onMenuClick,
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
      ) {
        Icon(
          imageVector = Icons.Outlined.Menu,
          contentDescription = if (language == AppLanguage.ARABIC) "القائمة الجانبية" else "Side Menu",
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(26.dp)
        )
      }

      // Center Logo and Brand Title
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        MyOSLogoIcon(size = 32.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "MyOS",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              letterSpacing = 0.5.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = AppStrings.appSubtitle(language),
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Action buttons: Search and "راحة" Button (Settings gear icon removed as requested)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Global Search Lens Button
        IconButton(
          onClick = onSearchClick,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
          Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = AppStrings.search(language),
            tint = BrightBlue,
            modifier = Modifier.size(20.dp)
          )
        }

        // "راحة" / "Rest" Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(restBgColor)
            .clickable(
              role = Role.Button,
              onClick = onRestModeToggle
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.FreeBreakfast,
              contentDescription = AppStrings.restMode(language),
              tint = restTextColor,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = AppStrings.restMode(language),
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              ),
              color = restTextColor
            )
          }
        }
      }
    }
  }
}
