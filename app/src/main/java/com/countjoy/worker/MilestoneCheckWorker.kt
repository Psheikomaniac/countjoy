package com.countjoy.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.countjoy.domain.repository.EventRepository
import com.countjoy.domain.usecase.milestone.CheckMilestonesUseCase
import com.countjoy.service.MilestoneNotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class MilestoneCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val eventRepository: EventRepository,
    private val checkMilestonesUseCase: CheckMilestonesUseCase,
    private val notificationService: MilestoneNotificationService
) : CoroutineWorker(context, params) {
    
    companion object {
        const val WORK_NAME = "milestone_check_work"
        
        fun schedulePeriodicWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
            
            val workRequest = PeriodicWorkRequestBuilder<MilestoneCheckWorker>(
                repeatInterval = 6,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()
            
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
        }
        
        fun scheduleOneTimeWork(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<MilestoneCheckWorker>()
                .build()
            
            WorkManager.getInstance(context)
                .enqueue(workRequest)
        }
    }
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Get all active events
            val events = eventRepository.getAllEvents().first()
            
            // Check milestones for each event
            events.forEach { event ->
                val achievedMilestones = checkMilestonesUseCase(event.id)
                
                // Send notifications for achieved milestones
                achievedMilestones.forEach { achievement ->
                    notificationService.showMilestoneNotification(
                        milestone = achievement.milestone,
                        event = achievement.event
                    )
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}