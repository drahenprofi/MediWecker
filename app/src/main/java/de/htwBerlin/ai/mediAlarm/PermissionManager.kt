package de.htwBerlin.ai.mediAlarm

import android.content.Context
import androidx.core.app.NotificationManagerCompat

class PermissionManager(private val context: Context) {

    fun notificationPermissionGiven(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}