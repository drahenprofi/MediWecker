package de.htwBerlin.ai.mediAlarm

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.room.Room
import com.google.gson.Gson
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
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
        //Toast.makeText(mContext, "$msg", Toast.LENGTH_SHORT).show()

        //mContext.setAlarm()

        //medicineDao.insertAll(Medicine("Iboprofen", 800f, ""))

        return getMedicine()
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
        medicineDao.insertAll(medicine)
    }

    @JavascriptInterface
    fun updateMedicine(medicineJson: String) {
        Log.d("DEBUG", "medicineJson: " + medicineJson);

        val medicine = gson.fromJson(medicineJson, Medicine::class.java)
        medicineDao.update(medicine)
    }
}