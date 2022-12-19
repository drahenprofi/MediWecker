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

class WebAppInterface internal constructor(c: Context) {
    private var mContext: MainActivity = c as MainActivity
    private val medicineDao: MedicineDao = AppDatabase.getDatabase(c).medicineDao()
    private val gson: Gson = Gson()

    @JavascriptInterface
    fun showToast(msg: String) {
        Toast.makeText(mContext, "$msg", Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun navigateBackInApp() {
        mContext.onBackPressedBypassWebView()
    }

    @JavascriptInterface
    fun getIfNotificationsPermissionGiven() : Boolean {
        return mContext.getIfNotificationsPermissionGiven()
    }

    @JavascriptInterface
    fun getIfInternetPermissionGiven() : Boolean {
        return mContext.getIfInternetPermissionGiven()
    }

    @JavascriptInterface
    fun attemptRequestPermissions() {
        mContext.attemptRequestPermissions()
    }

    @JavascriptInterface
    fun getAndResetPermissionsRequestCompleted() : Boolean {
        return mContext.getAndResetPermissionsRequestCompleted()
    }

    /*@JavascriptInterface
    fun getInitialSetupDone() : Boolean {
        return mContext.preferences.getBoolean("Setup.Done", false);
    }

    @JavascriptInterface()
    fun markInitialSetupAsDone() : Boolean {
        var editor = mContext.preferences.edit()
        editor.putBoolean("Setup.Done", true);
        editor.apply()
    }*/

    /**
     * Returns whether WakeUpTimeData was atleast once provided by the user and contains
     * user dictated values. If false, the user should be prompted to set them up.
     */
    @JavascriptInterface
    fun getWakeUpTimesInitialized() : Boolean {
        return mContext.preferences.getBoolean("WakeUpTimes.Initialized", false);
    }

    @JavascriptInterface
    fun getWakeUpTimeData() : String {
        return "{}";
    }

    @JavascriptInterface
    fun updateWakeUpTimes(wakeUpTimeDataJson: String) {
        Log.d("DEBUG", "updateWakeUpTimes: JSON = " + wakeUpTimeDataJson);

        // Save that we setup wake up times
        var editor = mContext.preferences.edit()
        editor.putBoolean("WakeUpTimes.Initialized", true);
        editor.apply()
    }

    @JavascriptInterface
    fun getMedicine(): String {
        Log.d("DEBUG", "getMedicine: Called!");

        val medicine = medicineDao.getAll()
        return gson.toJson(medicine)
    }

    @JavascriptInterface
    fun deleteMedicine(id: Int) {
        Log.d("DEBUG", "medicineJson: " + id);

        medicineDao.deleteById(id)
    }

    @JavascriptInterface
    fun insertMedicine(medicineJson: String) {
        Log.d("DEBUG", "insertMedicine: " + medicineJson);

        val medicine = gson.fromJson(medicineJson, Medicine::class.java)

        medicine.id = medicineDao.insert(medicine)

        MedicineScheduler(mContext).schedule(medicine)
    }

    @JavascriptInterface
    fun updateMedicine(medicineJson: String) {
        Log.d("DEBUG", "medicineJson: $medicineJson");

        val medicine = gson.fromJson(medicineJson, Medicine::class.java)
        medicineDao.update(medicine)
    }
}