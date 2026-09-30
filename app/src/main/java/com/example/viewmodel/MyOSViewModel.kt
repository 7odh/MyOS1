package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MyOSRepository
import com.example.model.DailyAnalytics
import com.example.model.DayOfWeekArabic
import com.example.model.Goal
import com.example.model.Habit
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Priority
import com.example.model.QuickAddType
import com.example.model.ScreenDestination
import com.example.model.Task
import com.example.model.TaskSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyOSViewModel(
  private val repository: MyOSRepository = MyOSRepository()
) : ViewModel() {

  private val _uiState = MutableStateFlow(MyOSUiState())
  val uiState: StateFlow<MyOSUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
        repository.user,
        repository.habits,
        repository.generalTasks,
        repository.goals
      ) { user, habits, tasks, goals ->
        val activeGoals = goals.filter { !it.isPaused }
        // Today's goals progress is calculated strictly from today's scheduled goal tasks
        val todayGoalTasks = activeGoals.flatMap { it.todayTasks }
        val analytics = DailyAnalytics(
          habitsCompleted = habits.count { it.isCompleted },
          habitsTotal = habits.size,
          tasksCompleted = tasks.count { it.isCompleted },
          tasksTotal = tasks.size,
          goalsCompleted = todayGoalTasks.count { it.isCompleted },
          goalsTotal = todayGoalTasks.size
        )
        _uiState.update { current ->
          current.copy(
            user = user,
            habits = habits,
            generalTasks = tasks,
            goals = goals,
            dailyAnalytics = analytics
          )
        }
      }.collect {}
    }
  }

  fun onScreenSelected(destination: ScreenDestination) {
    _uiState.update { it.copy(currentScreen = destination, selectedGoalId = null) }
  }

  fun onSelectGoal(goalId: String) {
    _uiState.update { it.copy(selectedGoalId = goalId) }
  }

  fun onClearSelectedGoal() {
    _uiState.update { it.copy(selectedGoalId = null) }
  }

  fun setGoalFilter(filter: GoalFilter) {
    _uiState.update { it.copy(activeGoalFilter = filter) }
  }

  fun openCreateGoalSheet() {
    _uiState.update { it.copy(isCreateGoalSheetVisible = true, editingGoal = null) }
  }

  fun closeCreateGoalSheet() {
    _uiState.update { it.copy(isCreateGoalSheetVisible = false, editingGoal = null) }
  }

  fun openEditGoal(goal: Goal) {
    _uiState.update { it.copy(isCreateGoalSheetVisible = true, editingGoal = goal) }
  }

  fun saveGoal(
    title: String,
    description: String?,
    iconId: String,
    priority: Priority,
    dueDate: String?,
    category: String? = null
  ) {
    val editing = _uiState.value.editingGoal
    if (editing != null) {
      repository.updateGoal(
        goalId = editing.id,
        title = title,
        description = description,
        iconId = iconId,
        priority = priority,
        dueDate = dueDate
      )
      _uiState.update { it.copy(notificationMessage = "تم تحديث الهدف بنجاح 🎯") }
    } else {
      repository.addGoal(
        title = title,
        description = description,
        iconId = iconId,
        priority = priority,
        dueDate = dueDate,
        category = category ?: "أهداف شخصية"
      )
      _uiState.update { it.copy(notificationMessage = "تم إنشاء الهدف الجديد بنجاح 🚀") }
    }
    closeCreateGoalSheet()
  }

  fun toggleGoalPause(goalId: String) {
    repository.toggleGoalPause(goalId)
    val goal = _uiState.value.goals.find { it.id == goalId }
    val wasPaused = goal?.isPaused ?: false
    val message = if (!wasPaused) {
      "تم ركن الهدف مؤقتاً. تاريخك وإنجازاتك محفوظة دائماً 📦"
    } else {
      "تم استئناف الهدف بنجاح! عودة موفقة 🎯"
    }
    _uiState.update { it.copy(notificationMessage = message) }
  }

  fun deleteGoal(goalId: String) {
    repository.deleteGoal(goalId)
    _uiState.update {
      it.copy(
        selectedGoalId = if (it.selectedGoalId == goalId) null else it.selectedGoalId,
        notificationMessage = "تم حذف الهدف"
      )
    }
  }

  fun openAddGoalTaskDialog() {
    _uiState.update { it.copy(isAddGoalTaskDialogVisible = true, editingGoalTask = null) }
  }

  fun openEditGoalTask(task: Task) {
    _uiState.update { it.copy(isAddGoalTaskDialogVisible = true, editingGoalTask = task) }
  }

  fun closeAddGoalTaskDialog() {
    _uiState.update { it.copy(isAddGoalTaskDialogVisible = false, editingGoalTask = null) }
  }

  fun saveGoalTask(
    goalId: String,
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) {
    val editing = _uiState.value.editingGoalTask
    if (editing != null) {
      repository.updateGoalTask(
        goalId = goalId,
        taskId = editing.id,
        title = title,
        notes = notes,
        priority = priority,
        schedule = schedule,
        dueDateFormatted = dueDateFormatted
      )
      _uiState.update { it.copy(notificationMessage = "تم تعديل المهمة بنجاح ✅") }
    } else {
      repository.addGoalTask(
        goalId = goalId,
        title = title,
        notes = notes,
        priority = priority,
        schedule = schedule,
        dueDateFormatted = dueDateFormatted
      )
      _uiState.update { it.copy(notificationMessage = "تمت إضافة المهمة للهدف بنجاح ✨") }
    }
    closeAddGoalTaskDialog()
  }

  fun deleteGoalTask(goalId: String, taskId: String) {
    repository.deleteGoalTask(goalId, taskId)
    _uiState.update { it.copy(notificationMessage = "تم حذف المهمة") }
  }

  fun onToggleRestMode() {
    repository.toggleRestMode()
    val isRest = repository.user.value.isRestModeActive
    val message = if (isRest) {
      "تم تفعيل وضع الراحة لهذا اليوم ✨ استرح بدون قلق، لن تتأثر سلاسل إنجازاتك."
    } else {
      "تم إيقاف وضع الراحة، عودة موفقة لروتينك اليومي!"
    }
    _uiState.update { it.copy(notificationMessage = message) }
  }

  fun dismissNotification() {
    _uiState.update { it.copy(notificationMessage = null) }
  }

  fun openEditNameDialog() {
    _uiState.update { it.copy(isEditNameDialogVisible = true) }
  }

  fun closeEditNameDialog() {
    _uiState.update { it.copy(isEditNameDialogVisible = false) }
  }

  fun saveUserName(newName: String) {
    repository.updateUserName(newName)
    closeEditNameDialog()
  }

  fun openQuickAddSheet() {
    _uiState.update { it.copy(isQuickAddSheetVisible = true) }
  }

  fun closeQuickAddSheet() {
    _uiState.update { it.copy(isQuickAddSheetVisible = false) }
  }

  fun onQuickAddOptionSelected(type: QuickAddType) {
    _uiState.update { it.copy(isQuickAddSheetVisible = false) }
    when (type) {
      QuickAddType.GOAL -> openCreateGoalSheet()
      QuickAddType.HABIT -> openCreateHabitSheet()
      else -> _uiState.update { it.copy(activeCreationDialog = type) }
    }
  }

  fun closeCreationDialog() {
    _uiState.update { it.copy(activeCreationDialog = null) }
  }

  fun setHabitFilter(filter: HabitFilter) {
    _uiState.update { it.copy(activeHabitFilter = filter) }
  }

  fun openCreateHabitSheet() {
    _uiState.update { it.copy(isCreateHabitSheetVisible = true, editingHabit = null) }
  }

  fun openEditHabit(habit: Habit) {
    _uiState.update { it.copy(isCreateHabitSheetVisible = true, editingHabit = habit) }
  }

  fun closeCreateHabitSheet() {
    _uiState.update { it.copy(isCreateHabitSheetVisible = false, editingHabit = null) }
  }

  fun saveHabit(
    title: String,
    type: HabitType,
    targetValue: Int,
    unit: String,
    frequency: HabitFrequency,
    scheduledDays: List<DayOfWeekArabic>,
    isMandatory: Boolean,
    priority: Priority,
    iconEmoji: String
  ) {
    val currentEditing = _uiState.value.editingHabit
    if (currentEditing != null) {
      repository.updateHabit(
        habitId = currentEditing.id,
        title = title,
        type = type,
        targetValue = targetValue,
        unit = unit,
        frequency = frequency,
        scheduledDays = scheduledDays,
        isMandatory = isMandatory,
        priority = priority,
        iconEmoji = iconEmoji
      )
      _uiState.update { it.copy(notificationMessage = "تم تعديل العادة بنجاح ✨") }
    } else {
      repository.addHabit(
        title = title,
        type = type,
        targetValue = targetValue,
        unit = unit,
        frequency = frequency,
        scheduledDays = scheduledDays,
        isMandatory = isMandatory,
        priority = priority,
        iconEmoji = iconEmoji
      )
      _uiState.update { it.copy(notificationMessage = "تمت إضافة العادة بنجاح 🌱") }
    }
    closeCreateHabitSheet()
  }

  fun deleteHabit(habitId: String) {
    repository.deleteHabit(habitId)
    _uiState.update { it.copy(notificationMessage = "تم حذف العادة بنجاح 🗑️") }
  }

  fun toggleHabit(habitId: String) {
    repository.toggleHabit(habitId)
  }

  fun openLogHabitDialog(habit: com.example.model.Habit) {
    _uiState.update { it.copy(selectedHabitToLog = habit) }
  }

  fun closeLogHabitDialog() {
    _uiState.update { it.copy(selectedHabitToLog = null) }
  }

  fun incrementHabit(habitId: String) {
    repository.incrementHabit(habitId)
  }

  fun updateHabitProgress(habitId: String, newTotalValue: Int) {
    repository.updateHabitProgress(habitId, newTotalValue)
    closeLogHabitDialog()
  }

  fun toggleHabitBoolean(habitId: String) {
    repository.toggleHabitBoolean(habitId)
  }

  fun toggleGeneralTask(taskId: String) {
    repository.toggleGeneralTask(taskId)
  }

  fun toggleGoalTask(goalId: String, taskId: String) {
    repository.toggleGoalTask(goalId, taskId)
  }

  fun addHabit(title: String, priority: Priority) {
    repository.addHabit(title = title, priority = priority)
    closeCreationDialog()
    _uiState.update { it.copy(notificationMessage = "تمت إضافة العادة بنجاح 🌱") }
  }

  fun addTask(title: String, priority: Priority) {
    repository.addTask(title, priority)
    closeCreationDialog()
    _uiState.update { it.copy(notificationMessage = "تمت إضافة المهمة بنجاح ✅") }
  }
}
