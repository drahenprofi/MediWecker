package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    val time: String,
    val medicineId: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

