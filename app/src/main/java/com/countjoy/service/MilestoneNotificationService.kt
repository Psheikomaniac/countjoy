package com.countjoy.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.countjoy.MainActivity
import com.countjoy.R
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.Milestone
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MilestoneNotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val MILESTONE_CHANNEL_ID = "milestone_notifications"
        const val MILESTONE_CHANNEL_NAME = "Milestone Achievements"
        const val MILESTONE_NOTIFICATION_BASE_ID = 10000
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MILESTONE_CHANNEL_ID,
                MILESTONE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for countdown milestone achievements"
                enableVibration(true)
                enableLights(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showMilestoneNotification(
        milestone: Milestone,
        event: CountdownEvent
    ) {
        if (!milestone.isNotificationEnabled) return
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("event_id", event.id)
            putExtra("milestone_id", milestone.id)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            milestone.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, MILESTONE_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
            .setContentTitle("🎯 ${milestone.title}")
            .setContentText("${event.title}: ${milestone.message}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 100, 500))
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(MILESTONE_NOTIFICATION_BASE_ID + milestone.id.hashCode(), notification)
        }
    }
    
    fun showMultipleMilestoneNotifications(
        milestones: List<Pair<Milestone, CountdownEvent>>
    ) {
        milestones.forEach { (milestone, event) ->
            showMilestoneNotification(milestone, event)
        }
    }
    
    fun cancelMilestoneNotification(milestoneId: String) {
        with(NotificationManagerCompat.from(context)) {
            cancel(MILESTONE_NOTIFICATION_BASE_ID + milestoneId.hashCode())
        }
    }
}