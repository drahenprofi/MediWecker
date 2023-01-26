package de.htwBerlin.ai.mediAlarm.data.userTime

import com.google.gson.annotations.SerializedName

data class UserTime(
    @SerializedName("WakeupMonday")
    var wakeupMonday: Long,
    @SerializedName("WakeupTuesday")
    var wakeupTuesday: Long,
    @SerializedName("WakeupWednesday")
    var wakeupWednesday: Long,
    @SerializedName("WakeupThursday")
    var wakeupThursday: Long,
    @SerializedName("WakeupFriday")
    var wakeupFriday: Long,
    @SerializedName("WakeupSaturday")
    var wakeupSaturday: Long,
    @SerializedName("WakeupSunday")
    var wakeupSunday: Long,
    @SerializedName("SleepMonday")
    var sleepMonday: Long,
    @SerializedName("SleepTuesday")
    var sleepTuesday: Long,
    @SerializedName("SleepWednesday")
    var sleepWednesday: Long,
    @SerializedName("SleepThursday")
    var sleepThursday: Long,
    @SerializedName("SleepFriday")
    var sleepFriday: Long,
    @SerializedName("SleepSaturday")
    var sleepSaturday: Long,
    @SerializedName("SleepSunday")
    var sleepSunday: Long
)