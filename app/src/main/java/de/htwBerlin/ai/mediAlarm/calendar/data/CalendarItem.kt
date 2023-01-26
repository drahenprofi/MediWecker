package de.htwBerlin.ai.mediAlarm.calendar.data

import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine

data class CalendarItem(
    val medicine: Medicine,
    val scheduledTimeUtc: Long,
    val actualTimeUtc: Long,
    val userResponded: Boolean = false
)
