package com.example.model

object AppStrings {
  fun appTitle(lang: AppLanguage): String = "MyOS"

  fun appSubtitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "عقلك الثاني"
    AppLanguage.ENGLISH -> "Second Brain"
  }

  fun welcome(name: String, lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "مرحباً $name 👋"
    AppLanguage.ENGLISH -> "Welcome, $name 👋"
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

  fun todayAgenda(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "عليك اليوم"
    AppLanguage.ENGLISH -> "Today's Agenda"
  }

  fun viewAll(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "عرض الكل"
    AppLanguage.ENGLISH -> "View All"
  }

  fun dailySummary(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "ملخص يومك"
    AppLanguage.ENGLISH -> "Daily Summary"
  }

  fun comprehensiveAnalytics(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "التحليلات الشاملة 📊"
    AppLanguage.ENGLISH -> "Analytics 📊"
  }

  fun completed(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "مكتملة"
    AppLanguage.ENGLISH -> "Completed"
  }

  fun remaining(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "متبقية"
    AppLanguage.ENGLISH -> "Remaining"
  }

  fun tasksDueToday(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "المهام المستحقة اليوم"
    AppLanguage.ENGLISH -> "Tasks Due Today"
  }

  fun activeGoals(count: Int, lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "$count أهداف نشطة"
    AppLanguage.ENGLISH -> "$count Active Goals"
  }

  fun completedOf(completed: Int, total: Int, lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "$completed من $total مكتملة"
    AppLanguage.ENGLISH -> "$completed of $total done"
  }

  fun restModeActiveNotice(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "وضع الراحة مفعّل: تظهر العادات الإجبارية فقط، وبقية العادات في استراحة مستحقة ☕"
    AppLanguage.ENGLISH -> "Rest Mode active: Showing mandatory habits only ☕"
  }

  fun closeMenu(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "إغلاق القائمة"
    AppLanguage.ENGLISH -> "Close Menu"
  }

  fun journeyQuote(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "\"رحلتك للأفضل\nتبدأ من هنا\""
    AppLanguage.ENGLISH -> "\"Your journey to greatness\nstarts right here\""
  }

  fun editName(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "تعديل الاسم"
    AppLanguage.ENGLISH -> "Edit Name"
  }

  fun shuffleQuote(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "تبديل الجملة التحفيزية"
    AppLanguage.ENGLISH -> "Shuffle Quote"
  }

  fun postponeToTomorrow(lang: AppLanguage): String = when (lang) {
    AppLanguage.ARABIC -> "تمرير للترحيل للغد ➡️"
    AppLanguage.ENGLISH -> "Swipe to postpone to tomorrow ➡️"
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
