package com.example.viewmodel

import com.example.model.DailyAnalytics
import com.example.model.Goal
import com.example.model.Habit
import com.example.model.QuickAddType
import com.example.model.ScreenDestination
import com.example.model.Task
import com.example.model.User

enum class GoalFilter(val titleArabic: String) {
  ALL("الكل"),
  ACTIVE("النشطة"),
  PAUSED("المتوقفة مؤقتاً"),
  COMPLETED("المكتملة")
}

enum class HabitFilter(val titleArabic: String) {
  TODAY("اليوم"),
  ALL("كل العادات"),
  MANDATORY("إجبارية 🛡️"),
  SCHEDULED("دورية 📅")
}

data class MyOSUiState(
  val user: User = User(),
  val habits: List<Habit> = emptyList(),
  val generalTasks: List<Task> = emptyList(),
  val goals: List<Goal> = emptyList(),
  val dailyAnalytics: DailyAnalytics = DailyAnalytics(0, 0, 0, 0, 0, 0),
  val currentScreen: ScreenDestination = ScreenDestination.HOME,
  val selectedGoalId: String? = null,
  val activeGoalFilter: GoalFilter = GoalFilter.ALL,
  val activeHabitFilter: HabitFilter = HabitFilter.TODAY,
  val isCreateGoalSheetVisible: Boolean = false,
  val editingGoal: Goal? = null,
  val isCreateHabitSheetVisible: Boolean = false,
  val editingHabit: Habit? = null,
  val isAddGoalTaskDialogVisible: Boolean = false,
  val editingGoalTask: Task? = null,
  val isQuickAddSheetVisible: Boolean = false,
  val activeCreationDialog: QuickAddType? = null,
  val isEditNameDialogVisible: Boolean = false,
  val selectedHabitToLog: Habit? = null,
  val notificationMessage: String? = null
) {
  val selectedGoal: Goal?
    get() = goals.find { it.id == selectedGoalId }

  val filteredGoals: List<Goal>
    get() = when (activeGoalFilter) {
      GoalFilter.ALL -> goals
      GoalFilter.ACTIVE -> goals.filter { !it.isPaused && it.progressPercentage < 100 }
      GoalFilter.PAUSED -> goals.filter { it.isPaused }
      GoalFilter.COMPLETED -> goals.filter { it.progressPercentage == 100 && !it.isPaused }
    }

  val filteredHabits: List<Habit>
    get() {
      val todayDay = com.example.model.getCurrentDayOfWeekArabic()
      return when (activeHabitFilter) {
        HabitFilter.TODAY -> habits.filter { it.isScheduledForToday(todayDay) }
        HabitFilter.ALL -> habits
        HabitFilter.MANDATORY -> habits.filter { it.isMandatory }
        HabitFilter.SCHEDULED -> habits.filter { it.frequency == com.example.model.HabitFrequency.SPECIFIC_DAYS }
      }
    }
}
