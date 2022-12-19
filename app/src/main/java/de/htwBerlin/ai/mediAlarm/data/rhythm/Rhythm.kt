package de.htwBerlin.ai.mediAlarm.data.rhythm

data class Rhythm(
    val intervalDays: IntervalDaysData?,
    val specificDays: SpecificDaysData?,
    val timePoints: List<TimePoint>
)