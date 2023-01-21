package de.htwBerlin.ai.mediAlarm.data.calendar

import com.google.gson.annotations.SerializedName

data class CalendarRequest(
    @SerializedName("From")
    val from: Long,
    @SerializedName("To")
    val to: Long
)
