package de.htwBerlin.ai.mediAlarm.data.rhythm

import com.google.gson.annotations.SerializedName

enum class TimepointType {
    @SerializedName("0")
    AbsoluteTime,
    @SerializedName("1")
    Morning,
    @SerializedName("2")
    Midday,
    @SerializedName("3")
    Evening,
    @SerializedName("4")
    Night
}
