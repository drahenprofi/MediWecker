package de.htwBerlin.ai.mediAlarm.data.userTime

import android.content.Context

class UserTimePreferences(context: Context) {

    private val preferences = context.getSharedPreferences("UserTime", Context.MODE_PRIVATE)

    fun isInitialized(): Boolean {
        return preferences.getBoolean("UserTime.Initialized", false)
    }

    fun get(): UserTime {
        val wakeupMonday = preferences.getLong("UserTime.WakeupMonday", 420)
        val wakeupTuesday = preferences.getLong("UserTime.WakeupTuesday", 420)
        val wakeupWednesday = preferences.getLong("UserTime.WakeupWednesday", 420)
        val wakeupThursday = preferences.getLong("UserTime.WakeupThursday", 420)
        val wakeupFriday = preferences.getLong("UserTime.WakeupFriday", 420)
        val wakeupSaturday = preferences.getLong("UserTime.WakeupSaturday", 420)
        val wakeupSunday = preferences.getLong("UserTime.WakeupSunday", 420)

        val sleepMonday = preferences.getLong("UserTime.SleepMonday", 1320)
        val sleepTuesday = preferences.getLong("UserTime.SleepTuesday", 1320)
        val sleepWednesday = preferences.getLong("UserTime.SleepWednesday", 1320)
        val sleepThursday = preferences.getLong("UserTime.SleepThursday", 1320)
        val sleepFriday = preferences.getLong("UserTime.SleepFriday", 1320)
        val sleepSaturday = preferences.getLong("UserTime.SleepSaturday", 1320)
        val sleepSunday = preferences.getLong("UserTime.SleepSunday", 1320)
        

        return UserTime(wakeupMonday, wakeupTuesday, wakeupWednesday,
            wakeupThursday, wakeupFriday, wakeupSaturday, wakeupSunday,
            sleepMonday, sleepTuesday, sleepWednesday, sleepThursday,
            sleepFriday, sleepSaturday, sleepSunday)
    }

    fun set(userTime: UserTime) {
        val preferencesEditor = preferences.edit()

        preferencesEditor.putLong("UserTime.WakeupMonday", userTime.wakeupMonday)
        preferencesEditor.putLong("UserTime.WakeupTuesday", userTime.wakeupTuesday)
        preferencesEditor.putLong("UserTime.WakeupWednesday", userTime.wakeupWednesday)
        preferencesEditor.putLong("UserTime.WakeupThursday", userTime.wakeupThursday)
        preferencesEditor.putLong("UserTime.WakeupFriday", userTime.wakeupFriday)
        preferencesEditor.putLong("UserTime.WakeupSaturday", userTime.wakeupSaturday)
        preferencesEditor.putLong("UserTime.WakeupSunday", userTime.wakeupSunday)

        preferencesEditor.putLong("UserTime.SleepMonday", userTime.sleepMonday)
        preferencesEditor.putLong("UserTime.SleepTuesday", userTime.sleepTuesday)
        preferencesEditor.putLong("UserTime.SleepWednesday", userTime.sleepWednesday)
        preferencesEditor.putLong("UserTime.SleepThursday", userTime.sleepThursday)
        preferencesEditor.putLong("UserTime.SleepFriday", userTime.sleepFriday)
        preferencesEditor.putLong("UserTime.SleepSaturday", userTime.sleepSaturday)
        preferencesEditor.putLong("UserTime.SleepSunday", userTime.sleepSunday)

        preferencesEditor.putBoolean("UserTime.Initialized", true)

        preferencesEditor.apply()
    }
}