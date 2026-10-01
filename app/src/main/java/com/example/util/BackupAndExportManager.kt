package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.model.DayHabitRecord
import com.example.model.DayOfWeekArabic
import com.example.model.DaySummaryHistory
import com.example.model.DayTaskRecord
import com.example.model.ExportDataType
import com.example.model.ExportFormat
import com.example.model.Goal
import com.example.model.GoalStatus
import com.example.model.Habit
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Note
import com.example.model.Priority
import com.example.model.Task
import com.example.model.TaskSchedule
import com.example.model.User
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ExportDataBundle(
  val user: User,
  val tasks: List<Task>,
  val habits: List<Habit>,
  val goals: List<Goal>,
  val notes: List<Note>,
  val dailyHistory: Map<String, DaySummaryHistory>,
  val focusMinutesToday: Int = 0
)

data class RestoredDataBundle(
  val user: User?,
  val tasks: List<Task>,
  val habits: List<Habit>,
  val goals: List<Goal>,
  val notes: List<Note>,
  val dailyHistory: Map<String, DaySummaryHistory>
)

object BackupAndExportManager {

  private fun getBackupsDir(context: Context): File {
    val base = context.getExternalFilesDir(null) ?: context.filesDir
    val dir = File(base, "backups")
    if (!dir.exists()) dir.mkdirs()
    return dir
  }

  private fun getExportsDir(context: Context): File {
    val base = context.getExternalFilesDir(null) ?: context.filesDir
    val dir = File(base, "exports")
    if (!dir.exists()) dir.mkdirs()
    return dir
  }

  fun getBackupsCount(context: Context): Int {
    return getBackupsDir(context).listFiles { file -> file.name.endsWith(".json") }?.size ?: 0
  }

  fun getExportsCount(context: Context): Int {
    return getExportsDir(context).listFiles()?.size ?: 0
  }

  fun listBackups(context: Context): List<com.example.model.BackupFileInfo> {
    val dir = getBackupsDir(context)
    val files = dir.listFiles { f -> f.name.endsWith(".json") } ?: return emptyList()
    return files.map { f ->
      val length = f.length()
      val sizeStr = if (length < 1024) "$length B" else "${length / 1024} KB"
      val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
      val dateFormatted = dateFormat.format(Date(f.lastModified()))

      // Read small summary from json
      var summary = "نسخة احتياطية"
      try {
        val text = f.readText()
        val json = JSONObject(text)
        val habitsCount = json.optJSONArray("habits")?.length() ?: 0
        val tasksCount = json.optJSONArray("tasks")?.length() ?: 0
        val goalsCount = json.optJSONArray("goals")?.length() ?: 0
        val notesCount = json.optJSONArray("notes")?.length() ?: 0
        summary = "$habitsCount عادات | $tasksCount مهام | $goalsCount أهداف | $notesCount أفكار"
      } catch (_: Exception) {}

      com.example.model.BackupFileInfo(
        fileName = f.name,
        filePath = f.absolutePath,
        fileSizeFormatted = sizeStr,
        timestamp = f.lastModified(),
        dateFormatted = dateFormatted,
        itemsSummary = summary,
        isExport = false
      )
    }.sortedByDescending { it.timestamp }
  }

  fun listExports(context: Context): List<com.example.model.BackupFileInfo> {
    val dir = getExportsDir(context)
    val files = dir.listFiles() ?: return emptyList()
    return files.map { f ->
      val length = f.length()
      val sizeStr = if (length < 1024) "$length B" else "${length / 1024} KB"
      val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
      val dateFormatted = dateFormat.format(Date(f.lastModified()))

      val ext = f.extension.uppercase()
      val summary = "ملف تصدير $ext"

      com.example.model.BackupFileInfo(
        fileName = f.name,
        filePath = f.absolutePath,
        fileSizeFormatted = sizeStr,
        timestamp = f.lastModified(),
        dateFormatted = dateFormatted,
        itemsSummary = summary,
        isExport = true
      )
    }.sortedByDescending { it.timestamp }
  }

