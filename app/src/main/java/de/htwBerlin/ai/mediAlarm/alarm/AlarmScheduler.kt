package de.htwBerlin.ai.mediAlarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    fun schedule(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("alarmId", alarm.id)

        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        val triggerAtMillis = getTriggerAtMillisFromTargetTime(alarm.targetTime)

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    private fun getTriggerAtMillisFromTargetTime(targetTime: Long): Long {
        return targetTime - Calendar.getInstance().timeInMillis + SystemClock.elapsedRealtime()
    }
}