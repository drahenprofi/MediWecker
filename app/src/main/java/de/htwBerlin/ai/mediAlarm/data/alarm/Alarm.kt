package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimepointType
import java.util.UUID

@Entity
data class Alarm(
    val medicineId: Long,
    val timePointUUID: UUID,
    val targetTimeUtc: Long,
    var isExpired: Boolean = false,
    var userResponded: Boolean = false
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    var actualTimeUtc: Long = 0
}