  fun createBackup(context: Context, data: ExportDataBundle): File {
    val dir = getBackupsDir(context)
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val file = File(dir, "MyOS_Backup_$timeStamp.json")

    val jsonString = buildFullJson(
      data = data,
      selectedTypes = ExportDataType.values().toSet(),
      isBackup = true
    )

    FileOutputStream(file).use { fos ->
      OutputStreamWriter(fos, StandardCharsets.UTF_8).use { writer ->
        writer.write(jsonString)
        writer.flush()
      }
    }
    return file
  }

  fun restoreFromBackupFile(file: File): RestoredDataBundle? {
    return try {
      val text = file.readText(StandardCharsets.UTF_8)
      val json = JSONObject(text)

      val tasks = mutableListOf<Task>()
      val tasksArr = json.optJSONArray("tasks")
      if (tasksArr != null) {
        for (i in 0 until tasksArr.length()) {
          val obj = tasksArr.getJSONObject(i)
          tasks.add(
            Task(
              id = obj.optString("id", "t_$i"),
              title = obj.optString("title", "مهمة"),
              notes = obj.optString("notes").takeIf { it.isNotBlank() },
              priority = try { Priority.valueOf(obj.optString("priority", "MEDIUM")) } catch (_: Exception) { Priority.MEDIUM },
              isCompleted = obj.optBoolean("isCompleted", false),
              goalId = obj.optString("goalId").takeIf { it.isNotBlank() },
              isGoalTask = obj.optBoolean("isGoalTask", false),
              category = obj.optString("category").takeIf { it.isNotBlank() },
              schedule = try { TaskSchedule.valueOf(obj.optString("schedule", "TODAY")) } catch (_: Exception) { TaskSchedule.TODAY },
              dueDateFormatted = obj.optString("dueDateFormatted").takeIf { it.isNotBlank() },
              isPostponed = obj.optBoolean("isPostponed", false),
              postponedCount = obj.optInt("postponedCount", 0),
              postponedFromDate = obj.optString("postponedFromDate").takeIf { it.isNotBlank() }
            )
          )
        }
      }

      val habits = mutableListOf<Habit>()
      val habitsArr = json.optJSONArray("habits")
      if (habitsArr != null) {
        for (i in 0 until habitsArr.length()) {
          val obj = habitsArr.getJSONObject(i)
          habits.add(
            Habit(
              id = obj.optString("id", "h_$i"),
              title = obj.optString("title", "عادة"),
              type = try { HabitType.valueOf(obj.optString("type", "BOOLEAN")) } catch (_: Exception) { HabitType.BOOLEAN },
              targetValue = obj.optInt("targetValue", 1),
              currentValue = obj.optInt("currentValue", 0),
              unit = obj.optString("unit", "مرة"),
              frequency = try { HabitFrequency.valueOf(obj.optString("frequency", "DAILY")) } catch (_: Exception) { HabitFrequency.DAILY },
              isMandatory = obj.optBoolean("isMandatory", false),
              priority = try { Priority.valueOf(obj.optString("priority", "HIGH")) } catch (_: Exception) { Priority.HIGH },
              currentStreak = obj.optInt("currentStreak", 0),
              iconEmoji = obj.optString("iconEmoji", "🌱"),
              isPaused = obj.optBoolean("isPaused", false),
              pauseUntilDate = if (obj.has("pauseUntilDate")) obj.optLong("pauseUntilDate") else null
            )
          )
        }
      }

      val goals = mutableListOf<Goal>()
      val goalsArr = json.optJSONArray("goals")
      if (goalsArr != null) {
        for (i in 0 until goalsArr.length()) {
          val obj = goalsArr.getJSONObject(i)
          val goalTasks = mutableListOf<Task>()
          val gTasksArr = obj.optJSONArray("tasks")
          if (gTasksArr != null) {
            for (j in 0 until gTasksArr.length()) {
              val tobj = gTasksArr.getJSONObject(j)
              goalTasks.add(
                Task(
                  id = tobj.optString("id", "gt_$j"),
                  title = tobj.optString("title", "مهمة فرعية"),
                  notes = tobj.optString("notes").takeIf { it.isNotBlank() },
                  priority = try { Priority.valueOf(tobj.optString("priority", "MEDIUM")) } catch (_: Exception) { Priority.MEDIUM },
                  isCompleted = tobj.optBoolean("isCompleted", false),
                  goalId = obj.optString("id"),
                  isGoalTask = true,
                  schedule = try { TaskSchedule.valueOf(tobj.optString("schedule", "TODAY")) } catch (_: Exception) { TaskSchedule.TODAY },
                  dueDateFormatted = tobj.optString("dueDateFormatted").takeIf { it.isNotBlank() },
                  isPostponed = tobj.optBoolean("isPostponed", false)
                )
              )
            }
          }

          goals.add(
            Goal(
              id = obj.optString("id", "g_$i"),
              title = obj.optString("title", "هدف"),
              description = obj.optString("description").takeIf { it.isNotBlank() },
              iconId = obj.optString("iconId", "target"),
              priority = try { Priority.valueOf(obj.optString("priority", "HIGH")) } catch (_: Exception) { Priority.HIGH },
              dueDate = obj.optString("dueDate").takeIf { it.isNotBlank() },
              isPaused = obj.optBoolean("isPaused", false),
              tasks = goalTasks
            )
          )
        }
      }

      val notes = mutableListOf<Note>()
      val notesArr = json.optJSONArray("notes")
      if (notesArr != null) {
        for (i in 0 until notesArr.length()) {
          val obj = notesArr.getJSONObject(i)
          notes.add(
            Note(
              id = obj.optString("id", "n_$i"),
              title = obj.optString("title", "فكرة"),
              content = obj.optString("content", ""),
              colorLong = obj.optLong("colorLong", 0xFFFFFBEB),
              isPinned = obj.optBoolean("isPinned", false),
              tag = obj.optString("tag", "أفكار عامة"),
              createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
              updatedAt = obj.optLong("updatedAt", System.currentTimeMillis())
            )
          )
        }
      }

      val userObj = json.optJSONObject("user")
      val user = if (userObj != null) {
        User(
          name = userObj.optString("name", "أحمد"),
          motivationalSentence = userObj.optString("motivationalSentence", "كل يوم فرصة جديدة لتكون أفضل من أمس."),
          isMotivationEnabled = userObj.optBoolean("isMotivationEnabled", true),
          isRestModeActive = userObj.optBoolean("isRestModeActive", false)
        )
      } else null

      RestoredDataBundle(
        user = user,
        tasks = tasks,
        habits = habits,
        goals = goals,
        notes = notes,
        dailyHistory = emptyMap()
      )
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  fun exportData(
    context: Context,
    data: ExportDataBundle,
    selectedTypes: Set<ExportDataType>,
    format: ExportFormat
  ): File {
    val dir = getExportsDir(context)
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val fileName = "MyOS_Export_$timeStamp${format.extension}"
    val file = File(dir, fileName)

    when (format) {
      ExportFormat.CSV -> {
        // UTF-8 BOM \uFEFF ensures Microsoft Excel recognizes UTF-8 properly for Arabic text
        FileOutputStream(file).use { fos ->
          fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())) // BOM
          OutputStreamWriter(fos, StandardCharsets.UTF_8).use { writer ->
            writer.write(buildCsvContent(data, selectedTypes))
            writer.flush()
          }
        }
      }
      ExportFormat.JSON -> {
        val jsonStr = buildFullJson(data, selectedTypes, isBackup = false)
        FileOutputStream(file).use { fos ->
          OutputStreamWriter(fos, StandardCharsets.UTF_8).use { writer ->
            writer.write(jsonStr)
            writer.flush()
          }
        }
      }
      ExportFormat.REPORT -> {
        FileOutputStream(file).use { fos ->
          OutputStreamWriter(fos, StandardCharsets.UTF_8).use { writer ->
            writer.write(buildReportContent(data, selectedTypes))
            writer.flush()
          }
        }
      }
    }
    return file
  }

