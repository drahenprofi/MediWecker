package de.htwBerlin.ai.mediAlarm.reminderPrompt.data

import com.google.gson.annotations.SerializedName

data class ReminderPromptResponse (
    @SerializedName("MedicineId")
    val medicineId: Long,
    @SerializedName("AlarmId")
    val alarmId: Long,
    @SerializedName("ScheduledTimeUtc")
    val scheduledTimeUtc: Long,
    @SerializedName("ActualTimeUtc")
    val actualTimeUtc: Long
)