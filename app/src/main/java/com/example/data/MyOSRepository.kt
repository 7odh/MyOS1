package com.example.data

import com.example.model.DailyAnalytics
import com.example.model.DayOfWeekArabic
import com.example.model.Goal
import com.example.model.Habit
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Priority
import com.example.model.Task
import com.example.model.TaskSchedule
import com.example.model.User
import com.example.model.getCurrentDayOfWeekArabic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class MyOSRepository {

  private val _user = MutableStateFlow(
    User(
      name = "أحمد",
      motivationalSentence = "كل يوم فرصة جديدة لتكون أفضل من أمس.",
      isMotivationEnabled = true,
      isRestModeActive = false
    )
  )
  val user: StateFlow<User> = _user.asStateFlow()

  private val _habits = MutableStateFlow(
    listOf(
      Habit(
        id = "h1",
        title = "الصلوات الخمس",
        type = HabitType.COUNTER,
        targetValue = 5,
        currentValue = 3,
        unit = "صلوات",
        frequency = HabitFrequency.DAILY,
        isMandatory = true, // إجبارية لا تتأثر بوضع الراحة
        iconEmoji = "🕌",
        priority = Priority.HIGH,
        currentStreak = 24
      ),
      Habit(
        id = "h2",
        title = "الذهاب للجيم (تمارين رياضية)",
        type = HabitType.DURATION,
        targetValue = 60,
        currentValue = 0,
        unit = "دقيقة",
        frequency = HabitFrequency.SPECIFIC_DAYS,
        scheduledDays = listOf(DayOfWeekArabic.SATURDAY, DayOfWeekArabic.MONDAY, DayOfWeekArabic.THURSDAY),
        isMandatory = false, // تتأثر بوضع الراحة
        iconEmoji = "🏋️‍♂️",
        priority = Priority.HIGH,
        currentStreak = 8
      ),
      Habit(
        id = "h3",
        title = "الورد القرآني",
        type = HabitType.QUANTITY,
        targetValue = 10,
        currentValue = 6,
        unit = "صفحة",
        frequency = HabitFrequency.DAILY,
        isMandatory = true,
        iconEmoji = "📖",
        priority = Priority.HIGH,
        currentStreak = 15
      ),
      Habit(
        id = "h4",
        title = "التدرب على الكيبورد",
        type = HabitType.DURATION,
        targetValue = 30,
        currentValue = 20,
        unit = "دقيقة",
        frequency = HabitFrequency.DAILY,
        isMandatory = false,
        iconEmoji = "⌨️",
        priority = Priority.MEDIUM,
        currentStreak = 9
      ),
      Habit(
        id = "h5",
        title = "تمارين الضغط",
        type = HabitType.QUANTITY,
        targetValue = 50,
        currentValue = 30,
        unit = "عدة",
        frequency = HabitFrequency.DAILY,
        isMandatory = false,
        iconEmoji = "💪",
        priority = Priority.MEDIUM,
        currentStreak = 11
      ),
      Habit(
        id = "h6",
        title = "شرب الماء",
        type = HabitType.COUNTER,
        targetValue = 8,
        currentValue = 5,
        unit = "أكواب",
        frequency = HabitFrequency.DAILY,
        isMandatory = true,
        iconEmoji = "💧",
        priority = Priority.HIGH,
        currentStreak = 18
      ),
      Habit(
        id = "h7",
        title = "أذكار الصباح والمساء",
        type = HabitType.BOOLEAN,
        targetValue = 1,
        currentValue = 1,
        unit = "مرة",
        frequency = HabitFrequency.DAILY,
        isMandatory = true,
        iconEmoji = "✨",
        priority = Priority.HIGH,
        currentStreak = 25
      )
    )
  )
  val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

  private val _generalTasks = MutableStateFlow(
    listOf(
      Task(
        id = "t1",
        title = "إنهاء تصميم الصفحة الرئيسية - MyOS",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t2",
        title = "مراجعة الكود الخاص بالمشروع",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t3",
        title = "تجهيز تقرير العمل",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t4",
        title = "إرسال التحديثات للفريق",
        priority = Priority.MEDIUM,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t5",
        title = "مراجعة البريد الإلكتروني الهام",
        priority = Priority.LOW,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t6",
        title = "تنظيم ملفات الأسبوع",
        priority = Priority.LOW,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t7",
        title = "إعداد خطة الأسبوع القادم",
        priority = Priority.MEDIUM,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t8",
        title = "تحديث ملاحظات الاجتماع",
        priority = Priority.LOW,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t9",
        title = "نسخ احتياطي للبيانات",
        priority = Priority.LOW,
        isCompleted = true,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      ),
      Task(
        id = "t10",
        title = "تحديد أولويات الغد",
        priority = Priority.MEDIUM,
        isCompleted = false,
        isGoalTask = false,
        schedule = TaskSchedule.TODAY
      )
    )
  )
  val generalTasks: StateFlow<List<Task>> = _generalTasks.asStateFlow()

  private val _goals = MutableStateFlow(
    listOf(
      Goal(
        id = "g1",
        title = "إتقان اللغة الإنجليزية",
        description = "تعلم اللغة الإنجليزية وفتح آفاق جديدة للمستقبل",
        category = "مهارات ولغات",
        iconId = "book",
        iconEmoji = "📚",
        priority = Priority.MEDIUM,
        startDate = "2025/09/20",
        dueDate = "2025/12/31",
        tasks = listOf(
          Task(
            id = "gt1_1",
            title = "مراجعة القواعد الأساسية",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g1",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/22",
            createdAt = 1000L
          ),
          Task(
            id = "gt1_2",
            title = "حفظ 20 كلمة جديدة",
            priority = Priority.MEDIUM,
            isCompleted = true,
            goalId = "g1",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/25",
            createdAt = 2000L
          ),
          Task(
            id = "gt1_3",
            title = "ممارسة الاستماع",
            priority = Priority.LOW,
            isCompleted = true,
            goalId = "g1",
            isGoalTask = true,
            schedule = TaskSchedule.NO_DATE,
            dueDateFormatted = null,
            createdAt = 3000L
          ),
          Task(
            id = "gt1_4",
            title = "ممارسة التحدث",
            priority = Priority.LOW,
            isCompleted = false,
            goalId = "g1",
            isGoalTask = true,
            schedule = TaskSchedule.NO_DATE,
            dueDateFormatted = null,
            createdAt = 4000L
          )
        )
      ),
      Goal(
        id = "g2",
        title = "تطوير المهارات البرمجية",
        description = "تعلم تقنيات تطوير التطبيقات الحديثة وبناء مشاريع حقيقية",
        category = "مشاريع وتطوير",
        iconId = "code",
        iconEmoji = "</>",
        priority = Priority.HIGH,
        startDate = "2025/09/15",
        dueDate = "2025/11/30",
        tasks = listOf(
          Task(
            id = "gt2_1",
            title = "تصميم واجهة لوحة التحكم",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 100L
          ),
          Task(
            id = "gt2_2",
            title = "بناء نظام الأهداف وتفاصيلها",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 200L
          ),
          Task(
            id = "gt2_3",
            title = "إجراء اختبارات الأداء",
            priority = Priority.MEDIUM,
            isCompleted = false,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.FUTURE,
            dueDateFormatted = "2025/10/05",
            createdAt = 300L
          ),
          Task(
            id = "gt2_4",
            title = "توثيق واجهات API",
            priority = Priority.LOW,
            isCompleted = false,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.FUTURE,
            dueDateFormatted = "2025/10/10",
            createdAt = 400L
          ),
          Task(
            id = "gt2_5",
            title = "نشر النسخة التجريبية",
            priority = Priority.HIGH,
            isCompleted = false,
            goalId = "g2",
            isGoalTask = true,
            schedule = TaskSchedule.FUTURE,
            dueDateFormatted = "2025/10/20",
            createdAt = 500L
          )
        )
      ),
      Goal(
        id = "g3",
        title = "تحسين اللياقة البدنية",
        description = "الالتزام بنمط حياة صحي وممارسة الرياضة يومياً",
        category = "صحة ورياضة",
        iconId = "fitness",
        iconEmoji = "🏋️",
        priority = Priority.HIGH,
        startDate = "2025/09/01",
        dueDate = "2025/10/01",
        tasks = listOf(
          Task(
            id = "gt3_1",
            title = "تمارين الإحماء اليومية",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 10L
          ),
          Task(
            id = "gt3_2",
            title = "تمرين الجري 30 دقيقة",
            priority = Priority.MEDIUM,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 20L
          ),
          Task(
            id = "gt3_3",
            title = "شرب 3 لتر ماء",
            priority = Priority.HIGH,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/29",
            createdAt = 30L
          ),
          Task(
            id = "gt3_4",
            title = "تمارين القوة المنزلية",
            priority = Priority.MEDIUM,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/28",
            createdAt = 40L
          ),
          Task(
            id = "gt3_5",
            title = "جلسة استطالة ومساج",
            priority = Priority.LOW,
            isCompleted = true,
            goalId = "g3",
            isGoalTask = true,
            schedule = TaskSchedule.TODAY,
            dueDateFormatted = "2025/09/28",
            createdAt = 50L
          )
        )
      )
    )
  )
  val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

  fun updateUserName(newName: String) {
    _user.update { it.copy(name = newName.trim().ifEmpty { it.name }) }
  }

  fun toggleRestMode() {
    _user.update { it.copy(isRestModeActive = !it.isRestModeActive) }
  }

  fun toggleMotivation(enabled: Boolean) {
    _user.update { it.copy(isMotivationEnabled = enabled) }
  }

  fun updateMotivationalSentence(sentence: String) {
    _user.update { it.copy(motivationalSentence = sentence) }
  }

  fun incrementHabit(habitId: String) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          habit.copy(currentValue = habit.currentValue + 1)
        } else habit
      }
    }
  }

  fun updateHabitProgress(habitId: String, newTotalValue: Int) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          habit.copy(currentValue = newTotalValue.coerceAtLeast(0))
        } else habit
      }
    }
  }

  fun toggleHabitBoolean(habitId: String) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          val nextVal = if (habit.currentValue >= habit.targetValue) 0 else habit.targetValue
          habit.copy(currentValue = nextVal)
        } else habit
      }
    }
  }

  fun toggleHabit(habitId: String) {
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          when (habit.type) {
            HabitType.COUNTER -> habit.copy(currentValue = habit.currentValue + 1)
            HabitType.BOOLEAN -> {
              val nextVal = if (habit.currentValue >= habit.targetValue) 0 else habit.targetValue
              habit.copy(currentValue = nextVal)
            }
            HabitType.QUANTITY, HabitType.DURATION -> {
              habit.copy(currentValue = habit.currentValue + 1)
            }
          }
        } else habit
      }
    }
  }

  fun toggleGeneralTask(taskId: String) {
    _generalTasks.update { list ->
      list.map { task ->
        if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
      }
    }
  }

  fun toggleGoalTask(goalId: String, taskId: String) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          val updatedTasks = goal.tasks.map { task ->
            if (task.id == taskId) {
              val newCompleted = !task.isCompleted
              task.copy(
                isCompleted = newCompleted,
                completedAt = if (newCompleted) System.currentTimeMillis() else null
              )
            } else {
              task
            }
          }
          goal.copy(tasks = updatedTasks)
        } else {
          goal
        }
      }
    }
  }

  fun toggleGoalPause(goalId: String) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) goal.copy(isPaused = !goal.isPaused) else goal
      }
    }
  }

  fun addGoal(
    title: String,
    description: String? = null,
    iconId: String = "target",
    priority: Priority = Priority.HIGH,
    dueDate: String? = null,
    category: String? = null
  ) {
    val newGoal = Goal(
      id = UUID.randomUUID().toString(),
      title = title,
      description = description?.trim()?.ifEmpty { null },
      category = category ?: "أهداف شخصية",
      iconId = iconId,
      priority = priority,
      dueDate = dueDate?.trim()?.ifEmpty { null },
      startDate = "2025/09/29",
      tasks = emptyList()
    )
    _goals.update { listOf(newGoal) + it }
  }

  fun updateGoal(
    goalId: String,
    title: String,
    description: String?,
    iconId: String,
    priority: Priority,
    dueDate: String?
  ) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          goal.copy(
            title = title,
            description = description?.trim()?.ifEmpty { null },
            iconId = iconId,
            priority = priority,
            dueDate = dueDate?.trim()?.ifEmpty { null }
          )
        } else {
          goal
        }
      }
    }
  }

  fun deleteGoal(goalId: String) {
    _goals.update { list -> list.filterNot { it.id == goalId } }
  }

  fun addGoalTask(
    goalId: String,
    title: String,
    notes: String? = null,
    priority: Priority = Priority.MEDIUM,
    schedule: TaskSchedule = TaskSchedule.TODAY,
    dueDateFormatted: String? = null
  ) {
    val newTask = Task(
      id = UUID.randomUUID().toString(),
      title = title,
      notes = notes?.trim()?.ifEmpty { null },
      priority = priority,
      isCompleted = false,
      goalId = goalId,
      isGoalTask = true,
      schedule = schedule,
      dueDateFormatted = dueDateFormatted?.trim()?.ifEmpty { null },
      createdAt = System.currentTimeMillis()
    )
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          goal.copy(tasks = goal.tasks + newTask)
        } else {
          goal
        }
      }
    }
  }

  fun updateGoalTask(
    goalId: String,
    taskId: String,
    title: String,
    notes: String?,
    priority: Priority,
    schedule: TaskSchedule,
    dueDateFormatted: String?
  ) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          val updatedTasks = goal.tasks.map { task ->
            if (task.id == taskId) {
              task.copy(
                title = title,
                notes = notes?.trim()?.ifEmpty { null },
                priority = priority,
                schedule = schedule,
                dueDateFormatted = dueDateFormatted?.trim()?.ifEmpty { null }
              )
            } else {
              task
            }
          }
          goal.copy(tasks = updatedTasks)
        } else {
          goal
        }
      }
    }
  }

  fun deleteGoalTask(goalId: String, taskId: String) {
    _goals.update { list ->
      list.map { goal ->
        if (goal.id == goalId) {
          goal.copy(tasks = goal.tasks.filterNot { it.id == taskId })
        } else {
          goal
        }
      }
    }
  }

  fun addHabit(
    title: String,
    type: HabitType = HabitType.BOOLEAN,
    targetValue: Int = 1,
    unit: String = "مرة",
    frequency: HabitFrequency = HabitFrequency.DAILY,
    scheduledDays: List<DayOfWeekArabic> = emptyList(),
    isMandatory: Boolean = false,
    priority: Priority = Priority.HIGH,
    iconEmoji: String = "🌱"
  ) {
    val newHabit = Habit(
      id = UUID.randomUUID().toString(),
      title = title.trim(),
      type = type,
      targetValue = targetValue.coerceAtLeast(1),
      currentValue = 0,
      unit = unit.trim().ifEmpty { "مرة" },
      frequency = frequency,
      scheduledDays = if (frequency == HabitFrequency.SPECIFIC_DAYS) scheduledDays else emptyList(),
      isMandatory = isMandatory,
      priority = priority,
      currentStreak = 0,
      iconEmoji = iconEmoji
    )
    _habits.update { listOf(newHabit) + it }
  }

  fun updateHabit(
    habitId: String,
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
    _habits.update { list ->
      list.map { habit ->
        if (habit.id == habitId) {
          habit.copy(
            title = title.trim(),
            type = type,
            targetValue = targetValue.coerceAtLeast(1),
            unit = unit.trim().ifEmpty { "مرة" },
            frequency = frequency,
            scheduledDays = if (frequency == HabitFrequency.SPECIFIC_DAYS) scheduledDays else emptyList(),
            isMandatory = isMandatory,
            priority = priority,
            iconEmoji = iconEmoji
          )
        } else habit
      }
    }
  }

  fun deleteHabit(habitId: String) {
    _habits.update { list -> list.filterNot { it.id == habitId } }
  }

  fun addTask(title: String, priority: Priority = Priority.MEDIUM) {
    val newTask = Task(
      id = UUID.randomUUID().toString(),
      title = title,
      priority = priority,
      isCompleted = false,
      isGoalTask = false,
      schedule = TaskSchedule.TODAY
    )
    _generalTasks.update { listOf(newTask) + it }
  }

  fun calculateAnalytics(): DailyAnalytics {
    val habitList = _habits.value
    val taskList = _generalTasks.value
    val activeGoalList = _goals.value.filter { !it.isPaused }

    // Today's goals progress is calculated from actual today's scheduled goal tasks!
    val todayGoalTasks = activeGoalList.flatMap { it.todayTasks }
    val totalTodayGoalTasks = todayGoalTasks.size
    val completedTodayGoalTasks = todayGoalTasks.count { it.isCompleted }

    val todayDay = getCurrentDayOfWeekArabic()
    val todayHabits = if (_user.value.isRestModeActive) {
      habitList.filter { it.isScheduledForToday(todayDay) && it.isMandatory }
    } else {
      habitList.filter { it.isScheduledForToday(todayDay) }
    }

    return DailyAnalytics(
      habitsCompleted = todayHabits.count { it.isCompleted },
      habitsTotal = todayHabits.size,
      tasksCompleted = taskList.count { it.isCompleted },
      tasksTotal = taskList.size,
      goalsCompleted = completedTodayGoalTasks,
      goalsTotal = totalTodayGoalTasks
    )
  }
}
