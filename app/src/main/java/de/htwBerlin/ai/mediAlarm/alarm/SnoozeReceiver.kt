package de.htwBerlin.ai.mediAlarm.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class SnoozeReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineId = intent.getLongExtra("MEDICINE_ID", 0)
        Log.d("SnoozeReceiver", "Received snooze event for medicine $medicineId")

        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(medicineId.toInt())

        //TODO("Not yet implemented")
    }
}