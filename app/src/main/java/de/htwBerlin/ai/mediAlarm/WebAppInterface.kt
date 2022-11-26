package de.htwBerlin.ai.mediAlarm

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.room.Room
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.alarm.AlarmScheduler
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.data.alarm.Alarm
import de.htwBerlin.ai.mediAlarm.data.medicine.Medicine
import de.htwBerlin.ai.mediAlarm.data.medicine.MedicineDao

class WebAppInterface internal constructor(c: Context) {
    private var mContext: MainActivity
    private val medicineDao: MedicineDao
    private val gson: Gson

    init {
        mContext = c as MainActivity

        medicineDao = Room
            .databaseBuilder(
                mContext,
                AppDatabase::class.java, "medi-wecker-database"
            )
            .build()
            .medicineDao()

        gson = Gson()
    }

    @JavascriptInterface
    fun showToast(msg: String): String {
        //medicineDao.insertAll(Medicine("Iboprofen", 800f, ""))

        val alarm = Alarm("TODO", 0)
        AlarmScheduler(mContext).schedule(alarm)

        return getMedicine()
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
        medicineDao.insertAll(medicine)
    }

    @JavascriptInterface
    fun updateMedicine(medicineJson: String) {
        val medicine = gson.fromJson(medicineJson, Medicine::class.java)
        medicineDao.update(medicine)
    }
}