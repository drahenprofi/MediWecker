package de.htwBerlin.ai.mediAlarm.alarm.snooze

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.Constants
import de.htwBerlin.ai.mediAlarm.notification.NotificationCanceller
import java.util.concurrent.Executors
import kotlin.random.Random

class SnoozeButtonReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineId = intent.getLongExtra(Constants.MEDICINE_ID, 0)
        val alarmId = intent.getLongExtra(Constants.ALARM_ID, 0)
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val alarm = AppDatabase.getDatabase(context).alarmDao().get(alarmId)

            if (alarm != null) {
                NotificationCanceller(context).cancel(alarm.notificationId)
            }

            createSnoozeAlarm(context, medicineId, alarmId)
        }
    }

    private fun createSnoozeAlarm(context: Context, medicineId: Long, alarmId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, SnoozeAlarmReceiver::class.java)
        intent.putExtra(Constants.MEDICINE_ID, medicineId)
        intent.putExtra(Constants.ALARM_ID, alarmId)

        val randomCode = Random.nextInt()

        val pendingIntent = PendingIntent.getBroadcast(context, randomCode, intent, PendingIntent.FLAG_IMMUTABLE)

        val triggerAtMillis = SystemClock.elapsedRealtime() + Constants.SNOOZE_INTERVAL_IN_MINUTES * 60 * 1000

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }
}