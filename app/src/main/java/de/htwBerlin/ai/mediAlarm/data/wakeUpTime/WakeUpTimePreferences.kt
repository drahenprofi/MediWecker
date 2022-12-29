package de.htwBerlin.ai.mediAlarm.data.wakeUpTime

import android.content.Context

class WakeUpTimePreferences(context: Context) {

    private val preferences = context.getSharedPreferences("WakeUpTime", Context.MODE_PRIVATE)

    fun isInitialized(): Boolean {
        return preferences.getBoolean("WakeUpTime.Initialized", false)
    }

    fun get(): WakeUpTime {
        val monday = preferences.getInt("WakeUpTime.Monday", 420)
        val tuesday = preferences.getInt("WakeUpTime.Tuesday", 420)
        val wednesday = preferences.getInt("WakeUpTime.Wednesday", 420)
        val thursday = preferences.getInt("WakeUpTime.Thursday", 420)
        val friday = preferences.getInt("WakeUpTime.Friday", 420)
        val saturday = preferences.getInt("WakeUpTime.Saturday", 420)
        val sunday = preferences.getInt("WakeUpTime.Sunday", 420)

        return WakeUpTime(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
    }

    fun set(wakeUpTime: WakeUpTime) {
        val preferencesEditor = preferences.edit()

        preferencesEditor.putInt("WakeUpTime.Monday", wakeUpTime.monday)
        preferencesEditor.putInt("WakeUpTime.Tuesday", wakeUpTime.tuesday)
        preferencesEditor.putInt("WakeUpTime.Wednesday", wakeUpTime.wednesday)
        preferencesEditor.putInt("WakeUpTime.Thursday", wakeUpTime.thursday)
        preferencesEditor.putInt("WakeUpTime.Friday", wakeUpTime.friday)
        preferencesEditor.putInt("WakeUpTime.Saturday", wakeUpTime.saturday)
        preferencesEditor.putInt("WakeUpTime.Sunday", wakeUpTime.sunday)

        preferencesEditor.putBoolean("WakeUpTime.Initialized", true)

        preferencesEditor.apply()
    }
}