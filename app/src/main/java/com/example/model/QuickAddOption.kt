package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FolderSpecial
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.BrightBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.HabitEmerald
import com.example.ui.theme.IndigoPrimary

enum class QuickAddType(
  val titleArabic: String,
  val titleEnglish: String,
  val icon: ImageVector,
  val accentColor: Color
) {
  TASK("إضافة مهمة", "Add Task", Icons.Outlined.CheckBox, BrightBlue),
  HABIT("إضافة عادة", "Add Habit", Icons.Outlined.Spa, HabitEmerald),
  GOAL("إضافة هدف", "Add Goal", Icons.Outlined.TrackChanges, IndigoPrimary),
  NOTE("إضافة ملاحظة", "Add Note", Icons.Outlined.Description, Color(0xFFF59E0B)),
  PROJECT("إضافة مشروع", "Add Project", Icons.Outlined.FolderSpecial, ElectricViolet),
  LIST("إضافة قائمة", "Add List", Icons.AutoMirrored.Outlined.List, ElectricCyan),
  EVENT("إضافة حدث", "Add Event", Icons.Outlined.CalendarMonth, DeepBlue),
  FILE("إضافة ملف", "Add File", Icons.AutoMirrored.Outlined.InsertDriveFile, Color(0xFF64748B))
}
