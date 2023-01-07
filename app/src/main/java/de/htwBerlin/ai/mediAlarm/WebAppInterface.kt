package de.htwBerlin.ai.mediAlarm

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.alarm.MedicineScheduler
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao
import de.htwBerlin.ai.mediAlarm.data.wakeUpTime.WakeUpTime
import de.htwBerlin.ai.mediAlarm.data.wakeUpTime.WakeUpTimePreferences

class WebAppInterface internal constructor(c: Context) {
    private var mContext: MainActivity = c as MainActivity
    private val gson: Gson = Gson()

    private val medicineDao: MedicineDao = AppDatabase.getDatabase(c).medicineDao()
    private val wakeUpTimePreferences = WakeUpTimePreferences(c)

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
    fun getWakeUpTimesInitialized(): Boolean {
        return wakeUpTimePreferences.isInitialized()
    }

    @JavascriptInterface
    fun getWakeUpTimeData(): String {
        return gson.toJson(wakeUpTimePreferences.get())
    }

    @JavascriptInterface
    fun updateWakeUpTimes(wakeUpTimeDataJson: String) {
        val wakeUpTimeData = gson.fromJson(wakeUpTimeDataJson, WakeUpTime::class.java)
        wakeUpTimePreferences.set(wakeUpTimeData)
        Log.d("DEBUG", "updateWakeUpTimes: JSON = " + wakeUpTimeDataJson);
    }

    @JavascriptInterface
    fun userTimesSetupInitialized() {
        // Save that we setup wake up times
        var editor = mContext.preferences.edit()
        editor.putBoolean("WakeUpTimes.Initialized", true);
        editor.apply()
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
}