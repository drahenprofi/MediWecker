package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Embedded
import androidx.room.Relation
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine

data class MedicineAlarm(
    @Embedded val medicine: Medicine,
    @Relation(
        parentColumn = "id",
        entityColumn = "medicineId"
    )
    val alarm: Alarm
)