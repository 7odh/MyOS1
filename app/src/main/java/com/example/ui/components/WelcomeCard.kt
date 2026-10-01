package com.example.ui.components

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
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.ui.theme.BrightBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WelcomeCard(
  userName: String,
  motivationalSentence: String,
  isMotivationEnabled: Boolean,
  onEditNameClick: () -> Unit,
  onRotateQuote: (() -> Unit)? = null,
  language: AppLanguage = AppLanguage.ARABIC,
  modifier: Modifier = Modifier
) {
  val formattedDate = remember(language) {
    try {
      val locale = if (language == AppLanguage.ARABIC) Locale("ar") else Locale.ENGLISH
      val pattern = if (language == AppLanguage.ARABIC) "EEEE، d MMMM yyyy" else "EEEE, MMMM d, yyyy"
      val sdf = SimpleDateFormat(pattern, locale)
      sdf.format(Date())
    } catch (e: Exception) {
      if (language == AppLanguage.ARABIC) "الأربعاء، 30 سبتمبر 2026" else "Wednesday, September 30, 2026"
    }
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp)),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(horizontal = 20.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.Center
    ) {
      // Top Date & Day Chip
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.CalendarToday,
            contentDescription = null,
            tint = BrightBlue,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = formattedDate,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.5.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Edit Name Icon Button
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onEditNameClick),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = AppStrings.editName(language),
            tint = BrightBlue,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Greeting Row
      Text(
        text = AppStrings.welcome(userName, language),
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp
        ),
        color = MaterialTheme.colorScheme.onSurface
      )

      // Motivational Quote Row
      if (isMotivationEnabled && motivationalSentence.isNotBlank()) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            .clickable(enabled = onRotateQuote != null) { onRotateQuote?.invoke() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "“$motivationalSentence”",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontSize = 13.sp,
              lineHeight = 19.sp,
              fontWeight = FontWeight.Normal
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
          )

          if (onRotateQuote != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(BrightBlue.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Outlined.Shuffle,
                contentDescription = AppStrings.shuffleQuote(language),
                tint = BrightBlue,
                modifier = Modifier.size(15.dp)
              )
            }
          }
        }
      }
    }
  }
}
