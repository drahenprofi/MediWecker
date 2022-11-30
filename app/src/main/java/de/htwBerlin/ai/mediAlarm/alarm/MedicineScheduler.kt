package de.htwBerlin.ai.mediAlarm.alarm

import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import java.util.*

class MedicineScheduler(private val context: Context) {

    private val alarmDao = AppDatabase.getDatabase(context).alarmDao()
    private val gson = Gson()

    fun schedule(medicine: Medicine) {
        Log.d("SCHEDULING", "Scheduling medicine reminder ${medicine.name}")

        /*if (alarmDao.getByMedicineId(medicine.id).isNotEmpty()) {
            return
        }*/

        val targetTime = getNextTargetTime(medicine)

        Log.d("SCHEDULING", "Scheduling ${medicine.name} for $targetTime")

        val alarm = Alarm(medicine.id, targetTime)

        AlarmScheduler(context).schedule(alarm)

        alarmDao.insertAll(alarm)
    }

    private fun getNextTargetTime(medicine: Medicine): Long {
        val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)

        val now = SystemClock.elapsedRealtime()

        val calendar = Calendar.getInstance()

        val scheduledDayMillis = now - calendar[Calendar.HOUR_OF_DAY] * 60 * 60 * 1000 - calendar[Calendar.MINUTE] * 60 * 1000 - calendar[Calendar.SECOND] * 1000

        /*if (rhythm.intervalDaysData != null) {

        } else if (rhythm.specificDaysData != null) {
            val calendar: Calendar = Calendar.getInstance()
            val currentWeekday: Int = calendar.get(Calendar.DAY_OF_WEEK)

        }*/

        val currentTimeFromMidnight = (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * 60 * 1000

        for (timePoint in rhythm.timePoints) {
            if (timePoint.absoluteTimeFromMidnight!! >= currentTimeFromMidnight) {
                return scheduledDayMillis + timePoint.absoluteTimeFromMidnight
            }
        }

        return SystemClock.elapsedRealtime()
    }
}