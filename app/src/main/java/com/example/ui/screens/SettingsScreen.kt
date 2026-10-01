package com.example.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.BrightnessMedium
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.ViewDay
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.BackupFileInfo
import com.example.model.BackupFrequency
import com.example.model.ExportDataType
import com.example.model.ExportFormat
import com.example.model.ScreenDestination
import com.example.model.ThemeMode
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.GoalBlueBg
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.HabitEmeraldBg
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityHighBg
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import com.example.viewmodel.MyOSUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
  uiState: MyOSUiState,
  onNavigateBack: () -> Unit,
  onSetThemeMode: (ThemeMode) -> Unit,
  onSetLanguage: (AppLanguage) -> Unit,
  onRotateQuote: () -> Unit,
  onAddQuote: (String) -> Unit,
  onDeleteQuote: (Int) -> Unit,
  onToggleAutoRotateQuotes: (Boolean) -> Unit,
  onToggleBottomNavTab: (ScreenDestination) -> Unit,
  onMoveBottomNavTabUp: (ScreenDestination) -> Unit,
  onMoveBottomNavTabDown: (ScreenDestination) -> Unit,
  onSetBackupFrequency: (BackupFrequency) -> Unit,
  onSetUserAvatarEmoji: (String) -> Unit,
  onSetUserTitle: (String) -> Unit,
  onSaveUserName: (String) -> Unit,
  onToggleSounds: (Boolean) -> Unit,
  onToggleHaptics: (Boolean) -> Unit,
  onRefreshBackups: (Context) -> Unit,
  onCreateBackup: (Context) -> Unit,
  onOpenRestoreDialog: (BackupFileInfo) -> Unit,
  onCloseRestoreDialog: () -> Unit,
  onConfirmRestore: (Context) -> Unit,
  onExportData: (Context, Set<ExportDataType>, ExportFormat, Boolean) -> Unit,
  onShareFile: (Context, BackupFileInfo) -> Unit,
  onOpenClearDataDialog: () -> Unit,
  onCloseClearDataDialog: () -> Unit,
  onConfirmClearAllData: (Context) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val lang = uiState.appSettings.language
  val isArabic = lang == AppLanguage.ARABIC

  // Refresh backup and export files list on first launch
  LaunchedEffect(Unit) {
    onRefreshBackups(context)
  }

  BackHandler {
    onNavigateBack()
  }

  // Local state for export choices
  var selectedExportTypes by remember {
    mutableStateOf(ExportDataType.values().toSet())
  }
  var selectedExportFormat by remember {
    mutableStateOf(ExportFormat.CSV)
  }

  // Local state for new quote dialog
  var isAddQuoteDialogShowing by remember { mutableStateOf(false) }
  var newQuoteInput by remember { mutableStateOf("") }

  // Local state for edit profile dialog
  var isEditProfileDialogShowing by remember { mutableStateOf(false) }
  var editNameInput by remember { mutableStateOf(uiState.user.name) }
  var editTitleInput by remember { mutableStateOf(uiState.appSettings.userTitle) }

  Surface(
    modifier = modifier
      .fillMaxSize()
      .statusBarsPadding()
      .navigationBarsPadding(),
    color = MaterialTheme.colorScheme.background
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onNavigateBack,
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = if (isArabic) "رجوع" else "Back",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = if (isArabic) "الإعدادات والضبط" else "Settings & Controls",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = if (isArabic) "التحكم الكامل في المظهر، التصدير، النسخ، والتخصيص" else "Complete control over theme, export, backup & tabs",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // --- SECTION 1: Appearance & Language ---
        item {
          SettingsSectionCard(
            title = if (isArabic) "المظهر واللغة" else "Appearance & Language",
            icon = Icons.Outlined.BrightnessMedium,
            badge = if (isArabic) "عام" else "General"
          ) {
            // Theme Mode
            Text(
              text = if (isArabic) "وضع المظهر (Dark Mode / Light Mode):" else "Theme Mode:",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              ThemeMode.values().forEach { mode ->
                val isSelected = uiState.appSettings.themeMode == mode
                FilterChip(
                  selected = isSelected,
                  onClick = { onSetThemeMode(mode) },
                  label = {
                    Text(
                      text = if (isArabic) mode.titleArabic else mode.titleEnglish,
                      fontSize = 12.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  leadingIcon = {
                    val icon = when (mode) {
                      ThemeMode.SYSTEM -> Icons.Outlined.BrightnessMedium
                      ThemeMode.LIGHT -> Icons.Outlined.LightMode
                      ThemeMode.DARK -> Icons.Outlined.DarkMode
                    }
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BrightBlue,
                    selectedLabelColor = TextWhite,
                    selectedLeadingIconColor = TextWhite
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(14.dp))

            // Language & Direction
            Text(
              text = if (isArabic) "لغة التطبيق واتجاه الشاشة:" else "App Language & Direction:",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              AppLanguage.values().forEach { language ->
                val isSelected = uiState.appSettings.language == language
                FilterChip(
                  selected = isSelected,
                  onClick = { onSetLanguage(language) },
                  label = {
                    Text(
                      text = if (isArabic) language.titleArabic else language.titleEnglish,
                      fontSize = 13.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BrightBlue,
                    selectedLabelColor = TextWhite,
                    selectedLeadingIconColor = TextWhite
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }
        }

        // --- SECTION 2: Motivational Quotes Management ---
        item {
          SettingsSectionCard(
            title = if (isArabic) "الجمل التحفيزية (الكارد الترحيبي)" else "Welcome Card Quotes",
            icon = Icons.Outlined.FormatQuote,
            badge = "${uiState.appSettings.motivationalQuotes.size} جمل"
          ) {
            Text(
              text = if (isArabic) "الجملة التحفيزية المعروضة حالياً في الرئيسية:" else "Currently displayed quote on Home:",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Active quote box
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "\"${uiState.user.motivationalSentence}\"",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                  onClick = onRotateQuote,
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BrightBlue.copy(alpha = 0.15f))
                ) {
                  Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = if (isArabic) "تدوير الجملة" else "Shuffle Quote",
                    tint = BrightBlue,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Auto rotate toggle
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = if (isArabic) "تغيير الجملة تلقائياً عند فتح التطبيق" else "Auto-rotate quote on app open",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (isArabic) "يتم اختيار جملة عشوائية من قائمتك كل مرة" else "Picks a quote from your list every time",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Switch(
                checked = uiState.appSettings.autoRotateQuotes,
                onCheckedChange = onToggleAutoRotateQuotes,
                colors = SwitchDefaults.colors(checkedThumbColor = BrightBlue)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Quotes list header & Add button
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (isArabic) "قائمة الجمل المخصصة:" else "Your Quotes List:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Button(
                onClick = { isAddQuoteDialogShowing = true },
                colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = if (isArabic) "إضافة جملة" else "Add Quote", fontSize = 12.sp)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Render list of quotes
            uiState.appSettings.motivationalQuotes.forEachIndexed { index, quote ->
              val isCurrent = quote == uiState.user.motivationalSentence
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(
                    if (isCurrent) BrightBlue.copy(alpha = 0.1f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                  )
                  .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  modifier = Modifier.weight(1f),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "${index + 1}.",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isCurrent) BrightBlue else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = quote,
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                      fontSize = 12.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }

                IconButton(
                  onClick = { onDeleteQuote(index) },
                  enabled = uiState.appSettings.motivationalQuotes.size > 1,
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = if (isArabic) "حذف الجملة" else "Delete quote",
                    tint = if (uiState.appSettings.motivationalQuotes.size > 1) PriorityHigh else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }

        // --- SECTION 3: Bottom Navigation Bar Customization ---
        item {
          SettingsSectionCard(
            title = if (isArabic) "التحكم في الشريط السفلي (Bottom Nav)" else "Bottom Navigation Bar",
            icon = Icons.Outlined.ViewDay,
            badge = "${uiState.appSettings.bottomNavTabs.size} تابات مفعلة"
          ) {
            Text(
              text = if (isArabic)
                "حدد التابات التي تريد ظهورها في الشريط السفلي ورتّبها حسب رغبتك (من 2 إلى 5 تابات):"
              else
                "Select which tabs appear in bottom bar and reorder them (2 to 5 tabs):",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            // All possible destinations to toggle
            val allDestinations = listOf(
              ScreenDestination.HOME,
              ScreenDestination.GOALS,
              ScreenDestination.HABITS,
              ScreenDestination.TASKS,
              ScreenDestination.CALENDAR,
              ScreenDestination.NOTES,
              ScreenDestination.FOCUS,
              ScreenDestination.ANALYTICS
            )

            // Current order list
            uiState.appSettings.bottomNavTabs.forEachIndexed { idx, dest ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 3.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                  .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "${idx + 1}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = BrightBlue
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Icon(
                    imageVector = dest.icon,
                    contentDescription = null,
                    tint = BrightBlue,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = AppStrings.getDestinationTitle(dest, lang),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                  // Up
                  IconButton(
                    onClick = { onMoveBottomNavTabUp(dest) },
                    enabled = idx > 0,
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.KeyboardArrowUp,
                      contentDescription = "تحريك لأعلى",
                      tint = if (idx > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                    )
                  }
                  // Down
                  IconButton(
                    onClick = { onMoveBottomNavTabDown(dest) },
                    enabled = idx < uiState.appSettings.bottomNavTabs.size - 1,
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.KeyboardArrowDown,
                      contentDescription = "تحريك لأسفل",
                      tint = if (idx < uiState.appSettings.bottomNavTabs.size - 1) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                    )
                  }
                  // Remove from bar
                  IconButton(
                    onClick = { onToggleBottomNavTab(dest) },
                    enabled = uiState.appSettings.bottomNavTabs.size > 2,
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Delete,
                      contentDescription = "إزالة من الشريط",
                      tint = if (uiState.appSettings.bottomNavTabs.size > 2) PriorityHigh else MaterialTheme.colorScheme.outline,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = if (isArabic) "إضافة أقسام أخرى للشريط السفلي:" else "Add more tabs to bottom bar:",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              allDestinations.filter { !uiState.appSettings.bottomNavTabs.contains(it) }.forEach { dest ->
                FilterChip(
                  selected = false,
                  onClick = { onToggleBottomNavTab(dest) },
                  label = {
                    Text(
                      text = "+ ${AppStrings.getDestinationTitle(dest, lang)}",
                      fontSize = 12.sp
                    )
                  },
                  leadingIcon = {
                    Icon(imageVector = dest.icon, contentDescription = null, modifier = Modifier.size(16.dp))
                  }
                )
              }
            }
          }
        }

        // --- SECTION 4: Data Export System ---
        item {
          SettingsSectionCard(
            title = if (isArabic) "تصدير البيانات (Excel / JSON / تقرير)" else "Data Export System",
            icon = Icons.Outlined.Download,
            badge = "${uiState.exportsList.size} ملف مصدر"
          ) {
            Text(
              text = if (isArabic)
                "اختر البيانات المحددة التي تريد تصديرها والصيغة المطلوبة:"
              else
                "Select specific data to export and the desired format:",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Checkbox options for data to include
            ExportDataType.values().forEach { type ->
              val isChecked = selectedExportTypes.contains(type)
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    selectedExportTypes = if (isChecked) {
                      selectedExportTypes - type
                    } else {
                      selectedExportTypes + type
                    }
                  }
                  .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Checkbox(
                  checked = isChecked,
                  onCheckedChange = { checked ->
                    selectedExportTypes = if (checked) {
                      selectedExportTypes + type
                    } else {
                      selectedExportTypes - type
                    }
                  },
                  colors = CheckboxDefaults.colors(checkedColor = BrightBlue)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (isArabic) type.titleArabic else type.titleEnglish,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Format Selection Chips
            Text(
              text = if (isArabic) "صيغة التصدير المطلوبة:" else "Export Format:",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              ExportFormat.values().forEach { format ->
                val isSelected = selectedExportFormat == format
                FilterChip(
                  selected = isSelected,
                  onClick = { selectedExportFormat = format },
                  label = {
                    Text(
                      text = if (isArabic) format.titleArabic else format.titleEnglish,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BrightBlue,
                    selectedLabelColor = TextWhite
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Export Button
            Button(
              onClick = {
                onExportData(context, selectedExportTypes, selectedExportFormat, true)
              },
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(imageVector = Icons.Outlined.Download, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isArabic) "تصدير الملف وحفظه ومشاركته 📤" else "Export, Save & Share File 📤",
                fontWeight = FontWeight.Bold
              )
            }

            // Exported Files List
            if (uiState.exportsList.isNotEmpty()) {
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = if (isArabic) "ملفات التصدير المحفوظة في جهازك:" else "Exported Files on Device:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(6.dp))

              uiState.exportsList.take(4).forEach { fileInfo ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = fileInfo.fileName,
                      style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                      color = MaterialTheme.colorScheme.onSurface,
                      maxLines = 1
                    )
                    Text(
                      text = "${fileInfo.dateFormatted} • ${fileInfo.fileSizeFormatted}",
                      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  IconButton(
                    onClick = { onShareFile(context, fileInfo) },
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Share,
                      contentDescription = "مشاركة الملف",
                      tint = BrightBlue,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }
              }
            }
          }
        }

        // --- SECTION 5: Local Backup & Restore ---
        item {
          SettingsSectionCard(
            title = if (isArabic) "النسخ الاحتياطي والاسترجاع المحلي" else "Local Backup & Restore",
            icon = Icons.Outlined.Backup,
            badge = "${uiState.backupsList.size} نسخة محلية"
          ) {
            Text(
              text = if (isArabic)
                "يتم حفظ النسخ الاحتياطية بأمان تام على جهازك داخل مجلد التطبيق، ويمكنك استرجاعها في أي لحظة."
              else
                "Backups are stored safely on your device in the app folder, and can be restored anytime.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Backup Frequency Selector
            Text(
              text = if (isArabic) "توقيت النسخ الاحتياطي التلقائي:" else "Auto-backup frequency:",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              BackupFrequency.values().forEach { freq ->
                val isSelected = uiState.appSettings.backupFrequency == freq
                FilterChip(
                  selected = isSelected,
                  onClick = { onSetBackupFrequency(freq) },
                  label = {
                    Text(
                      text = if (isArabic) freq.titleArabic else freq.titleEnglish,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = HabitEmerald,
                    selectedLabelColor = TextWhite
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Create Backup Now Button
            Button(
              onClick = { onCreateBackup(context) },
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = HabitEmerald),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(imageVector = Icons.Outlined.Backup, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isArabic) "إنشاء نسخة احتياطية الآن 💾" else "Create Local Backup Now 💾",
                fontWeight = FontWeight.Bold
              )
            }

            // Backups List & Restore
            if (uiState.backupsList.isNotEmpty()) {
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = if (isArabic) "النسخ الاحتياطية المتوفرة للاستعادة:" else "Available Backups to Restore:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(6.dp))

              uiState.backupsList.take(5).forEach { backup ->
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  shape = RoundedCornerShape(12.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Column(modifier = Modifier.weight(1f)) {
                      Text(
                        text = backup.fileName,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                      )
                      Text(
                        text = backup.itemsSummary,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, color = HabitEmerald),
                        maxLines = 1
                      )
                      Text(
                        text = "${backup.dateFormatted} • ${backup.fileSizeFormatted}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Button(
                        onClick = { onOpenRestoreDialog(backup) },
                        colors = ButtonDefaults.buttonColors(containerColor = BrightBlue),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                      ) {
                        Icon(imageVector = Icons.Outlined.Restore, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = if (isArabic) "استعادة" else "Restore", fontSize = 11.sp)
                      }
                      Spacer(modifier = Modifier.width(6.dp))
                      IconButton(
                        onClick = { onShareFile(context, backup) },
                        modifier = Modifier.size(32.dp)
                      ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = BrightBlue, modifier = Modifier.size(18.dp))
                      }
                    }
                  }
                }
              }
            }
          }
        }

        // --- SECTION 6: Profile & Extra Controls ---
        item {
          SettingsSectionCard(
            title = if (isArabic) "الملف الشخصي والتفضيلات" else "Profile & Preferences",
            icon = Icons.Outlined.Person,
            badge = uiState.user.name
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GoalBlueBg),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = uiState.appSettings.userAvatarEmoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = uiState.user.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = uiState.appSettings.userTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              OutlinedButton(
                onClick = {
                  editNameInput = uiState.user.name
                  editTitleInput = uiState.appSettings.userTitle
                  isEditProfileDialogShowing = true
                },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text(text = if (isArabic) "تعديل" else "Edit", fontSize = 12.sp)
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = if (isArabic) "اختيار الأيقونة الرمزية:" else "Choose Avatar Icon:",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            val emojis = listOf("👤", "🚀", "💻", "🧠", "⚡", "🌟", "🎯", "🏋️‍♂️")
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              emojis.forEach { emoji ->
                val isSelected = uiState.appSettings.userAvatarEmoji == emoji
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                      if (isSelected) BrightBlue.copy(alpha = 0.2f)
                      else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    )
                    .clickable { onSetUserAvatarEmoji(emoji) },
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = emoji, fontSize = 18.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(14.dp))

            // Sound & Haptics toggles
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = if (isArabic) "المؤثرات الصوتية لجلسات التركيز" else "Focus sound effects",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
              )
              Switch(
                checked = uiState.appSettings.soundEnabled,
                onCheckedChange = onToggleSounds,
                colors = SwitchDefaults.colors(checkedThumbColor = BrightBlue)
              )
            }
          }
        }

        // --- SECTION 7: Storage Stats & Clear All Data ---
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(
                  imageVector = Icons.Outlined.Storage,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.size(20.dp)
                )
                Text(
                  text = if (isArabic) "إحصائيات التخزين وبيانات التطبيق" else "Storage Stats & App Data",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Storage counter chips
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                StorageCounterCard(
                  label = if (isArabic) "العادات" else "Habits",
                  count = uiState.habits.size,
                  modifier = Modifier.weight(1f)
                )
                StorageCounterCard(
                  label = if (isArabic) "المهام" else "Tasks",
                  count = uiState.generalTasks.size,
                  modifier = Modifier.weight(1f)
                )
                StorageCounterCard(
                  label = if (isArabic) "الأهداف" else "Goals",
                  count = uiState.goals.size,
                  modifier = Modifier.weight(1f)
                )
                StorageCounterCard(
                  label = if (isArabic) "الأفكار" else "Notes",
                  count = uiState.notes.size,
                  modifier = Modifier.weight(1f)
                )
              }

              Spacer(modifier = Modifier.height(18.dp))

              // Clear All Data Warning Box
              Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PriorityHighBg.copy(alpha = 0.5f))
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Outlined.Warning,
                      contentDescription = null,
                      tint = PriorityHigh,
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = if (isArabic) "بدء الاستخدام الفعلي وحذف بيانات الاختبار" else "Clear Test Data for Real Use",
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                      color = PriorityHigh
                    )
                  }
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = if (isArabic)
                      "استخدم هذا الزر لمسح البيانات التجريبية والبدء على نظيف.\n🛡️ ملاحظة: هذا الزر لا يؤثر إطلاقاً على ملفات النسخ الاحتياطي والتصدير المحفوظة في جهازك."
                    else
                      "Use this button to reset dummy test data and start fresh.\n🛡️ Note: This will NOT delete any backups or exported files on your device.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  Button(
                    onClick = onOpenClearDataDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = PriorityHigh),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Icon(imageVector = Icons.Outlined.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = if (isArabic) "حذف جميع بيانات التطبيق الآن 🗑️" else "Clear All App Data Now 🗑️",
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // --- DIALOGS ---

  // 1. Clear All Data Confirmation Dialog
  if (uiState.isClearDataConfirmDialogOpen) {
    AlertDialog(
      onDismissRequest = onCloseClearDataDialog,
      icon = {
        Icon(
          imageVector = Icons.Outlined.Warning,
          contentDescription = null,
          tint = PriorityHigh,
          modifier = Modifier.size(36.dp)
        )
      },
      title = {
        Text(
          text = if (isArabic) "تأكيد حذف جميع البيانات" else "Confirm Reset All Data",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Text(
          text = if (isArabic)
            "هل أنت متأكد من رغبتك في حذف جميع المهام، العادات، الأهداف، والملاحظات لبدء استخدام حقيقي للتطبيق؟\n\n✅ كن مطمئناً: ملفات النسخ الاحتياطي ومجلد التصدير ستظل محفوظة في جهازك ولن تُمس نهائياً، ويمكنك استعادة بياناتك في أي وقت."
          else
            "Are you sure you want to clear all tasks, habits, goals, and notes to start fresh?\n\n✅ Rest assured: your backup files and exported files will stay completely untouched on your device.",
          style = MaterialTheme.typography.bodyMedium
        )
      },
      confirmButton = {
        Button(
          onClick = { onConfirmClearAllData(context) },
          colors = ButtonDefaults.buttonColors(containerColor = PriorityHigh)
        ) {
          Text(text = if (isArabic) "نعم، احذف البيانات" else "Yes, Clear Data")
        }
      },
      dismissButton = {
        TextButton(onClick = onCloseClearDataDialog) {
          Text(text = if (isArabic) "إلغاء" else "Cancel")
        }
      }
    )
  }

  // 2. Restore Backup Confirmation Dialog
  if (uiState.isRestoreConfirmDialogOpen && uiState.selectedBackupToRestore != null) {
    val backup = uiState.selectedBackupToRestore!!
    AlertDialog(
      onDismissRequest = onCloseRestoreDialog,
      icon = {
        Icon(
          imageVector = Icons.Outlined.Restore,
          contentDescription = null,
          tint = BrightBlue,
          modifier = Modifier.size(36.dp)
        )
      },
      title = {
        Text(
          text = if (isArabic) "تأكيد استعادة النسخة الاحتياطية" else "Confirm Restore Backup",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column {
          Text(
            text = if (isArabic)
              "هل ترغب في استعادة البيانات من النسخة المحددة؟ سيتم تحديث العادات والمهام والأهداف والملاحظات."
            else
              "Do you want to restore data from this backup? Your habits, tasks, goals and notes will be restored.",
            style = MaterialTheme.typography.bodyMedium
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "📁 ${backup.fileName}",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = BrightBlue
          )
          Text(
            text = backup.itemsSummary,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { onConfirmRestore(context) },
          colors = ButtonDefaults.buttonColors(containerColor = BrightBlue)
        ) {
          Text(text = if (isArabic) "تأكيد الاستعادة" else "Confirm Restore")
        }
      },
      dismissButton = {
        TextButton(onClick = onCloseRestoreDialog) {
          Text(text = if (isArabic) "إلغاء" else "Cancel")
        }
      }
    )
  }

  // 3. Add Motivational Quote Dialog
  if (isAddQuoteDialogShowing) {
    AlertDialog(
      onDismissRequest = { isAddQuoteDialogShowing = false },
      title = {
        Text(
          text = if (isArabic) "إضافة جملة تحفيزية جديدة" else "Add New Motivational Quote",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column {
          Text(
            text = if (isArabic) "اكتب جملة ملهمة لتظهر في الكارد الترحيبي بالرئيسية:" else "Enter an inspiring sentence for the welcome card:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = newQuoteInput,
            onValueChange = { newQuoteInput = it },
            placeholder = { Text(if (isArabic) "مثال: الانضباط يصنع المعجزات..." else "e.g., Discipline creates miracles...") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newQuoteInput.isNotBlank()) {
              onAddQuote(newQuoteInput)
              newQuoteInput = ""
              isAddQuoteDialogShowing = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BrightBlue)
        ) {
          Text(text = if (isArabic) "إضافة وحفظ" else "Add & Save")
        }
      },
      dismissButton = {
        TextButton(onClick = { isAddQuoteDialogShowing = false }) {
          Text(text = if (isArabic) "إلغاء" else "Cancel")
        }
      }
    )
  }

  // 4. Edit Profile Dialog
  if (isEditProfileDialogShowing) {
    AlertDialog(
      onDismissRequest = { isEditProfileDialogShowing = false },
      title = {
        Text(
          text = if (isArabic) "تعديل الملف الشخصي" else "Edit Profile",
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = editNameInput,
            onValueChange = { editNameInput = it },
            label = { Text(if (isArabic) "الاسم" else "Name") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editTitleInput,
            onValueChange = { editTitleInput = it },
            label = { Text(if (isArabic) "اللقب / الوصف" else "Title / Subtitle") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (editNameInput.isNotBlank()) {
              onSaveUserName(editNameInput)
            }
            if (editTitleInput.isNotBlank()) {
              onSetUserTitle(editTitleInput)
            }
            isEditProfileDialogShowing = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = BrightBlue)
        ) {
          Text(text = if (isArabic) "حفظ التغييرات" else "Save Changes")
        }
      },
      dismissButton = {
        TextButton(onClick = { isEditProfileDialogShowing = false }) {
          Text(text = if (isArabic) "إلغاء" else "Cancel")
        }
      }
    )
  }
}

@Composable
private fun SettingsSectionCard(
  title: String,
  icon: ImageVector,
  badge: String? = null,
  content: @Composable () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(BrightBlue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = BrightBlue,
              modifier = Modifier.size(18.dp)
            )
          }
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        if (badge != null) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = badge,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      content()
    }
  }
}

@Composable
private fun StorageCounterCard(
  label: String,
  count: Int,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp, horizontal = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "$count",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        ),
        color = BrightBlue
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
