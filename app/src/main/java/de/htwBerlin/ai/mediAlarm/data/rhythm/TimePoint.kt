package de.htwBerlin.ai.mediAlarm.data.rhythm

import java.util.UUID

data class TimePoint(
    val type: TimepointType,
    val absoluteTimeFromMidnight: Long?,
    var uuid: UUID
)