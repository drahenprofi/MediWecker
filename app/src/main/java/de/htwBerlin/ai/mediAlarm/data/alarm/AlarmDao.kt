package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Query("SELECT * FROM alarm WHERE medicineId = :medicineId")
    fun getByMedicineId(medicineId: Int): List<Alarm>

    @Insert
    fun insertAll(vararg alarms: Alarm)

    @Delete
    fun delete(alarm: Alarm)
}