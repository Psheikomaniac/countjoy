package com.countjoy.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.countjoy.domain.model.*
import com.countjoy.domain.repository.EventRepository
import com.countjoy.domain.usecase.milestone.CheckMilestonesUseCase
import com.countjoy.service.SmartNotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

/**
 * Enhanced milestone check worker with smart notifications
 */
@HiltWorker
class EnhancedMilestoneCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val eventRepository: EventRepository,
    private val checkMilestonesUseCase: CheckMilestonesUseCase,
    private val smartNotificationService: SmartNotificationService
) : CoroutineWorker(context, params) {
    
    companion object {
        const val WORK_NAME = "enhanced_milestone_check_work"
        const val URGENT_CHECK_WORK = "urgent_milestone_check"
        
        fun schedulePeriodicWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
            
            // Regular check every 4 hours
            val regularWorkRequest = PeriodicWorkRequestBuilder<EnhancedMilestoneCheckWorker>(
                repeatInterval = 4,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .addTag("milestone_regular")
                .build()
            
            // More frequent check for urgent events (every hour)
            val urgentWorkRequest = PeriodicWorkRequestBuilder<EnhancedMilestoneCheckWorker>(
                repeatInterval = 1,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .addTag("milestone_urgent")
                .setInputData(workDataOf("check_urgent" to true))
                .build()
            
            val workManager = WorkManager.getInstance(context)
            
            // Enqueue regular checks
            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                regularWorkRequest
            )
            
            // Enqueue urgent checks
            workManager.enqueueUniquePeriodicWork(
                URGENT_CHECK_WORK,
                ExistingPeriodicWorkPolicy.UPDATE,
                urgentWorkRequest
            )
        }
        
        fun scheduleAdaptiveWork(context: Context, event: CountdownEvent) {
            val now = LocalDateTime.now()
            val hoursUntil = ChronoUnit.HOURS.between(now, event.targetDateTime)
            
            // Schedule more frequent checks as event approaches
            val delay = when {
                hoursUntil <= 24 -> 1L // Check every hour in last 24 hours
                hoursUntil <= 72 -> 3L // Check every 3 hours in last 3 days
                hoursUntil <= 168 -> 6L // Check every 6 hours in last week
                else -> 12L // Check every 12 hours otherwise
            }
            
            val workRequest = OneTimeWorkRequestBuilder<EnhancedMilestoneCheckWorker>()
                .setInitialDelay(delay, TimeUnit.HOURS)
                .setInputData(workDataOf("event_id" to event.id))
                .addTag("adaptive_check_${event.id}")
                .build()
            
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val checkUrgentOnly = inputData.getBoolean("check_urgent", false)
            val specificEventId = inputData.getLong("event_id", -1)
            
            // Get events to check
            val events = if (specificEventId != -1L) {
                listOfNotNull(eventRepository.getEventById(specificEventId.toString()))
            } else {
                eventRepository.getAllEvents().first()
            }
            
            val notificationBundles = mutableListOf<NotificationBundle>()
            
            // Process each event
            events.forEach { event ->
                // Skip if checking urgent only and event is not high priority
                if (checkUrgentOnly && event.priority < 2) {
                    return@forEach
                }
                
                // Check standard milestones
                val achievements = checkMilestonesUseCase(event.id.toString())
                achievements.forEach { achievement ->
                    notificationBundles.add(
                        NotificationBundle(
                            event = achievement.event,
                            type = NotificationType.MILESTONE,
                            milestone = achievement.milestone
                        )
                    )
                }
                
                // Check percentage-based milestones
                checkPercentageMilestones(event)?.let { milestone ->
                    notificationBundles.add(
                        NotificationBundle(
                            event = event,
                            type = NotificationType.MILESTONE,
                            milestone = milestone
                        )
                    )
                }
                
                // Check hour-based milestones for imminent events
                checkHourBasedMilestones(event)?.let { milestone ->
                    notificationBundles.add(
                        NotificationBundle(
                            event = event,
                            type = if (event.priority >= 2) NotificationType.URGENT else NotificationType.REMINDER,
                            milestone = milestone
                        )
                    )
                }
                
                // Schedule adaptive work for this event
                scheduleAdaptiveWork(applicationContext, event)
            }
            
            // Send bundled notifications if any
            if (notificationBundles.isNotEmpty()) {
                val config = NotificationConfig(
                    eventId = 0, // Generic config
                    bundleNotifications = true,
                    maxBundleSize = 5,
                    enableQuickActions = true
                )
                
                smartNotificationService.showBundledNotifications(
                    notifications = notificationBundles,
                    config = config
                )
            }
            
            // Schedule AI-suggested reminders
            events.forEach { event ->
                val suggestedTimes = smartNotificationService.getSuggestedReminderTimes(event)
                scheduleSuggestedReminders(event, suggestedTimes)
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
    
    private fun checkPercentageMilestones(event: CountdownEvent): Milestone? {
        val now = LocalDateTime.now()
        val totalDuration = ChronoUnit.MINUTES.between(event.createdAt, event.targetDateTime)
        val elapsed = ChronoUnit.MINUTES.between(event.createdAt, now)
        val percentComplete = ((elapsed.toDouble() / totalDuration) * 100).toInt()
        
        // Check for 50%, 75%, 90% milestones
        return when {
            percentComplete in 48..52 -> Milestone(
                id = "percent_50_${event.id}",
                eventId = event.id.toString(),
                title = "Halfway There!",
                message = "You're 50% through the countdown to ${event.title}",
                triggerDays = -1,
                isAchieved = true,
                achievedAt = now,
                isNotificationEnabled = true
            )
            percentComplete in 73..77 -> Milestone(
                id = "percent_75_${event.id}",
                eventId = event.id.toString(),
                title = "Three Quarters Done!",
                message = "75% complete - ${event.title} is approaching",
                triggerDays = -1,
                isAchieved = true,
                achievedAt = now,
                isNotificationEnabled = true
            )
            percentComplete in 88..92 -> Milestone(
                id = "percent_90_${event.id}",
                eventId = event.id.toString(),
                title = "Final Stretch!",
                message = "90% there - get ready for ${event.title}",
                triggerDays = -1,
                isAchieved = true,
                achievedAt = now,
                isNotificationEnabled = true
            )
            else -> null
        }
    }
    
    private fun checkHourBasedMilestones(event: CountdownEvent): Milestone? {
        val hoursRemaining = ChronoUnit.HOURS.between(LocalDateTime.now(), event.targetDateTime)
        
        return when (hoursRemaining) {
            24L -> Milestone(
                id = "hour_24_${event.id}",
                eventId = event.id.toString(),
                title = "24 Hours Remaining!",
                message = "One day until ${event.title}",
                triggerDays = -1,
                isAchieved = true,
                achievedAt = LocalDateTime.now(),
                isNotificationEnabled = true
            )
            12L -> Milestone(
                id = "hour_12_${event.id}",
                eventId = event.id.toString(),
                title = "12 Hours to Go!",
                message = "Half a day until ${event.title}",
                triggerDays = -1,
                isAchieved = true,
                achievedAt = LocalDateTime.now(),
                isNotificationEnabled = true
            )
            6L -> Milestone(
                id = "hour_6_${event.id}",
                eventId = event.id.toString(),
                title = "6 Hours Left!",
                message = "${event.title} is almost here",
                triggerDays = -1,
                isAchieved = true,
                achievedAt = LocalDateTime.now(),
                isNotificationEnabled = true
            )
            3L -> Milestone(
                id = "hour_3_${event.id}",
                eventId = event.id.toString(),
                title = "3 Hour Warning!",
                message = "Get ready for ${event.title}",
                triggerDays = -1,
                isAchieved = true,
                achievedAt = LocalDateTime.now(),
                isNotificationEnabled = true
            )
            1L -> Milestone(
                id = "hour_1_${event.id}",
                eventId = event.id.toString(),
                title = "Final Hour!",
                message = "${event.title} starts in 1 hour",
                triggerDays = -1,
                isAchieved = true,
                achievedAt = LocalDateTime.now(),
                isNotificationEnabled = true
            )
            else -> null
        }
    }
    
    private fun scheduleSuggestedReminders(event: CountdownEvent, suggestedTimes: List<LocalDateTime>) {
        suggestedTimes.forEach { reminderTime ->
            val delay = ChronoUnit.MINUTES.between(LocalDateTime.now(), reminderTime)
            
            if (delay > 0) {
                val workRequest = OneTimeWorkRequestBuilder<SnoozeReminderWorker>()
                    .setInitialDelay(delay, TimeUnit.MINUTES)
                    .setInputData(
                        workDataOf(
                            "event_id" to event.id,
                            "is_suggested" to true
                        )
                    )
                    .addTag("suggested_reminder_${event.id}")
                    .build()
                
                WorkManager.getInstance(applicationContext).enqueue(workRequest)
            }
        }
    }
}