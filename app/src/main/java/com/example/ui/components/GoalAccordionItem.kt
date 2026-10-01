package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Goal
import com.example.model.Priority
import com.example.model.Task
import com.example.ui.theme.GoalBlue
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow

@Composable
fun GoalAccordionItem(
  goal: Goal,
  isExpanded: Boolean,
  onToggleExpand: () -> Unit,
  onToggleGoalTask: (taskId: String) -> Unit,
  onPostponeGoalTask: ((taskId: String) -> Unit)? = null,
  language: AppLanguage = AppLanguage.ARABIC,
  modifier: Modifier = Modifier
) {
  val isDark = MaterialTheme.colorScheme.background.red < 0.2f

  val rotationAngle by animateFloatAsState(
    targetValue = if (isExpanded) 180f else 0f,
    animationSpec = tween(durationMillis = 250),
    label = "accordionChevronRotation"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // 1. Goal Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onToggleExpand)
          .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Goal Icon
        val iconOption = com.example.model.GoalIconProvider.getIcon(goal.iconId)
        val iconBgColor = if (isDark) iconOption.tintColor.copy(alpha = 0.2f) else iconOption.bgColor

        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(iconBgColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = iconOption.icon,
            contentDescription = goal.title,
            tint = iconOption.tintColor,
            modifier = Modifier.size(18.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Goal Title & Category
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = goal.title,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )
          if (!goal.category.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = goal.category,
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp
              ),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Goal Priority Badge
        val priorityLabel = goal.priority.getTitle(language)
        val (goalPriorityColor, goalPriorityBg) = when (goal.priority) {
          Priority.HIGH -> Pair(
            PriorityHigh,
            if (isDark) Color(0xFF3B1A1A) else Color(0xFFFEF2F2)
          )
          Priority.MEDIUM -> Pair(
            Color(0xFFF59E0B),
            if (isDark) Color(0xFF382A13) else Color(0xFFFFFBEB)
          )
          Priority.LOW -> Pair(
            PriorityLow,
            if (isDark) Color(0xFF1E2838) else Color(0xFFF1F5F9)
          )
          Priority.NONE -> Pair(
            MaterialTheme.colorScheme.onSurfaceVariant,
            if (isDark) Color(0xFF19202E) else Color(0xFFF8FAFC)
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(goalPriorityBg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = priorityLabel,
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            ),
            color = goalPriorityColor
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Progress percentage badge
        Text(
          text = "${goal.progressPercentage}%",
          style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          ),
          color = GoalBlue
        )

        Spacer(modifier = Modifier.width(6.dp))

        // Expand/Collapse Chevron Indicator
        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = if (isExpanded) {
            if (language == AppLanguage.ARABIC) "طي" else "Collapse"
          } else {
            if (language == AppLanguage.ARABIC) "توسيع" else "Expand"
          },
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier
            .size(20.dp)
            .rotate(rotationAngle)
        )
      }

      // 2. Expandable Body: Today's Tasks for this goal
      AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(animationSpec = tween(250)) + fadeIn(animationSpec = tween(250)),
        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 0.5.dp
          )
          Spacer(modifier = Modifier.height(8.dp))

          val todayGoalTasks = goal.todayTasks
          if (todayGoalTasks.isEmpty()) {
            Text(
              text = if (language == AppLanguage.ARABIC) "لا توجد مهام مجدولة لهذا الهدف اليوم" else "No tasks scheduled for this goal today",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(vertical = 4.dp)
            )
          } else {
            todayGoalTasks.forEach { task ->
              TaskItemRow(
                task = task,
                onToggle = { onToggleGoalTask(task.id) },
                onPostpone = if (onPostponeGoalTask != null) {
                  { onPostponeGoalTask(task.id) }
                } else null,
                language = language
              )
              Spacer(modifier = Modifier.height(4.dp))
            }
          }
        }
      }
    }
  }
}
