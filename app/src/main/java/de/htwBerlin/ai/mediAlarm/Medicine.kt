package de.htwBerlin.ai.mediAlarm

import java.io.Serializable

data class Medicine(
    var id: Int,
    var name: String,
    var amount: Float,
    var rhythm: String?
) : Serializable