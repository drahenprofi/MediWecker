package de.htwBerlin.ai.mediAlarm.alarm

import android.content.Context
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimePoint
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimepointType
import de.htwBerlin.ai.mediAlarm.data.userTime.UserTimePreferences
import java.util.*

class TargetTimeCalculator(val context: Context) {
    private val gson = Gson()
    private val userTimes = UserTimePreferences(context).get()
    private val alarmDao = AppDatabase.getDatabase(context).alarmDao()

    fun calculate(medicine: Medicine, calendar: Calendar): Pair<Long, UUID> {
        val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)

        val now = calendar.timeInMillis
        val currentDay = now - calendar[Calendar.HOUR_OF_DAY] * 60 * 60 * 1000 - calendar[Calendar.MINUTE] * 60 * 1000 - calendar[Calendar.SECOND] * 1000
        val currentTimeFromMidnight = (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * 60 * 1000

        val pendingAlarmsToday = getAlarmTimePoints(rhythm.timePoints, calendar[Calendar.DAY_OF_WEEK])
            .map { timePoint -> Pair(timePoint.first * 60 * 1000, timePoint.second) }
            .filter { timePoint -> timePoint.first > currentTimeFromMidnight }
            .sortedBy { it.first }

        var scheduledDayDifference: Int = if (rhythm.intervalDays != null) {
            val mostRecentExpiredAlarm = alarmDao.getMostRecentExpiredAlarmByMedicineId(medicine.id)

            if (mostRecentExpiredAlarm != null) {
                val dayDifference = (now - mostRecentExpiredAlarm.targetTimeUtc).toInt() / 1000 / 60 / 60 / 24

                if (dayDifference == 0 && pendingAlarmsToday.isNotEmpty()) {
                    0
                } else {
                    (rhythm.intervalDays.days - dayDifference) % rhythm.intervalDays.days
                }
            } else if (pendingAlarmsToday.isEmpty()) {
                rhythm.intervalDays.days
            } else {
                0
            }
        } else if (rhythm.specificDays != null) {
            getDayDifferenceBySpecificDaysData(rhythm, calendar, scheduleToday = true)
        } else {
            0
        }

        if (scheduledDayDifference == 0 && pendingAlarmsToday.any()) {
            return Pair(currentDay + pendingAlarmsToday.first().first, pendingAlarmsToday.first().second)
        }

        scheduledDayDifference = if (rhythm.intervalDays != null) {
            rhythm.intervalDays.days
        } else if (rhythm.specificDays != null) {
            getDayDifferenceBySpecificDaysData(rhythm, calendar, scheduleToday = false)
        } else {
            1
        }

        val plannedDay = (calendar[Calendar.DAY_OF_WEEK] + scheduledDayDifference) % 8

        val nextPendingAlarm = getAlarmTimePoints(rhythm.timePoints, plannedDay)
            .map { timePoint -> Pair(timePoint.first * 60 * 1000, timePoint.second) }
            .sortedBy { it.first }

        return Pair(
            currentDay + nextPendingAlarm.first().first + 1000 * 60 * 60 * 24 * scheduledDayDifference,
            nextPendingAlarm.first().second
        )
    }

    private fun getAlarmTimePoints(timePoints: List<TimePoint>, weekDay: Int): List<Pair<Long, UUID>> {
        val result = mutableListOf<Pair<Long, UUID>>()

        for (timePoint in timePoints) {
            when(timePoint.type) {
                TimepointType.AbsoluteTime -> result.add(Pair(timePoint.absoluteTimeFromMidnight!!, timePoint.uuid))
                TimepointType.Morning -> when (weekDay) {
                    Calendar.MONDAY -> result.add(Pair(userTimes.wakeupMonday, timePoint.uuid))
                    Calendar.TUESDAY -> result.add(Pair(userTimes.wakeupTuesday, timePoint.uuid))
                    Calendar.WEDNESDAY -> result.add(Pair(userTimes.wakeupWednesday, timePoint.uuid))
                    Calendar.THURSDAY -> result.add(Pair(userTimes.wakeupThursday, timePoint.uuid))
                    Calendar.FRIDAY -> result.add(Pair(userTimes.wakeupFriday, timePoint.uuid))
                    Calendar.SATURDAY -> result.add(Pair(userTimes.wakeupSaturday, timePoint.uuid))
                    Calendar.SUNDAY -> result.add(Pair(userTimes.wakeupSunday, timePoint.uuid))
                }
                TimepointType.Midday -> when (weekDay) {
                    Calendar.MONDAY -> result.add(Pair((userTimes.wakeupMonday + userTimes.sleepMonday) / 2, timePoint.uuid))
                    Calendar.TUESDAY -> result.add(Pair((userTimes.wakeupTuesday + userTimes.sleepTuesday) / 2, timePoint.uuid))
                    Calendar.WEDNESDAY -> result.add(Pair((userTimes.wakeupWednesday + userTimes.sleepWednesday) / 2, timePoint.uuid))
                    Calendar.THURSDAY -> result.add(Pair((userTimes.wakeupThursday + userTimes.sleepThursday) / 2, timePoint.uuid))
                    Calendar.FRIDAY -> result.add(Pair((userTimes.wakeupFriday + userTimes.sleepFriday) / 2, timePoint.uuid))
                    Calendar.SATURDAY -> result.add(Pair((userTimes.wakeupSaturday + userTimes.sleepSaturday) / 2, timePoint.uuid))
                    Calendar.SUNDAY -> result.add(Pair((userTimes.wakeupSunday + userTimes.sleepSunday) / 2, timePoint.uuid))
                }
                TimepointType.Evening -> when (weekDay) {
                    Calendar.MONDAY -> result.add(Pair(userTimes.sleepMonday, timePoint.uuid))
                    Calendar.TUESDAY -> result.add(Pair(userTimes.sleepTuesday, timePoint.uuid))
                    Calendar.WEDNESDAY -> result.add(Pair(userTimes.sleepWednesday, timePoint.uuid))
                    Calendar.THURSDAY -> result.add(Pair(userTimes.sleepThursday, timePoint.uuid))
                    Calendar.FRIDAY -> result.add(Pair(userTimes.sleepFriday, timePoint.uuid))
                    Calendar.SATURDAY -> result.add(Pair(userTimes.sleepSaturday, timePoint.uuid))
                    Calendar.SUNDAY -> result.add(Pair(userTimes.sleepSunday, timePoint.uuid))
                }
                TimepointType.Night -> when (weekDay) {
                    Calendar.MONDAY -> result.add(Pair(userTimes.sleepMonday + 4 * 60, timePoint.uuid))
                    Calendar.TUESDAY -> result.add(Pair(userTimes.sleepTuesday + 4 * 60, timePoint.uuid))
                    Calendar.WEDNESDAY -> result.add(Pair(userTimes.sleepWednesday + 4 * 60, timePoint.uuid))
                    Calendar.THURSDAY -> result.add(Pair(userTimes.sleepThursday + 4 * 60, timePoint.uuid))
                    Calendar.FRIDAY -> result.add(Pair(userTimes.sleepFriday + 4 * 60, timePoint.uuid))
                    Calendar.SATURDAY -> result.add(Pair(userTimes.sleepSaturday + 4 * 60, timePoint.uuid))
                    Calendar.SUNDAY -> result.add(Pair(userTimes.sleepSunday + 4 * 60, timePoint.uuid))
                }
            }
        }

        return result
    }

