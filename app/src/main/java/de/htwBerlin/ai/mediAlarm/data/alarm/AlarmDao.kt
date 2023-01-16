package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Query("SELECT * FROM alarm WHERE id = :id")
    fun get(id: Long): Alarm?

    @Query("SELECT * FROM alarm WHERE medicineId = :medicineId")
    fun getByMedicineId(medicineId: Long): List<Alarm>

    @Insert
    fun insertAll(vararg alarms: Alarm)

    @Insert
    fun insert(alarm: Alarm): Long

    @Delete
    fun delete(alarm: Alarm)

    @Update
    fun update(alarm: Alarm)

    @Query("SELECT * FROM alarm WHERE targetTime >= :from AND targetTime <= :to AND isExpired")
    fun getExpiredAlarmsByTimeFrame(from: Long, to: Long): List<Alarm>

    @Query("SELECT * FROM alarm WHERE medicineId = :medicineId AND isExpired ORDER BY targetTime DESC LIMIT 1")
    fun getMostRecentExpiredAlarmByMedicineId(medicineId: Long): Alarm?
}