  private fun buildCsvContent(data: ExportDataBundle, selectedTypes: Set<ExportDataType>): String {
    val sb = StringBuilder()

    if (selectedTypes.contains(ExportDataType.TASKS)) {
      sb.append("=== المهام العامة (TASKS) ===\n")
      sb.append("المعرف,العنوان,الحالة,الأولوية,الموعد,تاريخ الاستحقاق,مُرحلة,الملاحظات\n")
      data.tasks.forEach { t ->
        val status = if (t.isCompleted) "مكتملة" else "قيد التنفيذ"
        val notesEscaped = "\"" + (t.notes ?: "").replace("\"", "\"\"") + "\""
        val titleEscaped = "\"" + t.title.replace("\"", "\"\"") + "\""
        sb.append("${t.id},$titleEscaped,$status,${t.priority.name},${t.schedule.name},${t.dueDateFormatted ?: ""},${if (t.isPostponed) "نعم" else "لا"},$notesEscaped\n")
      }
      sb.append("\n")
    }

    if (selectedTypes.contains(ExportDataType.HABITS)) {
      sb.append("=== العادات اليومية (HABITS) ===\n")
      sb.append("المعرف,الاسم,النوع,المستهدف,المنجز,الوحدة,التكرار,إجبارية,الأيام المتواصلة (Streak),مركونة\n")
      data.habits.forEach { h ->
        val titleEscaped = "\"" + h.title.replace("\"", "\"\"") + "\""
        sb.append("${h.id},$titleEscaped,${h.type.name},${h.targetValue},${h.currentValue},${h.unit},${h.frequency.name},${if (h.isMandatory) "نعم" else "لا"},${h.currentStreak},${if (h.isPaused) "نعم" else "لا"}\n")
      }
      sb.append("\n")
    }

    if (selectedTypes.contains(ExportDataType.GOALS)) {
      sb.append("=== الأهداف الكبرى (GOALS) ===\n")
      sb.append("المعرف,عنوان الهدف,الأولوية,الحالة,تاريخ الاستحقاق,نسبة الإنجاز,عدد المهام\n")
      data.goals.forEach { g ->
        val titleEscaped = "\"" + g.title.replace("\"", "\"\"") + "\""
        sb.append("${g.id},$titleEscaped,${g.priority.name},${g.status.name},${g.dueDate ?: ""},${g.progressPercentage}%,${g.tasks.size}\n")
        g.tasks.forEach { gt ->
          val gTitleEsc = "\"" + gt.title.replace("\"", "\"\"") + "\""
          sb.append(" ,-- مهمة تابعة: $gTitleEsc,${if (gt.isCompleted) "مكتملة" else "معلقة"},${gt.priority.name},${gt.schedule.name},${gt.dueDateFormatted ?: ""}\n")
        }
      }
      sb.append("\n")
    }

    if (selectedTypes.contains(ExportDataType.NOTES)) {
      sb.append("=== الملاحظات ومخزن الأفكار (NOTES) ===\n")
      sb.append("المعرف,العنوان,التصنيف,مثبتة,تاريخ التدوين,المحتوى\n")
      val df = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
      data.notes.forEach { n ->
        val titleEscaped = "\"" + n.title.replace("\"", "\"\"") + "\""
        val contentEscaped = "\"" + n.content.replace("\"", "\"\"") + "\""
        sb.append("${n.id},$titleEscaped,${n.tag},${if (n.isPinned) "نعم" else "لا"},${df.format(Date(n.createdAt))},$contentEscaped\n")
      }
      sb.append("\n")
    }

    return sb.toString()
  }

