package de.htwBerlin.ai.mediAlarm.data.medicine

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Medicine(
    val name: String,
    val amount: String,
    var rhythm: String?
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}