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

        val targetTime = TargetTimeCalculator().calculate(medicine)

        val alarm = Alarm(medicine.id, targetTime)
        alarm.id = alarmDao.insert(alarm)

        AlarmScheduler(context).schedule(alarm)
    }

    /*private fun getNextTargetTime(medicine: Medicine): Long {
        val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)

        val calendar = Calendar.getInstance()

        val now = calendar.timeInMillis

        val scheduledDayMillis = now - calendar[Calendar.HOUR_OF_DAY] * 60 * 60 * 1000 - calendar[Calendar.MINUTE] * 60 * 1000 - calendar[Calendar.SECOND] * 1000

        // TODO: Schedule day (intervalDaysData/specificDaysData)
        /*if (rhythm.intervalDaysData != null) {

        } else if (rhythm.specificDaysData != null) {
            val calendar: Calendar = Calendar.getInstance()
            val currentWeekday: Int = calendar.get(Calendar.DAY_OF_WEEK)

        }*/

        val currentTimeFromMidnight = (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * 60 * 1000

        // TODO: this will crash for all timePointTypes except TimePointType.AbsoluteTime
        for (timePoint in rhythm.timePoints) {
            val scheduledTimeFromMidnight = timePoint.absoluteTimeFromMidnight!! * 60 * 1000
            if (scheduledTimeFromMidnight > currentTimeFromMidnight) {
                return scheduledDayMillis + scheduledTimeFromMidnight
            }
        }

        // all time points for current day expired
        // schedule earliest time point for next day
        val firstTimePointNextDay = rhythm.timePoints
            .minByOrNull { x -> x.absoluteTimeFromMidnight!! }!!
            .absoluteTimeFromMidnight!!

        return scheduledDayMillis + 1000 * 60 * 60 * 24 + firstTimePointNextDay * 60 * 1000
    }*/
}