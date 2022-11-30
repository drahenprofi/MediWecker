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
import de.htwBerlin.ai.mediAlarm.data.rhythm.IntervalDaysData
import de.htwBerlin.ai.mediAlarm.data.rhythm.Rhythm
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimePoint
import de.htwBerlin.ai.mediAlarm.data.rhythm.TimepointType

class WebAppInterface internal constructor(c: Context) {
    private var mContext: MainActivity = c as MainActivity
    private val medicineDao: MedicineDao = AppDatabase.getDatabase(c).medicineDao()
    private val gson: Gson = Gson()

    @JavascriptInterface
    fun showToast(msg: String) {
        Toast.makeText(mContext, "$msg", Toast.LENGTH_SHORT).show()
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

        val rhythm = Rhythm(
            IntervalDaysData(days = 1),
            null,
            listOf(
                TimePoint(
                    TimepointType.AbsoluteTime,
                    (14 * 60 + 56) * 60 * 1000
                )
            )
        )

        medicine.rhythm = gson.toJson(rhythm)
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