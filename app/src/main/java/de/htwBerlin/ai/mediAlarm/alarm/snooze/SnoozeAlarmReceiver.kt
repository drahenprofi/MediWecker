package de.htwBerlin.ai.mediAlarm.alarm.snooze

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.Constants
import de.htwBerlin.ai.mediAlarm.notification.NotificationSender
import java.util.concurrent.Executors

class SnoozeAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val database = AppDatabase.getDatabase(context)
        val medicineDao = database.medicineDao()
        val alarmDao = database.alarmDao()

        val medicineId = intent.getLongExtra(Constants.MEDICINE_ID, 0)
        val alarmId = intent.getLongExtra(Constants.ALARM_ID, 0)

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val medicine = medicineDao.get(medicineId)
            val alarm = alarmDao.get(alarmId)

            if (medicine != null && alarm != null) {
                val notificationId = NotificationSender().send(context, medicine, alarm)

                alarm.notificationId = notificationId

                alarmDao.update(alarm)
            }
        }
    }
}