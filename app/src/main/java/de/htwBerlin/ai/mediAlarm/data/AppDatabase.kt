package de.htwBerlin.ai.mediAlarm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.alarm.AlarmDao
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao

@Database(entities = [Medicine::class, Alarm::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "medi-wecker-database"
                    ).build()
                }
            }

            return instance!!
        }
    }
}