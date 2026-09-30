package com.example.model

enum class TaskSchedule(val titleArabic: String) {
  TODAY("اليوم"),
  TOMORROW("غداً"),
  FUTURE("قريباً"),
  NO_DATE("بدون موعد")
}

data class Task(
  val id: String,
  val title: String,
  val notes: String? = null,
  val priority: Priority = Priority.MEDIUM,
  val isCompleted: Boolean = false,
  val goalId: String? = null,
  val isGoalTask: Boolean = false,
  val category: String? = null,
  val schedule: TaskSchedule = TaskSchedule.TODAY,
  val dueDateFormatted: String? = null,
  val createdAt: Long = System.currentTimeMillis(),
  val completedAt: Long? = null
)
