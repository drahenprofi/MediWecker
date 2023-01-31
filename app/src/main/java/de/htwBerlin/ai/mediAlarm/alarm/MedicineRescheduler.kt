package de.htwBerlin.ai.mediAlarm.alarm

import android.content.Context
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine

class MedicineRescheduler(val context: Context) {

    private val alarmDao = AppDatabase.getDatabase(context).alarmDao()

    fun reschedule(medicine: Medicine) {
        val runningAlarms = alarmDao
            .getByMedicineId(medicine.id)
            .filter { alarm -> !alarm.isExpired }

        runningAlarms.forEach { alarm -> alarmDao.delete(alarm) }

        MedicineScheduler(context).schedule(medicine)
    }
}