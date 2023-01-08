package de.htwBerlin.ai.mediAlarm.data.userTime

import com.google.gson.annotations.SerializedName

data class UserTime(
    @SerializedName("WakeupMonday")
    val wakeupMonday: Long,
    @SerializedName("WakeupTuesday")
    val wakeupTuesday: Long,
    @SerializedName("WakeupWednesday")
    val wakeupWednesday: Long,
    @SerializedName("WakeupThursday")
    val wakeupThursday: Long,
    @SerializedName("WakeupFriday")
    val wakeupFriday: Long,
    @SerializedName("WakeupSaturday")
    val wakeupSaturday: Long,
    @SerializedName("WakeupSunday")
    val wakeupSunday: Long,
    @SerializedName("SleepMonday")
    val sleepMonday: Long,
    @SerializedName("SleepTuesday")
    val sleepTuesday: Long,
    @SerializedName("SleepWednesday")
    val sleepWednesday: Long,
    @SerializedName("SleepThursday")
    val sleepThursday: Long,
    @SerializedName("SleepFriday")
    val sleepFriday: Long,
    @SerializedName("SleepSaturday")
    val sleepSaturday: Long,
    @SerializedName("SleepSunday")
    val sleepSunday: Long
)