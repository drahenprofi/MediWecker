package de.htwBerlin.ai.mediAlarm

import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.alarm.MedicineRescheduler
import de.htwBerlin.ai.mediAlarm.alarm.MedicineScheduler
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.reminderPrompt.data.ReminderPromptResponse
import de.htwBerlin.ai.mediAlarm.calendar.data.CalendarRequest
import de.htwBerlin.ai.mediAlarm.calendar.CalendarRequestProcessor
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import de.htwBerlin.ai.mediAlarm.data.userTime.UserTime
import de.htwBerlin.ai.mediAlarm.data.userTime.UserTimePreferences
import de.htwBerlin.ai.mediAlarm.reminderPrompt.ReminderPromptResponseHandler
import de.htwBerlin.ai.mediAlarm.reminderPrompt.RescheduleSuggestion
import de.htwBerlin.ai.mediAlarm.reminderPrompt.Rescheduler
import java.util.*

class WebAppInterface internal constructor(c: Context) {
    private var mContext: MainActivity = c as MainActivity
    private val gson: Gson = Gson()

    private val medicineDao: MedicineDao = AppDatabase.getDatabase(c).medicineDao()
    private val userTimePreferences = UserTimePreferences(c)

    @JavascriptInterface
    fun showToast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun navigateBackInApp() {
        mContext.onBackPressedBypassWebView()
    }

    @JavascriptInterface
    fun getIfNotificationsPermissionGiven(): Boolean {
        return PermissionManager(mContext).notificationPermissionGiven()
    }

    @JavascriptInterface
    fun getIfInternetPermissionGiven(): Boolean {
        return true
    }

    @JavascriptInterface
    fun attemptRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionManager(mContext).requestNotificationPermission()
        }
    }

    @JavascriptInterface
    fun getAndResetPermissionsRequestCompleted(): Boolean {
        return mContext.getAndResetPermissionsRequestCompleted()
    }

    @JavascriptInterface
    fun getUserTimesInitialized(): Boolean {
        return userTimePreferences.isInitialized()
    }

    @JavascriptInterface
    fun getUserTimesData(): String {
        return gson.toJson(userTimePreferences.get())
    }

    @JavascriptInterface
    fun updateUserTimesData(userTimeDataJson: String) {
        val userTimeData = gson.fromJson(userTimeDataJson, UserTime::class.java)
        userTimePreferences.set(userTimeData)
    }

    @JavascriptInterface
    fun userTimesInitialized() {
        userTimePreferences.setInitialized()
    }

    @JavascriptInterface
    fun submitReminderPromptResponse(responseJson: String): String {
        val response = gson.fromJson(responseJson, ReminderPromptResponse::class.java)
        val rescheduleSuggestions = ReminderPromptResponseHandler(mContext).handle(response)
        return gson.toJson(rescheduleSuggestions)
    }

    @JavascriptInterface
    fun confirmRescheduleSuggestion(suggestionJson: String) {
        val suggestion = gson.fromJson(suggestionJson, RescheduleSuggestion::class.java)
        Rescheduler(mContext).reschedule(suggestion)
    }

    @JavascriptInterface
    fun getMedicine(): String {
        val medicine = medicineDao.getAll()
        return gson.toJson(medicine)
    }

    @JavascriptInterface
    fun deleteMedicine(id: Int) {
        medicineDao.deleteById(id)
    }

    @JavascriptInterface
    fun insertMedicine(medicineJson: String) {
        val medicine = gson.fromJson(medicineJson, Medicine::class.java)

        val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)
        rhythm.timePoints.map { it.uuid = UUID.randomUUID() }
        medicine.rhythm = gson.toJson(rhythm)

        medicine.id = medicineDao.insert(medicine)

        MedicineScheduler(mContext).schedule(medicine)
    }

    @JavascriptInterface
    fun updateMedicine(medicineJson: String) {
        val medicine = gson.fromJson(medicineJson, Medicine::class.java)

        val rhythm = gson.fromJson(medicine.rhythm, Rhythm::class.java)
        rhythm.timePoints.map { it.uuid = UUID.randomUUID() }
        medicine.rhythm = gson.toJson(rhythm)

        medicineDao.update(medicine)

        MedicineRescheduler(mContext).reschedule(medicine)
    }

    @JavascriptInterface
    fun getCalendarData(calendarRequestJson: String): String {
        val request = gson.fromJson(calendarRequestJson, CalendarRequest::class.java)
        val calendarItems = CalendarRequestProcessor(mContext).process(request)
        return gson.toJson(calendarItems)
    }
}