package de.htwBerlin.ai.mediAlarm.reminderPrompt

import android.content.Context
import de.htwBerlin.ai.mediAlarm.data.AppDatabase
import de.htwBerlin.ai.mediAlarm.notification.NotificationCanceller
import de.htwBerlin.ai.mediAlarm.reminderPrompt.data.ReminderPromptResponse
import de.htwBerlin.ai.mediAlarm.reminderPrompt.data.RescheduleSuggestion
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class ReminderPromptResponseHandler(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val alarmDao = database.alarmDao()

    private val notificationCanceller = NotificationCanceller(context)
    private val suggestionProvider = SuggestionProvider(context)

    fun handle(reminderPromptResponse: ReminderPromptResponse): List<RescheduleSuggestion> {
        val callable = Callable {
            val alarm = alarmDao.get(reminderPromptResponse.alarmId)

            if (alarm != null) {
                alarm.actualTimeUtc = reminderPromptResponse.actualTimeUtc
                alarm.userResponded = true

                notificationCanceller.cancel(alarm.notificationId)

                alarmDao.update(alarm)
            }

            return@Callable suggestionProvider.getSuggestion(alarm)
        }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future.get()
    }
}