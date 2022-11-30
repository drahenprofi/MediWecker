package de.htwBerlin.ai.mediAlarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.room.Room
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.MainActivity
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.alarm.AlarmDao
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    private val alarmDao: AlarmDao = AppDatabase.getDatabase(context).alarmDao()
    private val gson: Gson = Gson()

    fun schedule(alarm: Alarm) {
        // TODO: use alarm, send alarm to AlarmReceiver
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            alarm.targetTime,
            pendingIntent
        )

        Log.d("Medicine Reminder", "created alarm")
    }
}