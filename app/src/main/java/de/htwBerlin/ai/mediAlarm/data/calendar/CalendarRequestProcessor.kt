package de.htwBerlin.ai.mediAlarm.data.calendar

import android.content.Context
import de.htwBerlin.ai.mediAlarm.alarm.TargetTimeCalculator
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao
import java.util.Calendar

class CalendarRequestProcessor(val context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val medicineDao: MedicineDao = database.medicineDao()

    fun process(request: CalendarRequest): List<CalendarItem> {
        val result = mutableListOf<CalendarItem>()

        val medicines = medicineDao.getAll()

        val targetTimeCalculator = TargetTimeCalculator(context)
        val calendar = Calendar.getInstance()

        medicines.forEach { medicine ->
            var scheduledTimeUtc = request.from

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