package de.htwBerlin.ai.mediAlarm


import android.Manifest
import androidx.core.app.NotificationManagerCompat

class PermissionManager(private val context: MainActivity) {

    fun notificationPermissionGiven(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun requestNotificationPermission() {
        context.requestPermissionLauncher!!.launch(
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

}