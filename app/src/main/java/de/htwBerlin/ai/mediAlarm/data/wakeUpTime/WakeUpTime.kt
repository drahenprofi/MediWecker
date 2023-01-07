package de.htwBerlin.ai.mediAlarm.data.wakeUpTime

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class WakeUpTime(
    @SerializedName("Monday")
    val monday: Long,
    @SerializedName("Tuesday")
    val tuesday: Long,
    @SerializedName("Wednesday")
    val wednesday: Long,
    @SerializedName("Thursday")
    val thursday: Long,
    @SerializedName("Friday")
    val friday: Long,
    @SerializedName("Saturday")
    val saturday: Long,
    @SerializedName("Sunday")
    val sunday: Long
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}