package de.htwBerlin.ai.mediAlarm.data.rhythm

data class Rhythm(
    val intervalDaysData: IntervalDaysData?,
    val specificDaysData: SpecificDaysData?,
    val timePoints: List<TimePoint>
)