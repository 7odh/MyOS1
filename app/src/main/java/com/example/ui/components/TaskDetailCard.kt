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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Priority
import com.example.model.Task
import com.example.model.TaskSchedule
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TaskVioletBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun TaskDetailCard(
  task: Task,
  onToggle: () -> Unit,
  onEdit: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  val checkBgColor by animateColorAsState(
    targetValue = if (task.isCompleted) HabitEmerald else Color.Transparent,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBg"
  )

  val checkBorderColor by animateColorAsState(
    targetValue = if (task.isCompleted) HabitEmerald else BorderLight,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBorder"
  )

  val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f || MaterialTheme.colorScheme.background.red < 0.2f
  val cardContainer = if (task.isCompleted) {
    if (isDark) Color(0xFF132A22) else Color(0xFFF9FDFB)
  } else {
    MaterialTheme.colorScheme.surface
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(
        width = 1.dp,
        color = if (task.isCompleted) HabitEmerald.copy(alpha = if (isDark) 0.5f else 0.3f) else MaterialTheme.colorScheme.outlineVariant,
        shape = RoundedCornerShape(16.dp)
      ),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = cardContainer
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      // Main row: Checkbox, Title & Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        // Checkbox button (48dp min touch target)
        Box(
          modifier = Modifier
            .size(36.dp)
            .clickable(onClick = onToggle),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(checkBgColor)
              .border(1.5.dp, checkBorderColor, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            if (task.isCompleted) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "مكتملة",
                tint = TextWhite,
                modifier = Modifier.size(15.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Title and notes
        Column(
          modifier = Modifier
            .weight(1f)
            .clickable(onClick = onToggle)
        ) {
          Text(
            text = task.title,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Bold,
              fontSize = 14.sp,
              textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
            ),
            color = if (task.isCompleted) TextSecondary.copy(alpha = 0.7f) else TextPrimary
          )

          if (!task.notes.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = task.notes,
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
              ),
              color = TextSecondary,
              maxLines = 2
            )
          }
        }

        // Actions: Edit and Delete
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(if (isDark) Color(0xFF1B2740) else Color(0xFFEFF6FF))
              .clickable(onClick = onEdit),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.Edit,
              contentDescription = "تعديل",
              tint = BrightBlue,
              modifier = Modifier.size(14.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(if (isDark) Color(0xFF3B1A1A) else Color(0xFFFEE2E2))
              .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.DeleteOutline,
              contentDescription = "حذف",
              tint = Color(0xFFEF4444),
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Badges Row: Schedule badge and Priority badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Schedule Badge
        val (scheduleText, scheduleBg, scheduleColor) = when (task.schedule) {
          TaskSchedule.TODAY -> Triple("اليوم ⚡", TaskVioletBg, TaskViolet)
          TaskSchedule.TOMORROW -> Triple("غداً 📅", if (isDark) Color(0xFF152642) else Color(0xFFEFF6FF), BrightBlue)
          TaskSchedule.FUTURE -> Triple("قريباً ⏳", if (isDark) Color(0xFF11291E) else Color(0xFFF0FDF4), HabitEmerald)
          TaskSchedule.NO_DATE -> Triple("بدون موعد 📭", if (isDark) Color(0xFF1E2638) else Color(0xFFF1F5F9), TextSecondary)
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(scheduleBg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = scheduleText,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            ),
            color = scheduleColor
          )
        }

        // Priority Badge
        val (pText, pBg, pColor) = when (task.priority) {
          Priority.HIGH -> Triple("أولوية عالية", if (isDark) Color(0xFF3B1A1A) else Color(0xFFFEF2F2), PriorityHigh)
          Priority.MEDIUM -> Triple("أولوية متوسطة", if (isDark) Color(0xFF382A13) else Color(0xFFFFFBEB), Color(0xFFF59E0B))
          Priority.LOW -> Triple("أولوية منخفضة", if (isDark) Color(0xFF1E2838) else Color(0xFFF1F5F9), PriorityLow)
          Priority.NONE -> Triple("بدون أولوية", if (isDark) Color(0xFF182030) else Color(0xFFF8FAFC), TextSecondary)
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(pBg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = pText,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            ),
            color = pColor
          )
        }

        // Formatted Date Badge (if specified)
        if (!task.dueDateFormatted.isNullOrBlank() && task.schedule != TaskSchedule.NO_DATE) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (isDark) Color(0xFF1E2638) else Color(0xFFF8FAFC))
              .border(0.5.dp, if (isDark) Color(0xFF2E394E) else BorderLight, RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = "🗓️ ${task.dueDateFormatted}",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
              ),
              color = TextSecondary
            )
          }
        }
      }
    }
  }
}
