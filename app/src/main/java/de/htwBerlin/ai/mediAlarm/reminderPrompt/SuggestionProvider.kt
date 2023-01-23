package de.htwBerlin.ai.mediAlarm.reminderPrompt

import android.content.Context
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.Constants
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimePoint
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimepointType
import de.htwBerlin.ai.mediAlarm.data.userTime.UserTimePreferences
import java.util.*

class SuggestionProvider(val context: Context) {
    private val gson = Gson()

    private val database = AppDatabase.getDatabase(context)
    private val alarmDao = database.alarmDao()
    private val medicineDao = database.medicineDao()

    private val userTime = UserTimePreferences(context).get()

    fun getSuggestion(alarm: Alarm?): List<RescheduleSuggestion> {
        if (alarm != null) {
            val medicine = medicineDao.get(alarm.medicineId)

            if (medicine != null) {
                val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)

                val timePoint = rhythm.timePoints
                    .first { it.uuid == alarm.timePointUUID }

                val mostRecentAlarms = alarmDao.getMostRecentAlarmsByTimePointUUID(timePoint.uuid)

                val rescheduleSuggested = mostRecentAlarms
                    .map { it.actualTimeUtc - it.targetTimeUtc > Constants.RESCHEDULE_MINIMUM_DEVIATION_IN_MINUTES * 60 * 1000 }
                    .all { it }

                if (rescheduleSuggested) {
                    when (timePoint.type) {
                        TimepointType.Morning -> {
                            return listOf(
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    RescheduleSuggestionType.RescheduleWakeUpTime,
                                    getSuggestedTimeFromMidnightForWakeUpTime(alarm)
                                ),
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    RescheduleSuggestionType.RescheduleAbsoluteTime,
                                    getSuggestedTimeFromMidnightForAbsoluteTime(alarm)
                                )
                            )
                        }
                        TimepointType.Night -> {
                            return listOf(
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    RescheduleSuggestionType.RescheduleSleepTime,
                                    getSuggestedTimeFromMidnightForWakeUpTime(alarm)
                                ),
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    RescheduleSuggestionType.RescheduleAbsoluteTime,
                                    getSuggestedTimeFromMidnightForAbsoluteTime(alarm)
                                )
                            )
                        }
                        else -> {
                            return listOf(
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    RescheduleSuggestionType.RescheduleAbsoluteTime,
                                    getSuggestedTimeFromMidnightForAbsoluteTime(alarm)
                                )
                            )
                        }
                    }
                }
            }

            return listOf(
                RescheduleSuggestion(
                    alarm.medicineId,
                    RescheduleSuggestionType.Acknowledged,
                    0L
                )
            )
        }

        return listOf()
    }

    private fun getSuggestedTimeFromMidnightForWakeUpTime(alarm: Alarm): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarm.targetTimeUtc

        return when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> userTime.wakeupMonday
            Calendar.TUESDAY -> userTime.wakeupTuesday
            Calendar.WEDNESDAY -> userTime.wakeupWednesday
            Calendar.THURSDAY -> userTime.wakeupThursday
            Calendar.FRIDAY -> userTime.wakeupFriday
            Calendar.SATURDAY -> userTime.wakeupSaturday
            Calendar.SUNDAY -> userTime.wakeupSunday
            else -> 0
        } + Constants.RESCHEDULE_MINIMUM_DEVIATION_IN_MINUTES
    }

    private fun getSuggestedTimeFromMidnightForSleepTime(alarm: Alarm): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarm.targetTimeUtc

        return when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> userTime.sleepMonday
            Calendar.TUESDAY -> userTime.sleepTuesday
            Calendar.WEDNESDAY -> userTime.sleepWednesday
            Calendar.THURSDAY -> userTime.sleepThursday
            Calendar.FRIDAY -> userTime.sleepFriday
            Calendar.SATURDAY -> userTime.sleepSaturday
            Calendar.SUNDAY -> userTime.sleepSunday
            else -> 0
        } + Constants.RESCHEDULE_MINIMUM_DEVIATION_IN_MINUTES
    }

    private fun getSuggestedTimeFromMidnightForAbsoluteTime(alarm: Alarm): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarm.targetTimeUtc
        val day = alarm.targetTimeUtc - calendar[Calendar.HOUR_OF_DAY] * 60 * 60 * 1000 - calendar[Calendar.MINUTE] * 60 * 1000 - calendar[Calendar.SECOND] * 1000

        return alarm.targetTimeUtc - day + Constants.RESCHEDULE_MINIMUM_DEVIATION_IN_MINUTES * 60 * 1000
    }
}