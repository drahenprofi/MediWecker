package de.htwBerlin.ai.mediAlarm.data.medicine

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Medicine(
    @PrimaryKey val id: Int,
    val name: String,
    val amount: Float,
    val rhythm: String?
)