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
import java.util.concurrent.Executors


class AlarmReceiver: BroadcastReceiver() {
    private val channelId = "1"

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)

        val database = AppDatabase.getDatabase(context)
        val alarmDao = database.alarmDao()
        val medicineDao = database.medicineDao()

        val alarmId = intent.getLongExtra("ALARM_ID", 0)

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val alarm = alarmDao.get(alarmId)

            if (alarm != null) {
                val medicine = medicineDao.get(alarm.medicineId)

                sendNotification(context, medicine)

                alarmDao.delete(alarm)
                MedicineScheduler(context).schedule(medicine)
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MedicineReminder"
            val descriptionText = "MedicineReminder"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(context: Context, medicine: Medicine) {
        val snoozePendingIntent = getSnoozePendingIntent(context, medicine)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setContentTitle("Medicine Reminder")
            .setContentText(medicine.name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_launcher_foreground, "Schlummern", snoozePendingIntent)
            .build()

        notification.flags = Notification.FLAG_AUTO_CANCEL

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(medicine.id.toInt(), notification)
        Log.d("Medicine Reminder", "Sent notification for medicine ${medicine.name}")
    }

    private fun getSnoozePendingIntent(context: Context, medicine: Medicine): PendingIntent {
        val snoozeIntent = Intent(context, SnoozeReceiver::class.java).apply {
            putExtra("MEDICINE_ID", medicine.id)
        }

        return PendingIntent.getBroadcast(context, medicine.id.toInt(), snoozeIntent, 0)
    }
}