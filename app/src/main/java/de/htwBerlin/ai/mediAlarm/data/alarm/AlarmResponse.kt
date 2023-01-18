package de.htwBerlin.ai.mediAlarm.data.alarm

import com.google.gson.annotations.SerializedName

data class AlarmResponse (
    @SerializedName("MedicineId")
    val medicineId: Long,
    @SerializedName("ScheduledTimeUtc")
    val scheduledTimeUtc: Long,
    @SerializedName("ActualTimeUtc")
    val actualTimeUtc: Long
)