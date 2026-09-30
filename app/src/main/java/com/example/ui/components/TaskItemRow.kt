package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Priority
import com.example.model.Task
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.TaskViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun TaskItemRow(
  task: Task,
  onToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  val checkBgColor by animateColorAsState(
    targetValue = if (task.isCompleted) TaskViolet else Color.Transparent,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBg"
  )

  val checkBorderColor by animateColorAsState(
    targetValue = if (task.isCompleted) TaskViolet else BorderLight,
    animationSpec = tween(durationMillis = 200),
    label = "taskCheckBorder"
  )

  val (priorityText, priorityTextColor, priorityBg) = when (task.priority) {
    Priority.HIGH -> Triple("عالية", PriorityHigh, Color(0xFFFEF2F2))
    Priority.MEDIUM -> Triple("متوسطة", Color(0xFFD97706), Color(0xFFFFFBEB))
    Priority.LOW -> Triple("منخفضة", PriorityLow, Color(0xFFF1F5F9))
    Priority.NONE -> Triple("بدون أولوية", TextSecondary, Color(0xFFF8FAFC))
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onToggle)
      .padding(horizontal = 4.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Custom Check Circle
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

    Spacer(modifier = Modifier.width(12.dp))

    // Task Title
    Text(
      text = task.title,
      style = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 14.sp,
        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
      ),
      color = if (task.isCompleted) TextSecondary.copy(alpha = 0.65f) else TextPrimary,
      modifier = Modifier.weight(1f)
    )

    Spacer(modifier = Modifier.width(8.dp))

    // Task's individual Priority Badge (Only when not None)
    if (task.priority != Priority.NONE) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(priorityBg)
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = priorityText,
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          ),
          color = priorityTextColor
        )
      }
    }
  }
}
