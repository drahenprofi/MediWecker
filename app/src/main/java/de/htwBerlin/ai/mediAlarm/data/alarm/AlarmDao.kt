package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Query("SELECT * FROM alarm WHERE id = :id")
    fun get(id: Long): Alarm

    @Query("SELECT * FROM alarm WHERE medicineId = :medicineId")
    fun getByMedicineId(medicineId: Long): List<Alarm>

    @Insert
    fun insertAll(vararg alarms: Alarm)

    @Insert
    fun insert(alarm: Alarm): Long

    @Delete
    fun delete(alarm: Alarm)
}