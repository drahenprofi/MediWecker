package de.htwBerlin.ai.mediAlarm.reminderPrompt

import android.content.Context
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.reminderPrompt.data.ReminderPromptResponse
import java.util.concurrent.Executors

class ReminderPromptResponseHandler(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val alarmDao = database.alarmDao()

    fun handle(reminderPromptResponse: ReminderPromptResponse) {
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val alarm = alarmDao.get(reminderPromptResponse.alarmId)

            if (alarm != null) {
                alarm.actualTimeUtc = reminderPromptResponse.actualTimeUtc
                alarm.userResponded = true

                alarmDao.update(alarm)
            }
        }
    }
}