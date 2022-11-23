package de.htwBerlin.ai.mediAlarm

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine


class AlarmReceiver: BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null
    private var CHANNEL_ID = "1"

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)
        Log.d("MedicineReminder", "Alarm received")

        val medicine = Medicine("Iboprofen", 800f, "TODO: rhythm")

        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentTitle("Medicine Reminder")
            .setContentText(medicine.name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(medicine.id, builder.build())

        Log.d("Medicine Reminder", "sent notification")

    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MedicineReminder"
            val descriptionText = "MedicineReminder"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}