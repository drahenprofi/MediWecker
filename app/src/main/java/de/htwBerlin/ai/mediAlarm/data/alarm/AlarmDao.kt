package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

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

    @Query("SELECT * FROM alarm WHERE targetTimeUtc >= :from AND targetTimeUtc <= :to AND isExpired")
    fun getExpiredAlarmsByTimeFrame(from: Long, to: Long): List<Alarm>

    @Query("SELECT * FROM alarm WHERE medicineId = :medicineId AND isExpired ORDER BY targetTimeUtc DESC LIMIT 1")
    fun getMostRecentExpiredAlarmByMedicineId(medicineId: Long): Alarm?

    @Query("SELECT * FROM alarm WHERE timePointUUID = :uuid AND isExpired ORDER BY targetTimeUtc DESC LIMIT 3")
    fun getMostRecentAlarmsByTimePointUUID(uuid: UUID): List<Alarm>
}