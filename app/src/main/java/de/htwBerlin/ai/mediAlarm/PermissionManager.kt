package de.htwBerlin.ai.mediAlarm


import android.Manifest
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

class PermissionManager(private val context: MainActivity) {

    fun notificationPermissionGiven(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    @RequiresApi(33)
    fun requestNotificationPermission() {
        context.requestPermissionLauncher!!.launch(
            Manifest.permission.POST_NOTIFICATIONS
        )
    }
}