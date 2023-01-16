package de.htwBerlin.ai.mediAlarm

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.alarm.MedicineScheduler
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.calendar.CalendarItem
import de.htwBerlin.ai.mediAlarm.data.calendar.CalendarRequest
import de.htwBerlin.ai.mediAlarm.data.calendar.CalendarRequestProcessor
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao
import de.htwBerlin.ai.mediAlarm.data.userTime.UserTime
import de.htwBerlin.ai.mediAlarm.data.userTime.UserTimePreferences

class WebAppInterface internal constructor(c: Context) {
    private var mContext: MainActivity = c as MainActivity
    private val gson: Gson = Gson()

    private val medicineDao: MedicineDao = AppDatabase.getDatabase(c).medicineDao()
    private val userTimePreferences = UserTimePreferences(c)

    @JavascriptInterface
    fun showToast(msg: String) {
        Toast.makeText(mContext, "$msg", Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun navigateBackInApp() {
        mContext.onBackPressedBypassWebView()
    }

    @JavascriptInterface
    fun getIfNotificationsPermissionGiven(): Boolean {
        return mContext.getIfNotificationsPermissionGiven()
    }

    @JavascriptInterface
    fun getIfInternetPermissionGiven(): Boolean {
        return mContext.getIfInternetPermissionGiven()
    }

    @JavascriptInterface
    fun attemptRequestPermissions() {
        mContext.attemptRequestPermissions()
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
        medicine.id = medicineDao.insert(medicine)

        MedicineScheduler(mContext).schedule(medicine)
    }

    @JavascriptInterface
    fun updateMedicine(medicineJson: String) {
        val medicine = gson.fromJson(medicineJson, Medicine::class.java)
        medicineDao.update(medicine)
    }

    @JavascriptInterface
    fun getCalendarData(calendarRequestJson: String): String {
        val request = gson.fromJson(calendarRequestJson, CalendarRequest::class.java)
        return gson.toJson(CalendarRequestProcessor(mContext).process(request))
    }
}