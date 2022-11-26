package de.htwBerlin.ai.mediAlarm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import de.htwBerlin.ai.mediAlarm.data.AppDatabase

class RebootBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmScheduler = AlarmScheduler(context)

        val alarmDao = Room
            .databaseBuilder(
                context,
                AppDatabase::class.java, "medi-wecker-database"
            )
            .build()
            .alarmDao()

        val alarms = alarmDao.getAll()

        alarms.forEach {
            alarmScheduler.schedule(it)
        }
    }
}