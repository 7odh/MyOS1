package com.example.model

object AppStrings {
  fun appTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "MyOS"
    AppLanguage.ENGLISH -> "MyOS"
  }

  fun appSubtitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "عقلك الثاني"
    AppLanguage.ENGLISH -> "Second Brain"
  }

  fun welcome(name: String, lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "مرحباً $name 👋"
    AppLanguage.ENGLISH -> "Welcome $name 👋"
  }

  fun restMode(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "راحة"
    AppLanguage.ENGLISH -> "Rest"
  }

  fun settings(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "الإعدادات والضبط"
    AppLanguage.ENGLISH -> "Settings & Control"
  }

  fun home(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "الرئيسية"
    AppLanguage.ENGLISH -> "Home"
  }

  fun goals(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "الأهداف"
    AppLanguage.ENGLISH -> "Goals"
  }

  fun habits(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "العادات"
    AppLanguage.ENGLISH -> "Habits"
  }

  fun tasks(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "المهام"
    AppLanguage.ENGLISH -> "Tasks"
  }

  fun calendar(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "التقويم"
    AppLanguage.ENGLISH -> "Calendar"
  }

  fun notes(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "الملاحظات"
    AppLanguage.ENGLISH -> "Notes"
  }

  fun focus(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "التركيز"
    AppLanguage.ENGLISH -> "Focus"
  }

  fun analytics(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "التحليلات"
    AppLanguage.ENGLISH -> "Analytics"
  }

  fun search(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "البحث"
    AppLanguage.ENGLISH -> "Search"
  }

  fun getDestinationTitle(dest: ScreenDestination, lang: AppLanguage): String {
    return when (lang) {
      AppLanguage.ARABIC -> dest.titleArabic
      AppLanguage.ENGLISH -> when (dest) {
        ScreenDestination.HOME -> "Home"
        ScreenDestination.GOALS -> "Goals"
        ScreenDestination.HABITS -> "Habits"
        ScreenDestination.TASKS -> "Tasks"
        ScreenDestination.CALENDAR -> "Calendar"
        ScreenDestination.PROJECTS -> "Projects"
        ScreenDestination.LISTS -> "Lists"
        ScreenDestination.NOTES -> "Notes"
        ScreenDestination.KNOWLEDGE -> "Knowledge"
        ScreenDestination.FOCUS -> "Focus"
        ScreenDestination.ANALYTICS -> "Analytics"
        ScreenDestination.SEARCH -> "Search"
        ScreenDestination.MORE -> "More"
        ScreenDestination.SETTINGS -> "Settings"
        ScreenDestination.HELP -> "Help"
      }
    }
  }
}
