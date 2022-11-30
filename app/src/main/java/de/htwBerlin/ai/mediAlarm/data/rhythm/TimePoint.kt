package de.htwBerlin.ai.mediAlarm.data.rhythm

data class TimePoint(
    val type: TimepointType,
    val absoluteTimeFromMidnight: Long?
)