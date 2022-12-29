package de.htwBerlin.ai.mediAlarm.data.wakeUpTime

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class WakeUpTime(
    @SerializedName("Monday")
    val monday: Int,
    @SerializedName("Tuesday")
    val tuesday: Int,
    @SerializedName("Wednesday")
    val wednesday: Int,
    @SerializedName("Thursday")
    val thursday: Int,
    @SerializedName("Friday")
    val friday: Int,
    @SerializedName("Saturday")
    val saturday: Int,
    @SerializedName("Sunday")
    val sunday: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}