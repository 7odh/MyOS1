package com.example.model

enum class ThemeMode(val titleArabic: String, val titleEnglish: String) {
  SYSTEM("تلقائي (حسب النظام)", "System Default"),
  LIGHT("الوضع الفاتح ☀️", "Light Mode ☀️"),
  DARK("الوضع الداكن 🌙", "Dark Mode 🌙")
}

enum class AppLanguage(val titleArabic: String, val titleEnglish: String, val code: String) {
  ARABIC("العربية 🇸🇦", "Arabic 🇸🇦", "ar"),
  ENGLISH("الإنجليزية 🇬🇧", "English 🇬🇧", "en")
}

enum class BackupFrequency(val titleArabic: String, val titleEnglish: String, val intervalDays: Int) {
  MANUAL("يدوي فقط", "Manual Only", 0),
  DAILY("يومي تلقائي", "Daily Automatic", 1),
  WEEKLY("أسبوعي تلقائي", "Weekly Automatic", 7),
  MONTHLY("شهري تلقائي", "Monthly Automatic", 30)
}

enum class ExportDataType(val titleArabic: String, val titleEnglish: String) {
  TASKS("المهام العامة", "General Tasks"),
  HABITS("العادات اليومية", "Daily Habits"),
  GOALS("الأهداف ومهامها", "Goals & Subtasks"),
  NOTES("الملاحظات ومخزن الأفكار", "Notes & Ideas"),
  HISTORY("سجل الأيام والأرشيف", "Daily History & Logs"),
  FOCUS("جلسات التركيز", "Focus Sessions")
}

enum class ExportFormat(val titleArabic: String, val titleEnglish: String, val extension: String, val mimeType: String) {
  CSV("شيت إكسل / CSV", "Excel / CSV Sheet", ".csv", "text/csv"),
  JSON("ملف بيانات كامل JSON", "Complete JSON Data", ".json", "application/json"),
  REPORT("تقرير نصي شامل", "Detailed Text Report", ".txt", "text/plain")
}

data class BackupFileInfo(
  val fileName: String,
  val filePath: String,
  val fileSizeFormatted: String,
  val timestamp: Long,
  val dateFormatted: String,
  val itemsSummary: String,
  val isExport: Boolean = false
)

data class AppSettings(
  val themeMode: ThemeMode = ThemeMode.SYSTEM,
  val language: AppLanguage = AppLanguage.ARABIC,
  val motivationalQuotes: List<String> = listOf(
    "كل يوم هو فرصة جديدة لتكون أفضل من أمس.",
    "النجاح هو مجموع قرارات وانضباطات يومية صغيرة.",
    "ركّز على الخطوة الحالية ولا تقلق بشأن الدرج بأكمله.",
    "الاستمرارية تتغلب على الموهبة غير المنضبطة دائماً.",
    "أعظم استثمار هو الاستثمار في عقلك وعاداتك.",
    "ابدأ بما هو ضروري، ثم بما هو ممكن، لتجد نفسك تصنع المستحيل.",
    "الانضباط هو الجسر بين أهدافك وإنجازاتك."
  ),
  val currentQuoteIndex: Int = 0,
  val autoRotateQuotes: Boolean = true,
  val bottomNavTabs: List<ScreenDestination> = listOf(
    ScreenDestination.HOME,
    ScreenDestination.GOALS,
    ScreenDestination.HABITS,
    ScreenDestination.TASKS,
    ScreenDestination.CALENDAR
  ),
  val backupFrequency: BackupFrequency = BackupFrequency.DAILY,
  val lastBackupTimestamp: Long = 0L,
  val soundEnabled: Boolean = true,
  val hapticsEnabled: Boolean = true,
  val userAvatarEmoji: String = "👤",
  val userTitle: String = "رائد إنتاجية"
) {
  val currentQuote: String
    get() = if (motivationalQuotes.isNotEmpty()) {
      motivationalQuotes[currentQuoteIndex.coerceIn(0, motivationalQuotes.size - 1)]
    } else "كل يوم فرصة جديدة للإنجاز والنمو."
}
