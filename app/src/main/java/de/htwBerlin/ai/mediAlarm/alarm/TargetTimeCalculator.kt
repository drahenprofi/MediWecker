package de.htwBerlin.ai.mediAlarm.alarm

import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import java.util.*

class TargetTimeCalculator {

    private val gson = Gson()

    fun calculate(medicine: Medicine): Long {
        val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)
        return calculate(rhythm)
    }

    private fun calculate(rhythm: Rhythm): Long {
        val calendar = Calendar.getInstance()

        val now = calendar.timeInMillis

        val currentDay = now - calendar[Calendar.HOUR_OF_DAY] * 60 * 60 * 1000 - calendar[Calendar.MINUTE] * 60 * 1000 - calendar[Calendar.SECOND] * 1000
        val currentTimeFromMidnight = (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * 60 * 1000

        var scheduledDayDifference = 0

        if (rhythm.intervalDays != null) {
            scheduledDayDifference = rhythm.intervalDays.days * 24 * 60 * 60 * 1000
        } else if (rhythm.specificDays != null) {
            scheduledDayDifference = getDayDifferenceBySpecificDaysData(rhythm, calendar, currentTimeFromMidnight)
        }

        // schedule next alarm for today
        if (scheduledDayDifference == 0) {
            // TODO: this will crash for all timePointTypes except TimePointType.AbsoluteTime
            for (timePoint in rhythm.timePoints) {
                val scheduledTimeFromMidnight = timePoint.absoluteTimeFromMidnight!! * 60 * 1000
                if (scheduledTimeFromMidnight > currentTimeFromMidnight) {
                    return currentDay + scheduledTimeFromMidnight
                }
            }
        }

        // all time points for current day expired
        // schedule earliest time point for next scheduled day or next day
        val earliestAbsoluteTimePoint = rhythm.timePoints
            .minByOrNull { x -> x.absoluteTimeFromMidnight!! }!!
            .absoluteTimeFromMidnight!!

        return if (scheduledDayDifference > 0) {
            currentDay + earliestAbsoluteTimePoint * 60 * 1000 + scheduledDayDifference
        } else {
            currentDay + earliestAbsoluteTimePoint * 60 * 1000 + 1000 * 60 * 60 * 24
        }
    }

    private fun getDayDifferenceBySpecificDaysData(rhythm: Rhythm, calendar: Calendar, currentTimeFromMidnight: Int): Int {
        if (rhythm.specificDays == null) return 0

        val absoluteTimePointAfterCurrentTime = rhythm.timePoints
            .any { timePoint -> timePoint.absoluteTimeFromMidnight!! * 60 * 1000 > currentTimeFromMidnight }

        val scheduledDayDifference = when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> {
                if (rhythm.specificDays.monday && absoluteTimePointAfterCurrentTime) {
                    0
                } else if (rhythm.specificDays.tuesday) {
                    24 * 60 * 60 * 1000
                } else if (rhythm.specificDays.wednesday) {
                    24 * 60 * 60 * 1000 * 2
                } else if (rhythm.specificDays.thursday) {
                    24 * 60 * 60 * 1000 * 3
                }else if (rhythm.specificDays.friday) {
                    24 * 60 * 60 * 1000 * 4
                }else if (rhythm.specificDays.saturday) {
                    24 * 60 * 60 * 1000 * 5
                } else if (rhythm.specificDays.sunday) {
                    24 * 60 * 60 * 1000 * 6
                } else {
                    24 * 60 * 60 * 1000 * 7
                }
            }
            Calendar.TUESDAY -> {
                if (rhythm.specificDays.tuesday && absoluteTimePointAfterCurrentTime) {
                    0
                } else if (rhythm.specificDays.wednesday) {
                    24 * 60 * 60 * 1000
                } else if (rhythm.specificDays.thursday) {
                    24 * 60 * 60 * 1000 * 2
                } else if (rhythm.specificDays.friday) {
                    24 * 60 * 60 * 1000 * 3
                }else if (rhythm.specificDays.saturday) {
                    24 * 60 * 60 * 1000 * 4
                }else if (rhythm.specificDays.sunday) {
                    24 * 60 * 60 * 1000 * 5
                } else if (rhythm.specificDays.monday) {
                    24 * 60 * 60 * 1000 * 6
                } else {
                    24 * 60 * 60 * 1000 * 7
                }
            }
            Calendar.WEDNESDAY -> {
                if (rhythm.specificDays.wednesday && absoluteTimePointAfterCurrentTime) {
                    0
                } else if (rhythm.specificDays.thursday) {
                    24 * 60 * 60 * 1000
                } else if (rhythm.specificDays.friday) {
                    24 * 60 * 60 * 1000 * 2
                } else if (rhythm.specificDays.saturday) {
                    24 * 60 * 60 * 1000 * 3
                }else if (rhythm.specificDays.sunday) {
                    24 * 60 * 60 * 1000 * 4
                }else if (rhythm.specificDays.monday) {
                    24 * 60 * 60 * 1000 * 5
                } else if (rhythm.specificDays.tuesday) {
                    24 * 60 * 60 * 1000 * 6
                } else {
                    24 * 60 * 60 * 1000 * 7
                }
            }
            Calendar.THURSDAY -> {
                if (rhythm.specificDays.thursday && absoluteTimePointAfterCurrentTime) {
                    0
                } else if (rhythm.specificDays.friday) {
                    24 * 60 * 60 * 1000
                } else if (rhythm.specificDays.saturday) {
                    24 * 60 * 60 * 1000 * 2
                } else if (rhythm.specificDays.sunday) {
                    24 * 60 * 60 * 1000 * 3
                }else if (rhythm.specificDays.monday) {
                    24 * 60 * 60 * 1000 * 4
                }else if (rhythm.specificDays.tuesday) {
                    24 * 60 * 60 * 1000 * 5
                } else if (rhythm.specificDays.wednesday) {
                    24 * 60 * 60 * 1000 * 6
                } else {
                    24 * 60 * 60 * 1000 * 7
                }
            }
            Calendar.FRIDAY -> {
                if (rhythm.specificDays.friday && absoluteTimePointAfterCurrentTime) {
                    0
                } else if (rhythm.specificDays.saturday) {
                    24 * 60 * 60 * 1000
                } else if (rhythm.specificDays.sunday) {
                    24 * 60 * 60 * 1000 * 2
                } else if (rhythm.specificDays.monday) {
                    24 * 60 * 60 * 1000 * 3
                }else if (rhythm.specificDays.tuesday) {
                    24 * 60 * 60 * 1000 * 4
                }else if (rhythm.specificDays.wednesday) {
                    24 * 60 * 60 * 1000 * 5
                } else if (rhythm.specificDays.thursday) {
                    24 * 60 * 60 * 1000 * 6
                } else {
                    24 * 60 * 60 * 1000 * 7
                }
            }
            Calendar.SATURDAY -> {
                if (rhythm.specificDays.saturday && absoluteTimePointAfterCurrentTime) {
                    0
                } else if (rhythm.specificDays.sunday) {
                    24 * 60 * 60 * 1000
                } else if (rhythm.specificDays.monday) {
                    24 * 60 * 60 * 1000 * 2
                } else if (rhythm.specificDays.tuesday) {
                    24 * 60 * 60 * 1000 * 3
                }else if (rhythm.specificDays.wednesday) {
                    24 * 60 * 60 * 1000 * 4
                }else if (rhythm.specificDays.thursday) {
                    24 * 60 * 60 * 1000 * 5
                } else if (rhythm.specificDays.friday) {
                    24 * 60 * 60 * 1000 * 6
                } else {
                    24 * 60 * 60 * 1000 * 7
                }
            }
            Calendar.SUNDAY -> {
                if (rhythm.specificDays.sunday && absoluteTimePointAfterCurrentTime) {
                    0
                } else if (rhythm.specificDays.monday) {
                    24 * 60 * 60 * 1000
                } else if (rhythm.specificDays.tuesday) {
                    24 * 60 * 60 * 1000 * 2
                } else if (rhythm.specificDays.wednesday) {
                    24 * 60 * 60 * 1000 * 3
                }else if (rhythm.specificDays.thursday) {
                    24 * 60 * 60 * 1000 * 4
                }else if (rhythm.specificDays.friday) {
                    24 * 60 * 60 * 1000 * 5
                } else if (rhythm.specificDays.saturday) {
                    24 * 60 * 60 * 1000 * 6
                } else {
                    24 * 60 * 60 * 1000 * 7
                }
            }
            else -> { 0 }
        }

        return scheduledDayDifference
    }
}