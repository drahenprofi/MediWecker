package de.htwBerlin.ai.mediAlarm

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import java.util.concurrent.Executors

class NotificationClickActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_click)

        val database = AppDatabase.getDatabase(this)
        val medicineDao = database.medicineDao()

        val medicineId = intent.getLongExtra("MEDICINE_ID", 0)
        val scheduledTimeUtc = intent.getLongExtra("SCHEDULED_TIME_UTC", 0)

        if (medicineId > 0) {
            val notificationManager = this
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.cancel(medicineId.toInt())

            val executor = Executors.newSingleThreadExecutor()

            executor.execute {
                val medicine = medicineDao.get(medicineId)

                Log.d("DEBUG", "Clicked on notification for $medicine, scheduled $scheduledTimeUtc")
                TODO("Ryan: use medicine and scheduledTimeUtc in UI, display notification reaction possibilities")
            }
        } else {
            Log.d("DEBUG", "Medicine Id is 0")
        }
    }
}