package de.htwBerlin.ai.mediAlarm.alarm

import android.content.Context
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import java.util.*

class MedicineScheduler(private val context: Context) {

    private val alarmDao = AppDatabase.getDatabase(context).alarmDao()

    fun schedule(medicine: Medicine) {
        val runningAlarms = alarmDao
            .getByMedicineId(medicine.id)
            .filter { alarm -> !alarm.isExpired }

        if (runningAlarms.isNotEmpty()) {
            return
        }

        val calendar = Calendar.getInstance()
        val targetTimeCalculatorResult = TargetTimeCalculator(context).calculate(medicine, calendar)

        val alarm = Alarm(medicine.id, targetTimeCalculatorResult.second, targetTimeCalculatorResult.first)
        alarm.id = alarmDao.insert(alarm)

        AlarmScheduler(context).schedule(alarm)
    }
}