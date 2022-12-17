package de.htwBerlin.ai.mediAlarm.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import de.htwBerlin.ai.mediAlarm.R
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.notification.NotificationSender
import java.util.concurrent.Executors


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val database = AppDatabase.getDatabase(context)
        val alarmDao = database.alarmDao()
        val medicineDao = database.medicineDao()

        val alarmId = intent.getLongExtra("ALARM_ID", 0)

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val alarm = alarmDao.get(alarmId)

            if (alarm != null) {
                val medicine = medicineDao.get(alarm.medicineId)

                NotificationSender().send(context, medicine)

                alarmDao.delete(alarm)
                MedicineScheduler(context).schedule(medicine)
            }
        }
    }
}