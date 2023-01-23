package de.htwBerlin.ai.mediAlarm.reminderPrompt

data class RescheduleSuggestion(
    val medicineId: Long,
    val type: RescheduleSuggestionType,
    val suggestedTimeFromMidnight: Long
)
