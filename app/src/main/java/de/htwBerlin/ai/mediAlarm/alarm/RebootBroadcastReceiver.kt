package de.htwBerlin.ai.mediAlarm.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.htwBerlin.ai.mediAlarm.data.AppDatabase

class RebootBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmDao = AppDatabase.getDatabase(context).alarmDao()
        val alarmScheduler = AlarmScheduler(context)

        val alarms = alarmDao.getAll()

        alarms.forEach {
            alarmScheduler.schedule(it)
        }
    }
}