    private fun getDayDifferenceBySpecificDaysData(rhythm: Rhythm, calendar: Calendar, scheduleToday: Boolean): Int {
        if (rhythm.specificDays == null) return 0

        val specificDays = rhythm.specificDays!!

        return when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> {
                if (specificDays.monday && scheduleToday) {
                    0
                } else if (specificDays.tuesday) {
                    1
                } else if (specificDays.wednesday) {
                    2
                } else if (specificDays.thursday) {
                    3
                } else if (specificDays.friday) {
                    4
                } else if (specificDays.saturday) {
                    5
                } else if (specificDays.sunday) {
                    6
                } else {
                    7
                }
            }
            Calendar.TUESDAY -> {
                if (specificDays.tuesday && scheduleToday) {
                    0
                } else if (specificDays.wednesday) {
                    1
                } else if (specificDays.thursday) {
                    2
                } else if (specificDays.friday) {
                    3
                } else if (specificDays.saturday) {
                    4
                } else if (specificDays.sunday) {
                    5
                } else if (specificDays.monday) {
                    6
                } else {
                    7
                }
            }
            Calendar.WEDNESDAY -> {
                if (specificDays.wednesday && scheduleToday) {
                    0
                } else if (specificDays.thursday) {
                    1
                } else if (specificDays.friday) {
                    2
                } else if (specificDays.saturday) {
                    3
                } else if (specificDays.sunday) {
                    4
                } else if (specificDays.monday) {
                    5
                } else if (specificDays.tuesday) {
                    6
                } else {
                    7
                }
            }
            Calendar.THURSDAY -> {
                if (specificDays.thursday && scheduleToday) {
                    0
                } else if (specificDays.friday) {
                    1
                } else if (specificDays.saturday) {
                    2
                } else if (specificDays.sunday) {
                    3
                } else if (specificDays.monday) {
                    4
                } else if (specificDays.tuesday) {
                    5
                } else if (specificDays.wednesday) {
                    6
                } else {
                    7
                }
            }
            Calendar.FRIDAY -> {
                if (specificDays.friday && scheduleToday) {
                    0
                } else if (specificDays.saturday) {
                    1
                } else if (specificDays.sunday) {
                    2
                } else if (specificDays.monday) {
                    3
                } else if (specificDays.tuesday) {
                    4
                } else if (specificDays.wednesday) {
                    5
                } else if (specificDays.thursday) {
                    6
                } else {
                    7
                }
            }
            Calendar.SATURDAY -> {
                if (specificDays.saturday && scheduleToday) {
                    0
                } else if (specificDays.sunday) {
                    1
                } else if (specificDays.monday) {
                    2
                } else if (specificDays.tuesday) {
                    3
                } else if (specificDays.wednesday) {
                    4
                } else if (specificDays.thursday) {
                    5
                } else if (specificDays.friday) {
                    6
                } else {
                    7
                }
            }
            Calendar.SUNDAY -> {
                if (specificDays.sunday && scheduleToday) {
                    0
                } else if (specificDays.monday) {
                    1
                } else if (specificDays.tuesday) {
                    2
                } else if (specificDays.wednesday) {
                    3
                } else if (specificDays.thursday) {
                    4
                } else if (specificDays.friday) {
                    5
                } else if (specificDays.saturday) {
                    6
                } else {
                    7
                }
            }
            else -> {
                0
            }
        }
    }
}