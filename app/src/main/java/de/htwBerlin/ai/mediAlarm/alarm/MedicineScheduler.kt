package de.htwBerlin.ai.mediAlarm.alarm

import android.content.Context
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import java.util.*

class MedicineScheduler(private val context: Context) {

    private val alarmDao = AppDatabase.getDatabase(context).alarmDao()

    fun schedule(medicine: Medicine) {
        if (alarmDao.getByMedicineId(medicine.id).isNotEmpty()) {
            return
        }

        val targetTime = TargetTimeCalculator(context).calculate(medicine)

        val alarm = Alarm(medicine.id, targetTime)
        alarm.id = alarmDao.insert(alarm)

        AlarmScheduler(context).schedule(alarm)
    }
}