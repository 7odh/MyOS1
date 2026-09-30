package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MyOSRepository
import com.example.model.DayOfWeekArabic
import com.example.model.GoalStatus
import com.example.model.HabitFrequency
import com.example.model.HabitType
import com.example.model.Priority
import com.example.model.TaskSchedule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("MyOS", appName)
  }

  @Test
  fun `test goal task date logic and priority ordering`() {
    val repository = MyOSRepository()
    val goal1 = repository.goals.value.first { it.id == "g1" }

    // Goal 1 has 4 total tasks, 3 completed -> 75%
    assertEquals(4, goal1.totalTasksCount)
    assertEquals(3, goal1.completedTasksCount)
    assertEquals(75, goal1.progressPercentage)

    // On Home screen: ONLY tasks scheduled for TODAY appear!
    val todayTasks = goal1.todayTasks
    assertEquals(2, todayTasks.size)
    assertTrue(todayTasks.all { it.schedule == TaskSchedule.TODAY })

    // In Goal Details: all 4 tasks appear in sorted order (High -> Medium -> Low)
    val sortedAllTasks = goal1.sortedTasks
    assertEquals(4, sortedAllTasks.size)
    assertEquals(Priority.HIGH, sortedAllTasks[0].priority)
    assertEquals(Priority.MEDIUM, sortedAllTasks[1].priority)
    assertEquals(Priority.LOW, sortedAllTasks[2].priority)
    assertEquals(Priority.LOW, sortedAllTasks[3].priority)
  }

  @Test
  fun `test goal completion status and exclusion from home screen`() {
    val repository = MyOSRepository()
    val goal3 = repository.goals.value.first { it.id == "g3" }

    // Goal 3 has 5 of 5 tasks completed -> 100%
    assertEquals(5, goal3.totalTasksCount)
    assertEquals(5, goal3.completedTasksCount)
    assertEquals(100, goal3.progressPercentage)
    assertEquals(GoalStatus.COMPLETED, goal3.status)

    // On the Home screen: Completed goals (100%) and Paused goals are excluded from active daily list!
    val homeActiveGoals = repository.goals.value.filter { !it.isPaused && it.progressPercentage < 100 }
    assertFalse(homeActiveGoals.any { it.id == "g3" })
    assertTrue(homeActiveGoals.any { it.id == "g1" }) // Goal 1 is 75%, still active
    assertTrue(homeActiveGoals.any { it.id == "g2" }) // Goal 2 is 40%, still active

    // Now complete the last remaining task of Goal 1 -> reaches 100%
    repository.toggleGoalTask("g1", "gt1_4")
    val goal1After = repository.goals.value.first { it.id == "g1" }
    assertEquals(100, goal1After.progressPercentage)

    // Goal 1 now disappears from Home screen active goals!
    val homeActiveGoalsAfter = repository.goals.value.filter { !it.isPaused && it.progressPercentage < 100 }
    assertFalse(homeActiveGoalsAfter.any { it.id == "g1" })
  }

  @Test
  fun `test delete goal removes goal and its tasks`() {
    val repository = MyOSRepository()
    assertTrue(repository.goals.value.any { it.id == "g1" })

    // Delete goal g1
    repository.deleteGoal("g1")
    assertFalse(repository.goals.value.any { it.id == "g1" })
  }

  @Test
  fun `test pause and resume goal preserves progress`() {
    val repository = MyOSRepository()
    val goal1Before = repository.goals.value.first { it.id == "g1" }
    assertEquals(75, goal1Before.progressPercentage)
    assertEquals(false, goal1Before.isPaused)
    assertEquals(GoalStatus.ACTIVE, goal1Before.status)

    // Pause goal (ركن الهدف)
    repository.toggleGoalPause("g1")
    val goal1Paused = repository.goals.value.first { it.id == "g1" }
    assertEquals(true, goal1Paused.isPaused)
    assertEquals(GoalStatus.PAUSED, goal1Paused.status)
    // Progress remains 75%
    assertEquals(75, goal1Paused.progressPercentage)
    assertEquals(3, goal1Paused.completedTasksCount)

    // Resume goal (استئناف الهدف)
    repository.toggleGoalPause("g1")
    val goal1Resumed = repository.goals.value.first { it.id == "g1" }
    assertEquals(false, goal1Resumed.isPaused)
    assertEquals(GoalStatus.ACTIVE, goal1Resumed.status)
    assertEquals(75, goal1Resumed.progressPercentage)
  }

  @Test
  fun `test rich habit tracking and custom frequency`() {
    val repository = MyOSRepository()

    // 1. Counter habit (الصلوات الخمس): 3 of 5, mandatory, daily
    val prayerHabit = repository.habits.value.first { it.id == "h1" }
    assertEquals(HabitType.COUNTER, prayerHabit.type)
    assertEquals(3, prayerHabit.currentValue)
    assertEquals(5, prayerHabit.targetValue)
    assertEquals("صلوات", prayerHabit.unit)
    assertTrue(prayerHabit.isMandatory)
    assertFalse(prayerHabit.isCompleted)

    // Increment counter twice
    repository.incrementHabit("h1")
    repository.incrementHabit("h1")
    val prayerUpdated = repository.habits.value.first { it.id == "h1" }
    assertEquals(5, prayerUpdated.currentValue)
    assertTrue(prayerUpdated.isCompleted)

    // 2. Specific days habit (الجيم: سبت، اثنين، خميس)
    val gymHabit = repository.habits.value.first { it.id == "h2" }
    assertEquals(HabitFrequency.SPECIFIC_DAYS, gymHabit.frequency)
    assertTrue(gymHabit.isScheduledForToday(DayOfWeekArabic.SATURDAY))
    assertTrue(gymHabit.isScheduledForToday(DayOfWeekArabic.MONDAY))
    assertTrue(gymHabit.isScheduledForToday(DayOfWeekArabic.THURSDAY))
    assertFalse(gymHabit.isScheduledForToday(DayOfWeekArabic.FRIDAY))
    assertFalse(gymHabit.isScheduledForToday(DayOfWeekArabic.SUNDAY))
    assertFalse(gymHabit.isMandatory)

    // 3. Quantity habit with custom unit (تمارين الضغط: عدات)
    val pushupsHabit = repository.habits.value.first { it.id == "h5" }
    assertEquals(HabitType.QUANTITY, pushupsHabit.type)
    assertEquals("عدة", pushupsHabit.unit)
    assertEquals(50, pushupsHabit.targetValue)
    assertEquals(30, pushupsHabit.currentValue)

    // 4. Boolean habit (أذكار الصباح والمساء)
    val azkarHabit = repository.habits.value.first { it.id == "h7" }
    assertEquals(HabitType.BOOLEAN, azkarHabit.type)
    assertTrue(azkarHabit.isCompleted)
    assertTrue(azkarHabit.isMandatory)

    repository.toggleHabitBoolean("h7")
    val azkarToggled = repository.habits.value.first { it.id == "h7" }
    assertFalse(azkarToggled.isCompleted)
  }

  @Test
  fun `test habit CRUD and custom unit creation`() {
    val repository = MyOSRepository()

    // Add new habit with custom unit
    repository.addHabit(
      title = "تمارين نط الحبل",
      type = HabitType.QUANTITY,
      targetValue = 200,
      unit = "نطة",
      frequency = HabitFrequency.DAILY,
      isMandatory = false,
      priority = Priority.MEDIUM,
      iconEmoji = "🪢"
    )

    val newHabit = repository.habits.value.first { it.title == "تمارين نط الحبل" }
    assertEquals("نطة", newHabit.unit)
    assertEquals(200, newHabit.targetValue)
    assertEquals(HabitType.QUANTITY, newHabit.type)

    // Update habit
    repository.updateHabit(
      habitId = newHabit.id,
      title = "تمارين نط الحبل المتقدمة",
      type = HabitType.QUANTITY,
      targetValue = 300,
      unit = "قفزة",
      frequency = HabitFrequency.DAILY,
      scheduledDays = emptyList(),
      isMandatory = true,
      priority = Priority.HIGH,
      iconEmoji = "🪢"
    )

    val updatedHabit = repository.habits.value.first { it.id == newHabit.id }
    assertEquals("تمارين نط الحبل المتقدمة", updatedHabit.title)
    assertEquals(300, updatedHabit.targetValue)
    assertEquals("قفزة", updatedHabit.unit)
    assertTrue(updatedHabit.isMandatory)

    // Delete habit
    repository.deleteHabit(newHabit.id)
    assertFalse(repository.habits.value.any { it.id == newHabit.id })
  }

  @Test
  fun `test rest mode affects non-mandatory habits`() {
    val repository = MyOSRepository()
    assertEquals(false, repository.user.value.isRestModeActive)

    val prayer = repository.habits.value.first { it.id == "h1" }
    val gym = repository.habits.value.first { it.id == "h2" }

    assertTrue(prayer.isMandatory) // Prayer is mandatory
    assertFalse(gym.isMandatory)   // Gym is non-mandatory

    // Toggle rest mode
    repository.toggleRestMode()
    assertTrue(repository.user.value.isRestModeActive)

    // In Rest Mode, analytics only expects mandatory habits for today
    val analytics = repository.calculateAnalytics()
    assertTrue(analytics.habitsTotal > 0)
    // Mandatory habits (prayer, Quran, water, Azkar) remain counted
    val mandatoryHabitsCount = repository.habits.value.count { it.isMandatory }
    assertEquals(mandatoryHabitsCount, analytics.habitsTotal)
  }

  @Test
  fun `test habit pause freeze feature with history preservation`() {
    val repository = MyOSRepository()
    val pushupsBefore = repository.habits.value.first { it.id == "h5" }
    assertEquals(11, pushupsBefore.currentStreak)
    assertEquals(30, pushupsBefore.currentValue)
    assertFalse(pushupsBefore.isCurrentlyPaused)

    // Pause pushups habit for 7 days (ركن العادة لمدة أسبوع)
    repository.pauseHabit("h5", durationDays = 7)
    val pushupsPaused = repository.habits.value.first { it.id == "h5" }

    // History and streak are fully preserved
    assertEquals(11, pushupsPaused.currentStreak)
    assertEquals(30, pushupsPaused.currentValue)
    assertTrue(pushupsPaused.isPaused)
    assertTrue(pushupsPaused.isCurrentlyPaused)
    assertEquals(7, pushupsPaused.pauseRemainingDays)
    assertTrue(pushupsPaused.pauseStatusDescription!!.contains("7 أيام") || pushupsPaused.pauseStatusDescription!!.contains("أيام"))

    // Paused habit must NEVER appear in today's schedule on Home screen
    val todayDay = com.example.model.getCurrentDayOfWeekArabic()
    assertFalse(pushupsPaused.isScheduledForToday(todayDay))

    // Resume the habit (استئناف العادة)
    repository.resumeHabit("h5")
    val pushupsResumed = repository.habits.value.first { it.id == "h5" }

    assertFalse(pushupsResumed.isPaused)
    assertFalse(pushupsResumed.isCurrentlyPaused)
    // Streak is still preserved
    assertEquals(11, pushupsResumed.currentStreak)
    assertEquals(30, pushupsResumed.currentValue)
    assertTrue(pushupsResumed.isScheduledForToday(todayDay))
  }

  @Test
  fun `test home screen only displays due today items and excludes no-date tasks`() {
    val repository = MyOSRepository()
    val allTasks = repository.generalTasks.value

    // Confirm we have tasks with TODAY, NO_DATE, and TOMORROW
    assertTrue(allTasks.any { it.schedule == TaskSchedule.TODAY })
    assertTrue(allTasks.any { it.schedule == TaskSchedule.NO_DATE })
    assertTrue(allTasks.any { it.schedule == TaskSchedule.TOMORROW })

    // On Home screen: ONLY general tasks with schedule == TODAY are displayed
    val homeTasks = allTasks.filter { !it.isGoalTask && it.schedule == TaskSchedule.TODAY }
    assertTrue(homeTasks.all { it.schedule == TaskSchedule.TODAY })
    assertFalse(homeTasks.any { it.schedule == TaskSchedule.NO_DATE })
    assertFalse(homeTasks.any { it.schedule == TaskSchedule.TOMORROW })

    // Analytics strictly counts only today's tasks
    val analytics = repository.calculateAnalytics()
    assertEquals(homeTasks.size, analytics.tasksTotal)
    assertEquals(homeTasks.count { it.isCompleted }, analytics.tasksCompleted)
  }

  @Test
  fun `test tasks screen priority ordering and without-date tasks`() {
    val repository = MyOSRepository()

    // Add a high priority task with no date
    repository.addGeneralTask(
      title = "فكرة مشروع جديدة",
      notes = "تجهيز المسودة",
      priority = Priority.HIGH,
      schedule = TaskSchedule.NO_DATE,
      dueDateFormatted = null
    )

    // Add a no-priority (NONE) task for today
    repository.addGeneralTask(
      title = "شراء خبز",
      notes = null,
      priority = Priority.NONE,
      schedule = TaskSchedule.TODAY,
      dueDateFormatted = null
    )

    val allTasks = repository.generalTasks.value
    val noDateTask = allTasks.first { it.title == "فكرة مشروع جديدة" }
    val nonePriorityTask = allTasks.first { it.title == "شراء خبز" }

    // 1. Task without date does NOT appear in Home screen (isDueToday is false)
    assertFalse(noDateTask.isDueToday)

    // 2. Task with TODAY schedule appears in Home screen
    assertTrue(nonePriorityTask.isDueToday)

    // 3. Sorting by priority orders HIGH (rank 1) before MEDIUM (rank 2) before LOW (rank 3) before NONE (rank 4)
    val sortedByPriority = allTasks.filter { !it.isCompleted }.sortedWith(
      compareBy<com.example.model.Task> { it.priority.rank }
    )
    val highIndex = sortedByPriority.indexOfFirst { it.priority == Priority.HIGH }
    val noneIndex = sortedByPriority.indexOfFirst { it.priority == Priority.NONE }
    assertTrue(highIndex < noneIndex)

    // 4. Update task to add a date
    repository.updateGeneralTask(
      taskId = noDateTask.id,
      title = "فكرة مشروع جديدة ومراجعة",
      notes = "تم تحديث المسودة",
      priority = Priority.HIGH,
      schedule = TaskSchedule.TODAY,
      dueDateFormatted = com.example.model.getTodayDateString()
    )

    val updatedTask = repository.generalTasks.value.first { it.id == noDateTask.id }
    assertEquals("فكرة مشروع جديدة ومراجعة", updatedTask.title)
    assertTrue(updatedTask.isDueToday)

    // 5. Delete task
    repository.deleteGeneralTask(updatedTask.id)
    assertFalse(repository.generalTasks.value.any { it.id == updatedTask.id })
  }
}