  private fun buildFullJson(
    data: ExportDataBundle,
    selectedTypes: Set<ExportDataType>,
    isBackup: Boolean
  ): String {
    val root = JSONObject()
    root.put("app", "MyOS")
    root.put("version", "1.0")
    root.put("type", if (isBackup) "BACKUP" else "EXPORT")
    root.put("timestamp", System.currentTimeMillis())
    root.put("exportDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()))

    val userObj = JSONObject()
    userObj.put("name", data.user.name)
    userObj.put("motivationalSentence", data.user.motivationalSentence)
    userObj.put("isMotivationEnabled", data.user.isMotivationEnabled)
    userObj.put("isRestModeActive", data.user.isRestModeActive)
    root.put("user", userObj)

    if (selectedTypes.contains(ExportDataType.TASKS)) {
      val tasksArr = JSONArray()
      data.tasks.forEach { t ->
        val tobj = JSONObject()
        tobj.put("id", t.id)
        tobj.put("title", t.title)
        tobj.put("notes", t.notes ?: "")
        tobj.put("priority", t.priority.name)
        tobj.put("isCompleted", t.isCompleted)
        tobj.put("goalId", t.goalId ?: "")
        tobj.put("isGoalTask", t.isGoalTask)
        tobj.put("category", t.category ?: "")
        tobj.put("schedule", t.schedule.name)
        tobj.put("dueDateFormatted", t.dueDateFormatted ?: "")
        tobj.put("isPostponed", t.isPostponed)
        tobj.put("postponedCount", t.postponedCount)
        tobj.put("postponedFromDate", t.postponedFromDate ?: "")
        tasksArr.put(tobj)
      }
      root.put("tasks", tasksArr)
    }

    if (selectedTypes.contains(ExportDataType.HABITS)) {
      val habitsArr = JSONArray()
      data.habits.forEach { h ->
        val hobj = JSONObject()
        hobj.put("id", h.id)
        hobj.put("title", h.title)
        hobj.put("type", h.type.name)
        hobj.put("targetValue", h.targetValue)
        hobj.put("currentValue", h.currentValue)
        hobj.put("unit", h.unit)
        hobj.put("frequency", h.frequency.name)
        hobj.put("isMandatory", h.isMandatory)
        hobj.put("priority", h.priority.name)
        hobj.put("currentStreak", h.currentStreak)
        hobj.put("iconEmoji", h.iconEmoji)
        hobj.put("isPaused", h.isPaused)
        if (h.pauseUntilDate != null) hobj.put("pauseUntilDate", h.pauseUntilDate)
        habitsArr.put(hobj)
      }
      root.put("habits", habitsArr)
    }

    if (selectedTypes.contains(ExportDataType.GOALS)) {
      val goalsArr = JSONArray()
      data.goals.forEach { g ->
        val gobj = JSONObject()
        gobj.put("id", g.id)
        gobj.put("title", g.title)
        gobj.put("description", g.description ?: "")
        gobj.put("iconId", g.iconId)
        gobj.put("priority", g.priority.name)
        gobj.put("status", g.status.name)
        gobj.put("dueDate", g.dueDate ?: "")
        gobj.put("isPaused", g.isPaused)

        val subtasksArr = JSONArray()
        g.tasks.forEach { gt ->
          val tobj = JSONObject()
          tobj.put("id", gt.id)
          tobj.put("title", gt.title)
          tobj.put("notes", gt.notes ?: "")
          tobj.put("priority", gt.priority.name)
          tobj.put("isCompleted", gt.isCompleted)
          tobj.put("schedule", gt.schedule.name)
          tobj.put("dueDateFormatted", gt.dueDateFormatted ?: "")
          tobj.put("isPostponed", gt.isPostponed)
          subtasksArr.put(tobj)
        }
        gobj.put("tasks", subtasksArr)
        goalsArr.put(gobj)
      }
      root.put("goals", goalsArr)
    }

    if (selectedTypes.contains(ExportDataType.NOTES)) {
      val notesArr = JSONArray()
      data.notes.forEach { n ->
        val nobj = JSONObject()
        nobj.put("id", n.id)
        nobj.put("title", n.title)
        nobj.put("content", n.content)
        nobj.put("colorLong", n.colorLong)
        nobj.put("isPinned", n.isPinned)
        nobj.put("tag", n.tag)
        nobj.put("createdAt", n.createdAt)
        nobj.put("updatedAt", n.updatedAt)
        notesArr.put(nobj)
      }
      root.put("notes", notesArr)
    }

    return root.toString(2)
  }

  private fun buildReportContent(data: ExportDataBundle, selectedTypes: Set<ExportDataType>): String {
    val sb = StringBuilder()
    sb.append("====================================================\n")
    sb.append("   تقرير بيانات MyOS - نظام إدارة الحياة والعقل الثاني\n")
    sb.append("====================================================\n")
    sb.append("تاريخ التقرير: ${SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())}\n")
    sb.append("المستخدم: ${data.user.name}\n")
    sb.append("الجملة التحفيزية: \"${data.user.motivationalSentence}\"\n")
    sb.append("----------------------------------------------------\n\n")

    if (selectedTypes.contains(ExportDataType.TASKS)) {
      sb.append("📋 ملخص المهام العامة:\n")
      sb.append("• إجمالي المهام: ${data.tasks.size}\n")
      sb.append("• المكتملة: ${data.tasks.count { it.isCompleted }}\n")
      sb.append("• قيد الانتظار: ${data.tasks.count { !it.isCompleted }}\n")
      sb.append("• مهام تم ترحيلها: ${data.tasks.count { it.isPostponed }}\n\n")
      data.tasks.forEachIndexed { i, t ->
        val mark = if (t.isCompleted) "[✓]" else "[ ]"
        val post = if (t.isPostponed) " (مُرحلة ➡️)" else ""
        sb.append("${i + 1}. $mark ${t.title} [أولوية: ${t.priority.name}]$post\n")
        if (!t.notes.isNullOrBlank()) sb.append("   ملاحظة: ${t.notes}\n")
      }
      sb.append("\n----------------------------------------------------\n\n")
    }

    if (selectedTypes.contains(ExportDataType.HABITS)) {
      sb.append("🌱 ملخص العادات اليومية:\n")
      sb.append("• إجمالي العادات: ${data.habits.size}\n")
      sb.append("• عادات إجبارية 🛡️: ${data.habits.count { it.isMandatory }}\n")
      sb.append("• عادات مركونة مؤقتاً ⏸️: ${data.habits.count { it.isPaused }}\n\n")
      data.habits.forEachIndexed { i, h ->
        val pauseStr = if (h.isPaused) " (مركونة مؤقتاً ⏸️)" else ""
        sb.append("${i + 1}. ${h.iconEmoji} ${h.title}: ${h.currentValue}/${h.targetValue} ${h.unit} [سلسلة الأيام: ${h.currentStreak} 🔥]$pauseStr\n")
      }
      sb.append("\n----------------------------------------------------\n\n")
    }

    if (selectedTypes.contains(ExportDataType.GOALS)) {
      sb.append("🎯 ملخص الأهداف الكبرى:\n")
      sb.append("• إجمالي الأهداف: ${data.goals.size}\n")
      sb.append("• الأهداف المكتملة 100%: ${data.goals.count { it.progressPercentage == 100 }}\n\n")
      data.goals.forEachIndexed { i, g ->
        sb.append("${i + 1}. ${g.title} [إنجاز: ${g.progressPercentage}% | مهام: ${g.tasks.size}]\n")
        if (!g.description.isNullOrBlank()) sb.append("   الوصف: ${g.description}\n")
        g.tasks.forEach { gt ->
          val m = if (gt.isCompleted) "[✓]" else "[ ]"
          sb.append("   - $m ${gt.title}\n")
        }
      }
      sb.append("\n----------------------------------------------------\n\n")
    }

    if (selectedTypes.contains(ExportDataType.NOTES)) {
      sb.append("💡 مخزن الأفكار والخواطر (${data.notes.size} فكرة):\n\n")
      data.notes.forEachIndexed { i, n ->
        val pin = if (n.isPinned) "📌 " else ""
        sb.append("${i + 1}. $pin${n.title} [${n.tag}]\n")
        sb.append("   ${n.content}\n\n")
      }
      sb.append("----------------------------------------------------\n")
    }

    sb.append("\nتم إنشاء هذا التقرير بواسطة MyOS - نظامك الشخصي والعقل الثاني ✨\n")
    return sb.toString()
  }

  fun shareFile(context: Context, file: File) {
    try {
      val uri: Uri = try {
        FileProvider.getUriForFile(
          context,
          "${context.packageName}.fileprovider",
          file
        )
      } catch (_: Exception) {
        Uri.fromFile(file)
      }

      val intent = Intent(Intent.ACTION_SEND).apply {
        type = when (file.extension.lowercase()) {
          "csv" -> "text/csv"
          "json" -> "application/json"
          else -> "text/plain"
        }
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, file.name)
        putExtra(Intent.EXTRA_TEXT, "مرفق ملف ${file.name} المصدر من تطبيق MyOS")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      }
      context.startActivity(Intent.createChooser(intent, "مشاركة الملف عبر...").apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      })
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
