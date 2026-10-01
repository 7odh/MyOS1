package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Habit
import com.example.model.HabitType
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg

@Composable
fun HabitItemRow(
  habit: Habit,
  onIncrementCounter: () -> Unit,
  onOpenLogDialog: () -> Unit,
  onToggleBoolean: () -> Unit,
  language: AppLanguage = AppLanguage.ARABIC,
  modifier: Modifier = Modifier
) {
  val isCompleted = habit.isCompleted
  val isExceeded = habit.currentValue > habit.targetValue
  val isDark = MaterialTheme.colorScheme.background.red < 0.2f

  val rowBorderColor by animateColorAsState(
    targetValue = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant,
    animationSpec = tween(durationMillis = 200),
    label = "rowBorder"
  )

  val cardContainerColor = if (isCompleted) {
    if (isDark) Color(0xFF132A22) else Color(0xFFF9FDFB)
  } else {
    MaterialTheme.colorScheme.surface
  }

  val iconBadgeBg = if (isCompleted) {
    if (isDark) HabitEmerald.copy(alpha = 0.2f) else HabitEmeraldBg
  } else {
    MaterialTheme.colorScheme.surfaceVariant
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .border(1.dp, rowBorderColor, RoundedCornerShape(14.dp))
      .clickable {
        when (habit.type) {
          HabitType.COUNTER -> onIncrementCounter()
          HabitType.QUANTITY, HabitType.DURATION -> onOpenLogDialog()
          HabitType.BOOLEAN -> onToggleBoolean()
        }
      },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = cardContainerColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 1. Habit Icon in soft rounded badge
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(iconBadgeBg),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = habit.iconEmoji,
          fontSize = 18.sp
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // 2. Middle content: Title & Progress Bar / Text
      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = habit.title,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = if (isCompleted) FontWeight.SemiBold else FontWeight.Medium,
              fontSize = 14.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )

          // Current vs Target text badge
          val progressLabel = when (habit.type) {
            HabitType.BOOLEAN -> if (isCompleted) {
              if (language == AppLanguage.ARABIC) "مكتملة ✓" else "Done ✓"
            } else {
              if (language == AppLanguage.ARABIC) "لم تكتمل" else "Pending"
            }
            else -> "${habit.currentValue} / ${habit.targetValue} ${habit.unit}"
          }

          Text(
            text = progressLabel,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            ),
            color = when {
              isExceeded || isCompleted -> HabitEmerald
              else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
          )
        }

        // Linear Progress bar for non-boolean habits
        if (habit.type != HabitType.BOOLEAN) {
          Spacer(modifier = Modifier.height(6.dp))
          LinearProgressIndicator(
            progress = {
              if (habit.targetValue <= 0) 0f
              else (habit.currentValue.toFloat() / habit.targetValue.toFloat()).coerceIn(0f, 1f)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp)),
            color = if (isCompleted) HabitEmerald else BrightBlue,
            trackColor = if (isCompleted) {
              if (isDark) HabitEmerald.copy(alpha = 0.2f) else Color(0xFFD1FAE5)
            } else {
              MaterialTheme.colorScheme.surfaceVariant
            },
            strokeCap = StrokeCap.Round
          )
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

      // 3. Action button (Counter button, Log button, or Checkbox)
      when (habit.type) {
        HabitType.COUNTER -> {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (isCompleted) {
                  if (isDark) HabitEmerald.copy(alpha = 0.25f) else HabitEmeraldBg
                } else {
                  MaterialTheme.colorScheme.primaryContainer
                }
              )
              .border(
                width = 1.dp,
                color = if (isCompleted) HabitEmerald.copy(alpha = 0.5f) else BrightBlue.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
              )
              .clickable(onClick = onIncrementCounter)
              .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "+1",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              ),
              color = if (isCompleted) HabitEmerald else BrightBlue
            )
          }
        }
        HabitType.QUANTITY, HabitType.DURATION -> {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
              )
              .clickable(onClick = onOpenLogDialog)
              .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = if (language == AppLanguage.ARABIC) "تسجيل" else "Log",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
        HabitType.BOOLEAN -> {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(
                if (isCompleted) HabitEmerald else Color.Transparent
              )
              .border(
                width = 1.5.dp,
                color = if (isCompleted) HabitEmerald else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
              )
              .clickable(onClick = onToggleBoolean),
            contentAlignment = Alignment.Center
          ) {
            if (isCompleted) {
              Text(
                text = "✓",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                ),
                color = Color.White
              )
            }
          }
        }
      }
    }
  }
}
