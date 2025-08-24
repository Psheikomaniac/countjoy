package com.countjoy.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.countjoy.domain.model.NotificationChannelType
import com.countjoy.domain.model.NotificationConfig
import com.countjoy.domain.model.NotificationType
import com.countjoy.domain.repository.EventRepository
import com.countjoy.service.SmartNotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker to handle snoozed reminder notifications
 */
@HiltWorker
class SnoozeReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val eventRepository: EventRepository,
    private val smartNotificationService: SmartNotificationService
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val eventId = inputData.getLong("event_id", -1)
            val snoozeMinutes = inputData.getInt("snooze_minutes", 60)
            
            if (eventId == -1L) {
                return@withContext Result.failure()
            }
            
            // Get the event
            val event = eventRepository.getEventById(eventId.toString())
                ?: return@withContext Result.failure()
            
            // Create notification config for snoozed reminder
            val config = NotificationConfig(
                eventId = eventId,
                channelType = NotificationChannelType.REMINDERS,
                enableQuickActions = true,
                snoozeOptions = listOf(60, 180, 1440) // 1h, 3h, 1 day
            )
            
            // Show the snoozed reminder notification
            smartNotificationService.showSmartNotification(
                event = event,
                config = config,
                notificationType = NotificationType.REMINDER
            )
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}