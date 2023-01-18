package de.htwBerlin.ai.mediAlarm.notification

import android.app.NotificationManager
import android.content.Context

class NotificationCanceller(val context: Context) {
    private val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun cancel(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
}
