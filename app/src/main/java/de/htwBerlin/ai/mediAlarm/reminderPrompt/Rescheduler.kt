package de.htwBerlin.ai.mediAlarm.reminderPrompt

import android.content.Context
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.alarm.MedicineRescheduler
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimePoint
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimepointType
import de.htwBerlin.ai.mediAlarm.data.userTime.UserTimePreferences
import de.htwBerlin.ai.mediAlarm.reminderPrompt.data.RescheduleSuggestion
import de.htwBerlin.ai.mediAlarm.reminderPrompt.data.RescheduleSuggestionType
import java.util.Calendar

class Rescheduler(val context: Context) {
    private val gson = Gson()

    private val database = AppDatabase.getDatabase(context)
    private val medicineDao = database.medicineDao()
    private val alarmDao = database.alarmDao()

    private val userTimePreferences = UserTimePreferences(context)

    fun reschedule(suggestion: RescheduleSuggestion) {
        val medicine = medicineDao.get(suggestion.medicineId)
        val alarm = alarmDao.get(suggestion.alarmId)

        if (medicine != null && alarm != null) {
            when (suggestion.type) {
                RescheduleSuggestionType.Acknowledged -> return
                RescheduleSuggestionType.RescheduleAbsoluteTime -> rescheduleAbsoluteTime(suggestion, medicine, alarm)
                RescheduleSuggestionType.RescheduleWakeUpTime -> rescheduleWakeUpTime(suggestion, alarm)
                RescheduleSuggestionType.RescheduleSleepTime -> rescheduleSleepTime(suggestion, alarm)
            }

            MedicineRescheduler(context).reschedule(medicine)
        }
    }

    private fun rescheduleAbsoluteTime(suggestion: RescheduleSuggestion, medicine: Medicine, alarm: Alarm) {
        val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)

        rhythm.timePoints = rhythm.timePoints.map { timePoint ->
            if (timePoint.uuid == alarm.timePointUUID) {
                TimePoint(TimepointType.AbsoluteTime, suggestion.suggestedTimeFromMidnight, timePoint.uuid)
            } else {
                timePoint
            }
        }

        medicine.rhythm = gson.toJson(rhythm)

        medicineDao.update(medicine)
    }

    private fun rescheduleWakeUpTime(suggestion: RescheduleSuggestion, alarm: Alarm) {
        val userTime = userTimePreferences.get()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarm.targetTimeUtc

        when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> userTime.wakeupMonday = suggestion.suggestedTimeFromMidnight
            Calendar.TUESDAY -> userTime.wakeupTuesday = suggestion.suggestedTimeFromMidnight
            Calendar.WEDNESDAY -> userTime.wakeupWednesday = suggestion.suggestedTimeFromMidnight
            Calendar.THURSDAY -> userTime.wakeupThursday = suggestion.suggestedTimeFromMidnight
            Calendar.FRIDAY -> userTime.wakeupFriday = suggestion.suggestedTimeFromMidnight
            Calendar.SATURDAY -> userTime.wakeupSaturday = suggestion.suggestedTimeFromMidnight
            Calendar.SUNDAY -> userTime.wakeupSunday = suggestion.suggestedTimeFromMidnight
        }

        userTimePreferences.set(userTime)
    }

    private fun rescheduleSleepTime(suggestion: RescheduleSuggestion, alarm: Alarm) {
        val userTime = userTimePreferences.get()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarm.targetTimeUtc

        when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> userTime.sleepMonday = suggestion.suggestedTimeFromMidnight
            Calendar.TUESDAY -> userTime.sleepTuesday = suggestion.suggestedTimeFromMidnight
            Calendar.WEDNESDAY -> userTime.sleepWednesday = suggestion.suggestedTimeFromMidnight
            Calendar.THURSDAY -> userTime.sleepThursday = suggestion.suggestedTimeFromMidnight
            Calendar.FRIDAY -> userTime.sleepFriday = suggestion.suggestedTimeFromMidnight
            Calendar.SATURDAY -> userTime.sleepSaturday = suggestion.suggestedTimeFromMidnight
            Calendar.SUNDAY -> userTime.sleepSunday = suggestion.suggestedTimeFromMidnight
        }

        userTimePreferences.set(userTime)
    }
}