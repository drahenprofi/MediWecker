package de.htwBerlin.ai.mediAlarm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.alarm.AlarmDao
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao

@Database(entities = [Medicine::class, Alarm::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun alarmDao(): AlarmDao
}