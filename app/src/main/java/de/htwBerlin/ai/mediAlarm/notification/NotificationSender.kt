package de.htwBerlin.ai.mediAlarm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import de.htwBerlin.ai.mediAlarm.MainActivity
import de.htwBerlin.ai.mediAlarm.R
import de.htwBerlin.ai.mediAlarm.alarm.snooze.SnoozeButtonReceiver
import de.htwBerlin.ai.mediAlarm.data.Constants
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import java.util.*
import kotlin.random.Random

class NotificationSender {
    private val channelId = "1"

    fun send(context: Context, medicine: Medicine, alarm: Alarm) {
        createNotificationChannel(context)

        val notificationId = Random.nextInt()

        val clickPendingIntent = getClickPendingIntent(context, medicine, alarm, notificationId)
        val snoozePendingIntent = getSnoozePendingIntent(context, medicine, alarm, notificationId)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setContentTitle("Medicine Reminder")
            .setContentText(medicine.name)
            .setContentIntent(clickPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(R.drawable.ic_launcher_foreground, "Schlummern", snoozePendingIntent)
            .build()

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(notificationId, notification)

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

    private fun getClickPendingIntent(context: Context, medicine: Medicine, alarm: Alarm, notificationId: Int): PendingIntent {
        val scheduledTimeUtc = Calendar.getInstance().timeInMillis

        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(Constants.MEDICINE_ID, medicine.id)
            putExtra(Constants.ALARM_ID, alarm.id)
            putExtra(Constants.SCHEDULED_TIME_UTC, scheduledTimeUtc)
            putExtra(Constants.NOTIFICATION_ID, notificationId)
            putExtra(Constants.NOTIFICATION_CLICK, true)
        }

        return PendingIntent.getActivity(context, medicine.id.toInt(), clickIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getSnoozePendingIntent(context: Context, medicine: Medicine, alarm: Alarm, notificationId: Int): PendingIntent {
        val snoozeIntent = Intent(context, SnoozeButtonReceiver::class.java).apply {
            putExtra(Constants.MEDICINE_ID, medicine.id)
            putExtra(Constants.ALARM_ID, alarm.id)
            putExtra(Constants.NOTIFICATION_ID, notificationId)
        }

        return PendingIntent.getBroadcast(context, medicine.id.toInt(), snoozeIntent, PendingIntent.FLAG_IMMUTABLE)
    }
}