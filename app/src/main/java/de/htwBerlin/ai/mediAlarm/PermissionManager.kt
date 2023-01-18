package de.htwBerlin.ai.mediAlarm

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

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