package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.Priority
import com.example.model.Task
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun TaskItemRow(
  task: Task,
  onToggle: () -> Unit,
  onPostpone: (() -> Unit)? = null,
  language: AppLanguage = AppLanguage.ARABIC,
  modifier: Modifier = Modifier
) {
  val isDark = MaterialTheme.colorScheme.background.red < 0.2f

  val checkBgColor by animateColorAsState(
    targetValue = if (task.isCompleted) TaskViolet else Color.Transparent,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBg"
  )

  val checkBorderColor by animateColorAsState(
    targetValue = if (task.isCompleted) TaskViolet else MaterialTheme.colorScheme.outline,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBorder"
  )

  val priorityLabel = task.priority.getTitle(language)
  val (priorityTextColor, priorityBg) = when (task.priority) {
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

  // Swipe / Drag to postpone state (TickTick style gesture)
  val offsetX = remember { Animatable(0f) }
  val scope = rememberCoroutineScope()
  val density = LocalDensity.current
  val swipeThresholdPx = with(density) { 72.dp.toPx() }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
  ) {
    // Revealed background when swiping (TickTick Postpone Indicator)
    if (onPostpone != null && !task.isCompleted) {
      Box(
        modifier = Modifier
          .matchParentSize()
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = AppStrings.postponeToTomorrow(language),
            tint = BrightBlue,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = AppStrings.postponeToTomorrow(language),
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            ),
            color = BrightBlue
          )
        }
      }
    }

    // Main task row surface
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
        .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
        .pointerInput(task.id, task.isCompleted) {
          if (onPostpone == null || task.isCompleted) return@pointerInput
          detectHorizontalDragGestures(
            onDragEnd = {
              scope.launch {
                if (abs(offsetX.value) >= swipeThresholdPx) {
                  onPostpone()
                }
                offsetX.animateTo(0f, spring())
              }
            },
            onDragCancel = {
              scope.launch { offsetX.animateTo(0f, spring()) }
            },
            onHorizontalDrag = { _, dragAmount ->
              scope.launch {
                val newOffset = offsetX.value + dragAmount
                offsetX.snapTo(newOffset.coerceIn(-swipeThresholdPx * 1.5f, swipeThresholdPx * 1.5f))
              }
            }
          )
        }
        .clickable(onClick = onToggle)
        .padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Checkbox circle
      Box(
        modifier = Modifier
          .size(22.dp)
          .clip(CircleShape)
          .background(checkBgColor)
          .border(1.5.dp, checkBorderColor, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        if (task.isCompleted) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = TextWhite,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Task Title
      Text(
        text = task.title,
        style = MaterialTheme.typography.bodyMedium.copy(
          textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
          fontSize = 14.sp
        ),
        color = if (task.isCompleted) {
          MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        } else {
          MaterialTheme.colorScheme.onSurface
        },
        modifier = Modifier.weight(1f)
      )

      // Postponed indicator tag
      if (task.isPostponed && !task.isCompleted) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(BrightBlue.copy(alpha = 0.15f))
            .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
          Text(
            text = if (language == AppLanguage.ARABIC) "مُرحّلة ➡️" else "Postponed ➡️",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            ),
            color = BrightBlue
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
      }

      // Priority Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(priorityBg)
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = priorityLabel,
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          ),
          color = priorityTextColor
        )
      }
    }
  }
}
