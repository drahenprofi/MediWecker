package de.htwBerlin.ai.mediAlarm.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import de.htwBerlin.ai.mediAlarm.R
import de.htwBerlin.ai.mediAlarm.alarm.snooze.SnoozeButtonReceiver
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine

class NotificationSender {
    private val channelId = "1"

    fun send(context: Context, medicine: Medicine) {
        createNotificationChannel(context)

        val snoozePendingIntent = getSnoozePendingIntent(context, medicine)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setContentTitle("Medicine Reminder")
            .setContentText(medicine.name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(R.drawable.ic_launcher_foreground, "Schlummern", snoozePendingIntent)
            .build()

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(medicine.id.toInt(), notification)
        Log.d("Medicine Reminder", "Sent notification for medicine ${medicine.name}")
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

    private fun getSnoozePendingIntent(context: Context, medicine: Medicine): PendingIntent {
        val snoozeIntent = Intent(context, SnoozeButtonReceiver::class.java).apply {
            putExtra("MEDICINE_ID", medicine.id)
        }

        return PendingIntent.getBroadcast(context, medicine.id.toInt(), snoozeIntent, 0)
    }
}