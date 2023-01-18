package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    val medicineId: Long,
    val targetTimeUtc: Long,
    var isExpired: Boolean = false
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    var actualTimeUtc: Long = 0
}

