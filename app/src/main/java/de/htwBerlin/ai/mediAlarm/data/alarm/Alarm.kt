package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    val medicineId: Long,
    val targetTime: Long,
    var isExpired: Boolean = false
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}

