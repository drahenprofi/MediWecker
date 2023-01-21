package de.htwBerlin.ai.mediAlarm.data.reminderPrompt

data class ReminderPromptRequest(
    val medicineId: Long,
    val alarmId: Long,
    val scheduledTimeUtc: Long
)