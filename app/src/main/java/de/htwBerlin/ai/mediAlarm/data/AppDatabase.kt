package de.htwBerlin.ai.mediAlarm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao

@Database(entities = [Medicine::class], version = 1)
public abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
}