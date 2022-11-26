package de.htwBerlin.ai.mediAlarm.data.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Delete
    fun delete(alarm: Alarm)
}