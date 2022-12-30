package de.htwBerlin.ai.mediAlarm.data.wakeUpTime

import android.content.Context

class WakeUpTimePreferences(context: Context) {

    private val preferences = context.getSharedPreferences("WakeUpTime", Context.MODE_PRIVATE)

    fun isInitialized(): Boolean {
        return preferences.getBoolean("WakeUpTime.Initialized", false)
    }

    fun get(): WakeUpTime {
        val monday = preferences.getLong("WakeUpTime.Monday", 420)
        val tuesday = preferences.getLong("WakeUpTime.Tuesday", 420)
        val wednesday = preferences.getLong("WakeUpTime.Wednesday", 420)
        val thursday = preferences.getLong("WakeUpTime.Thursday", 420)
        val friday = preferences.getLong("WakeUpTime.Friday", 420)
        val saturday = preferences.getLong("WakeUpTime.Saturday", 420)
        val sunday = preferences.getLong("WakeUpTime.Sunday", 420)

        return WakeUpTime(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
    }

    fun set(wakeUpTime: WakeUpTime) {
        val preferencesEditor = preferences.edit()

        preferencesEditor.putLong("WakeUpTime.Monday", wakeUpTime.monday)
        preferencesEditor.putLong("WakeUpTime.Tuesday", wakeUpTime.tuesday)
        preferencesEditor.putLong("WakeUpTime.Wednesday", wakeUpTime.wednesday)
        preferencesEditor.putLong("WakeUpTime.Thursday", wakeUpTime.thursday)
        preferencesEditor.putLong("WakeUpTime.Friday", wakeUpTime.friday)
        preferencesEditor.putLong("WakeUpTime.Saturday", wakeUpTime.saturday)
        preferencesEditor.putLong("WakeUpTime.Sunday", wakeUpTime.sunday)

        preferencesEditor.putBoolean("WakeUpTime.Initialized", true)

        preferencesEditor.apply()
    }
}