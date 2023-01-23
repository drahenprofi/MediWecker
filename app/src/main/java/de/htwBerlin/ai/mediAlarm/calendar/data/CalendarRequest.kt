package de.htwBerlin.ai.mediAlarm.calendar.data

import com.google.gson.annotations.SerializedName

data class CalendarRequest(
    @SerializedName("From")
    val from: Long,
    @SerializedName("To")
    val to: Long
)
