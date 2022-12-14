package de.htwBerlin.ai.mediAlarm.alarm.snooze

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import kotlin.random.Random


class SnoozeButtonReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineId = intent.getLongExtra("MEDICINE_ID", 0)
        Log.d("SnoozeReceiver", "Received snooze event for medicine $medicineId")

        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(medicineId.toInt())

        createSnoozeAlarm(context, medicineId)
    }

    private fun createSnoozeAlarm(context: Context, medicineId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, SnoozeAlarmReceiver::class.java)
        intent.putExtra("MEDICINE_ID", medicineId)

        val randomCode = Random.nextInt()

        val pendingIntent = PendingIntent.getBroadcast(context, randomCode, intent, PendingIntent.FLAG_IMMUTABLE)

        val triggerAtMillis = SystemClock.elapsedRealtime() + 10 * 60 * 1000

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }
}