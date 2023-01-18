package de.htwBerlin.ai.mediAlarm.data.calendar

import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine

data class CalendarItem(
    val medicine: Medicine,
    val scheduledTimeUtc: Long
)
