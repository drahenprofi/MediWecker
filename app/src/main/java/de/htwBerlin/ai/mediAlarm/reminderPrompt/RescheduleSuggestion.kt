package de.htwBerlin.ai.mediAlarm.reminderPrompt

import java.util.UUID

data class RescheduleSuggestion(
    val medicineId: Long,
    val alarmId: Long,
    val type: RescheduleSuggestionType,
    val suggestedTimeFromMidnight: Long
)
