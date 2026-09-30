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
  ALL("الكل"),
  MANDATORY("إجبارية 🛡️"),
  SCHEDULED("دورية 📅"),
  PAUSED("مركونة ⏸️")
}

enum class TaskFilter(val titleArabic: String) {
  ALL("الكل"),
  TODAY("اليوم ⚡"),
  UPCOMING("قريباً / غداً 📅"),
  NO_DATE("بدون موعد 📭"),
  COMPLETED("المكتملة ✅")
}

enum class TaskSortOrder(val titleArabic: String) {
  PRIORITY("حسب الأولوية"),
  NEWEST("الأحدث أولاً"),
  OLDEST("الأقدم أولاً")
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
  val activeTaskFilter: TaskFilter = TaskFilter.ALL,
  val taskSortOrder: TaskSortOrder = TaskSortOrder.PRIORITY,
  val isCreateGoalSheetVisible: Boolean = false,
  val editingGoal: Goal? = null,
  val isCreateHabitSheetVisible: Boolean = false,
  val editingHabit: Habit? = null,
  val isCreateTaskSheetVisible: Boolean = false,
  val editingGeneralTask: Task? = null,
  val isAddGoalTaskDialogVisible: Boolean = false,
  val editingGoalTask: Task? = null,
  val isQuickAddSheetVisible: Boolean = false,
  val activeCreationDialog: QuickAddType? = null,
  val isEditNameDialogVisible: Boolean = false,
  val selectedHabitToLog: Habit? = null,
  val selectedHabitToPause: Habit? = null,
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
        HabitFilter.MANDATORY -> habits.filter { it.isMandatory && !it.isCurrentlyPaused }
        HabitFilter.SCHEDULED -> habits.filter { it.frequency == com.example.model.HabitFrequency.SPECIFIC_DAYS && !it.isCurrentlyPaused }
        HabitFilter.PAUSED -> habits.filter { it.isCurrentlyPaused }
      }
    }

  val filteredGeneralTasks: List<Task>
    get() {
      val base = when (activeTaskFilter) {
        TaskFilter.ALL -> generalTasks.filter { !it.isGoalTask }
        TaskFilter.TODAY -> generalTasks.filter { !it.isGoalTask && it.isDueToday }
        TaskFilter.UPCOMING -> generalTasks.filter {
          !it.isGoalTask && (it.schedule == com.example.model.TaskSchedule.TOMORROW || it.schedule == com.example.model.TaskSchedule.FUTURE) && !it.isDueToday
        }
        TaskFilter.NO_DATE -> generalTasks.filter { !it.isGoalTask && it.schedule == com.example.model.TaskSchedule.NO_DATE }
        TaskFilter.COMPLETED -> generalTasks.filter { !it.isGoalTask && it.isCompleted }
      }

      return when (taskSortOrder) {
        TaskSortOrder.PRIORITY -> base.sortedWith(
          compareBy<Task> { it.isCompleted }
            .thenBy { it.priority.rank }
            .thenByDescending { it.createdAt }
        )
        TaskSortOrder.NEWEST -> base.sortedWith(
          compareBy<Task> { it.isCompleted }
            .thenByDescending { it.createdAt }
        )
        TaskSortOrder.OLDEST -> base.sortedWith(
          compareBy<Task> { it.isCompleted }
            .thenBy { it.createdAt }
        )
      }
    }
}
