package de.htwBerlin.ai.mediAlarm.reminderPrompt.data

data class ReminderPromptRequest(
    val medicineId: Long,
    val alarmId: Long,
    val scheduledTimeUtc: Long
)