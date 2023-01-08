package de.htwBerlin.ai.mediAlarm.alarm

import android.content.Context
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimePoint
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimepointType
import de.htwBerlin.ai.mediAlarm.data.userTime.UserTimePreferences
import java.util.*

class TargetTimeCalculator(val context: Context) {

    private val gson = Gson()
    private val wakeUpTime = UserTimePreferences(context).get()

    fun calculate(medicine: Medicine): Long {
        val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)
        return calculate(rhythm)
    }

    private fun calculate(rhythm: Rhythm): Long {
        val calendar = Calendar.getInstance()

        val now = calendar.timeInMillis
        val currentDay = now - calendar[Calendar.HOUR_OF_DAY] * 60 * 60 * 1000 - calendar[Calendar.MINUTE] * 60 * 1000 - calendar[Calendar.SECOND] * 1000
        val currentTimeFromMidnight = (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * 60 * 1000

        val pendingAlarmsToday = getAlarmTimePoints(rhythm.timePoints, calendar[Calendar.DAY_OF_WEEK])
            .map { time -> time * 60 * 1000 }
            .filter { time -> time > currentTimeFromMidnight }
            .sorted()

        var scheduledDayDifference = if (rhythm.intervalDays != null && pendingAlarmsToday.isEmpty()) {
            rhythm.intervalDays.days
        } else if (rhythm.specificDays != null) {
            getDayDifferenceBySpecificDaysData(rhythm, calendar, scheduleToday = true)
        } else {
            0
        }

        if (scheduledDayDifference == 0 && pendingAlarmsToday.any()) {
            return currentDay + pendingAlarmsToday.first()
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
            .map { time -> time * 60 * 1000 }
            .sorted()

        return currentDay + nextPendingAlarm.first() + 1000 * 60 * 60 * 24 * scheduledDayDifference
    }

    private fun getAlarmTimePoints(timePoints: List<TimePoint>, weekDay: Int): List<Long> {
        val result = mutableListOf<Long>()

        for (timePoint in timePoints) {
            when(timePoint.type) {
                TimepointType.AbsoluteTime -> result.add(timePoint.absoluteTimeFromMidnight!!)
                TimepointType.Morning -> when (weekDay) {
                    Calendar.MONDAY -> result.add(wakeUpTime.wakeupMonday)
                    Calendar.TUESDAY -> result.add(wakeUpTime.wakeupTuesday)
                    Calendar.WEDNESDAY -> result.add(wakeUpTime.wakeupWednesday)
                    Calendar.THURSDAY -> result.add(wakeUpTime.wakeupThursday)
                    Calendar.FRIDAY -> result.add(wakeUpTime.wakeupFriday)
                    Calendar.SATURDAY -> result.add(wakeUpTime.wakeupSaturday)
                    Calendar.SUNDAY -> result.add(wakeUpTime.wakeupSunday)
                }
                TimepointType.Midday -> TODO()
                TimepointType.Evening -> TODO()
                TimepointType.Night -> TODO()
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