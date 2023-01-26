package de.htwBerlin.ai.mediAlarm.reminderPrompt

import com.google.gson.annotations.SerializedName

data class RescheduleSuggestion(
    @SerializedName("MedicineId")
    val medicineId: Long,
    @SerializedName("AlarmId")
    val alarmId: Long,
    @SerializedName("Type")
    val type: RescheduleSuggestionType,
    @SerializedName("SuggestedTimeFromMidnight")
    val suggestedTimeFromMidnight: Long
)
