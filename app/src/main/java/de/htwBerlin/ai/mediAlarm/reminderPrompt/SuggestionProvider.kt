package de.htwBerlin.ai.mediAlarm.reminderPrompt

import android.content.Context
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.Constants
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimepointType
import de.htwBerlin.ai.mediAlarm.reminderPrompt.data.RescheduleSuggestion
import de.htwBerlin.ai.mediAlarm.reminderPrompt.data.RescheduleSuggestionType
import java.util.*
import kotlin.math.abs

class SuggestionProvider(val context: Context) {
    private val gson = Gson()

    private val database = AppDatabase.getDatabase(context)
    private val alarmDao = database.alarmDao()
    private val medicineDao = database.medicineDao()

    fun getSuggestion(alarm: Alarm?): List<RescheduleSuggestion> {
        if (alarm != null) {
            val medicine = medicineDao.get(alarm.medicineId)

            if (medicine != null) {
                val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)

                val timePoint = rhythm.timePoints
                    .first { it.uuid == alarm.timePointUUID }

                val mostRecentAlarms = alarmDao.getMostRecentAlarmsByTimePointUUID(timePoint.uuid)

                val relevantAlarms = mostRecentAlarms
                    .filter { it.actualTimeUtc > 0L }
                    .map { abs(it.actualTimeUtc - it.targetTimeUtc) > Constants.RESCHEDULE_MINIMUM_DEVIATION_IN_MINUTES * 60 * 1000 }

                val rescheduleSuggested = relevantAlarms.isNotEmpty() && relevantAlarms.all { it }

                val suggestedTimeFromMidnight = getRoundedTimeFromMidnightFromTicks(
                    mostRecentAlarms
                        .filter { it.actualTimeUtc > 0L }
                        .map { it.actualTimeUtc - it.targetTimeUtc }
                        .average()
                        .toLong() + alarm.targetTimeUtc
                )

                if (rescheduleSuggested) {
                    when (timePoint.type) {
                        TimepointType.Morning -> {
                            return listOf(
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    alarm.id,
                                    RescheduleSuggestionType.RescheduleWakeUpTime,
                                    suggestedTimeFromMidnight
                                ),
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    alarm.id,
                                    RescheduleSuggestionType.RescheduleAbsoluteTime,
                                    suggestedTimeFromMidnight
                                )
                            )
                        }
                        TimepointType.Night -> {
                            return listOf(
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    alarm.id,
                                    RescheduleSuggestionType.RescheduleSleepTime,
                                    suggestedTimeFromMidnight
                                ),
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    alarm.id,
                                    RescheduleSuggestionType.RescheduleAbsoluteTime,
                                    suggestedTimeFromMidnight
                                )
                            )
                        }
                        else -> {
                            return listOf(
                                RescheduleSuggestion(
                                    alarm.medicineId,
                                    alarm.id,
                                    RescheduleSuggestionType.RescheduleAbsoluteTime,
                                    suggestedTimeFromMidnight
                                )
                            )
                        }
                    }
                }
            }

            return listOf(
                RescheduleSuggestion(
                    alarm.medicineId,
                    alarm.id,
                    RescheduleSuggestionType.Acknowledged,
                    0L
                )
            )
        }

        return listOf()
    }

    private fun getRoundedTimeFromMidnightFromTicks(ticks: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = ticks
        val day = ticks - calendar[Calendar.HOUR_OF_DAY] * 60 * 60 * 1000 - calendar[Calendar.MINUTE] * 60 * 1000 - calendar[Calendar.SECOND] * 1000

        val unRoundedMinutes = calendar[Calendar.MINUTE]
        val mod = unRoundedMinutes % 10
        calendar.add(Calendar.MINUTE, if (mod < 6) -mod else 10 - mod)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return (calendar.timeInMillis - day) / 1000 / 60
    }
}