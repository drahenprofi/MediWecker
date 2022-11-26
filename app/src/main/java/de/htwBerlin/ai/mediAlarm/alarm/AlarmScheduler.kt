package de.htwBerlin.ai.mediAlarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm

class AlarmScheduler(private val context: Context) {

    fun schedule(alarm: Alarm) {
        // TODO: use alarm, save alarm to DB, send alarm to AlarmReceiver
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            100,
            pendingIntent
        )

        Log.d("Medicine Reminder", "created alarm")
    }
}