package de.htwBerlin.ai.mediAlarm.data.calendar

import android.content.Context
import de.htwBerlin.ai.mediAlarm.alarm.TargetTimeCalculator
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.AlarmDao
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao
import java.util.Calendar

class CalendarRequestProcessor(val context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val medicineDao: MedicineDao = database.medicineDao()
    private val alarmDao: AlarmDao = database.alarmDao()

    fun process(request: CalendarRequest): List<CalendarItem> {
        return getPastCalendarItems(request) + getFutureCalendarItems(request)
    }

    private fun getPastCalendarItems(request: CalendarRequest): List<CalendarItem> {
        val alarms = alarmDao.getAlarmsByTimeFrame(request.from, request.to)

        val medicineIds = alarms.map { alarm -> alarm.medicineId }

        val medicines = medicineDao.loadAllByIds(medicineIds)

        return alarms.map { alarm ->
            val medicine = medicines.first { medicine -> medicine.id == alarm.medicineId }
            CalendarItem(medicine, alarm.targetTime)
        }
    }

    private fun getFutureCalendarItems(request: CalendarRequest): List<CalendarItem> {
        val result = mutableListOf<CalendarItem>()

        val medicines = medicineDao.getAll()

        val targetTimeCalculator = TargetTimeCalculator(context)
        val calendar = Calendar.getInstance()

        medicines.forEach { medicine ->
            var scheduledTimeUtc = calendar.timeInMillis

            while (scheduledTimeUtc < request.to) {
                calendar.timeInMillis = scheduledTimeUtc
                scheduledTimeUtc = targetTimeCalculator.calculate(medicine, calendar)

                if (scheduledTimeUtc < request.to) {
                    result.add(CalendarItem(medicine, scheduledTimeUtc))
                }
            }
        }

        return result
    }
}