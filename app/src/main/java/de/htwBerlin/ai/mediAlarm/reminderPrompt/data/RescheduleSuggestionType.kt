package de.htwBerlin.ai.mediAlarm.reminderPrompt.data

import com.google.gson.annotations.SerializedName

enum class RescheduleSuggestionType {
    @SerializedName("0")
    Acknowledged,
    @SerializedName("1")
    RescheduleAbsoluteTime,
    @SerializedName("2")
    RescheduleWakeUpTime,
    @SerializedName("3")
    RescheduleSleepTime
}
