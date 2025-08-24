package com.countjoy.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ShareCompat
import com.countjoy.MainActivity
import com.countjoy.R
import com.countjoy.domain.model.*
import com.countjoy.receiver.NotificationActionReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Enhanced notification service with smart features for Issue #46
 */
@Singleton
class SmartNotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        // Notification channel IDs
        const val CHANNEL_URGENT = "urgent_notifications"
        const val CHANNEL_MILESTONES = "milestone_notifications"
        const val CHANNEL_REMINDERS = "reminder_notifications"
        const val CHANNEL_SUMMARY = "summary_notifications"
        const val CHANNEL_SILENT = "silent_notifications"
        
        // Notification action codes
        const val ACTION_SNOOZE = "com.countjoy.action.SNOOZE"
        const val ACTION_SHARE = "com.countjoy.action.SHARE"
        const val ACTION_QUICK_VIEW = "com.countjoy.action.QUICK_VIEW"
        const val ACTION_MARK_SEEN = "com.countjoy.action.MARK_SEEN"
        
        // Extra keys
        const val EXTRA_EVENT_ID = "event_id"
        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val EXTRA_SNOOZE_MINUTES = "snooze_minutes"
        
        // Base notification IDs
        const val NOTIFICATION_BASE_ID = 10000
        const val SUMMARY_NOTIFICATION_ID = 99999
    }
    
    init {
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Urgent channel - highest priority
            val urgentChannel = NotificationChannel(
                CHANNEL_URGENT,
                "Urgent Events",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High priority events that always notify"
                enableVibration(true)
                enableLights(true)
                lightColor = Color.RED
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), 
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build())
                setBypassDnd(true)
            }
            
            // Milestones channel - standard priority
            val milestonesChannel = NotificationChannel(
                CHANNEL_MILESTONES,
                "Milestone Achievements",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Standard milestone alerts"
                enableVibration(true)
                enableLights(true)
                lightColor = Color.YELLOW
            }
            
            // Reminders channel - medium priority
            val remindersChannel = NotificationChannel(
                CHANNEL_REMINDERS,
                "Custom Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Custom user reminders"
                enableVibration(true)
                enableLights(true)
                lightColor = Color.BLUE
            }
            
            // Summary channel - low priority
            val summaryChannel = NotificationChannel(
                CHANNEL_SUMMARY,
                "Daily Summaries",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Daily and weekly event summaries"
                enableVibration(false)
                enableLights(false)
            }
            
            // Silent channel - no sound/vibration
            val silentChannel = NotificationChannel(
                CHANNEL_SILENT,
                "Silent Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Visual only notifications"
                enableVibration(false)
                enableLights(false)
                setSound(null, null)
            }
            
            notificationManager.createNotificationChannels(listOf(
                urgentChannel,
                milestonesChannel,
                remindersChannel,
                summaryChannel,
                silentChannel
            ))
        }
    }
    
    /**
     * Shows a smart notification with adaptive features
     */
    fun showSmartNotification(
        event: CountdownEvent,
        config: NotificationConfig,
        notificationType: NotificationType,
        milestone: Milestone? = null
    ) {
        // Check quiet hours
        if (!shouldNotifyDuringQuietHours(config, notificationType)) {
            return
        }
        
        val channelId = getChannelForType(config.channelType)
        val notificationId = generateNotificationId(event, milestone)
        
        val notification = buildNotification(
            event = event,
            config = config,
            channelId = channelId,
            notificationType = notificationType,
            milestone = milestone
        )
        
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }
    
    /**
     * Shows bundled notifications for multiple events
     */
    fun showBundledNotifications(
        notifications: List<NotificationBundle>,
        config: NotificationConfig
    ) {
        if (notifications.isEmpty()) return
        
        if (config.bundleNotifications && notifications.size > 1) {
            // Create summary notification
            val summaryNotification = buildSummaryNotification(notifications)
            
            with(NotificationManagerCompat.from(context)) {
                // Show individual notifications
                notifications.take(config.maxBundleSize).forEach { bundle ->
                    notify(generateNotificationId(bundle.event, bundle.milestone), 
                        buildNotification(
                            event = bundle.event,
                            config = config,
                            channelId = CHANNEL_SUMMARY,
                            notificationType = bundle.type,
                            milestone = bundle.milestone,
                            groupKey = "event_summary"
                        ))
                }
                
                // Show summary
                notify(SUMMARY_NOTIFICATION_ID, summaryNotification)
            }
        } else {
            // Show individual notifications
            notifications.forEach { bundle ->
                showSmartNotification(
                    event = bundle.event,
                    config = config,
                    notificationType = bundle.type,
                    milestone = bundle.milestone
                )
            }
        }
    }
    
    /**
     * AI-suggested notification times based on event type and user patterns
     */
    fun getSuggestedReminderTimes(event: CountdownEvent): List<LocalDateTime> {
        val suggestions = mutableListOf<LocalDateTime>()
        val now = LocalDateTime.now()
        val targetDate = event.targetDateTime
        
        // Calculate time until event
        val daysUntil = ChronoUnit.DAYS.between(now, targetDate)
        
        // Smart suggestions based on event category
        when (event.category) {
            "Birthday", "Anniversary" -> {
                // Remind 1 week, 3 days, and 1 day before
                if (daysUntil >= 7) suggestions.add(targetDate.minusDays(7).withHour(9).withMinute(0))
                if (daysUntil >= 3) suggestions.add(targetDate.minusDays(3).withHour(18).withMinute(0))
                if (daysUntil >= 1) suggestions.add(targetDate.minusDays(1).withHour(10).withMinute(0))
            }
            "Deadline", "Meeting" -> {
                // More aggressive reminders for work items
                if (daysUntil >= 14) suggestions.add(targetDate.minusDays(14).withHour(9).withMinute(0))
                if (daysUntil >= 7) suggestions.add(targetDate.minusDays(7).withHour(9).withMinute(0))
                if (daysUntil >= 3) suggestions.add(targetDate.minusDays(3).withHour(14).withMinute(0))
                if (daysUntil >= 1) suggestions.add(targetDate.minusDays(1).withHour(16).withMinute(0))
                if (daysUntil >= 0) suggestions.add(targetDate.minusHours(3))
            }
            "Holiday", "Travel" -> {
                // Longer lead time for planning
                if (daysUntil >= 30) suggestions.add(targetDate.minusDays(30).withHour(19).withMinute(0))
                if (daysUntil >= 14) suggestions.add(targetDate.minusDays(14).withHour(19).withMinute(0))
                if (daysUntil >= 7) suggestions.add(targetDate.minusDays(7).withHour(20).withMinute(0))
            }
            else -> {
                // Default pattern
                if (daysUntil >= 7) suggestions.add(targetDate.minusDays(7).withHour(10).withMinute(0))
                if (daysUntil >= 3) suggestions.add(targetDate.minusDays(3).withHour(10).withMinute(0))
                if (daysUntil >= 1) suggestions.add(targetDate.minusDays(1).withHour(10).withMinute(0))
            }
        }
        
        // Filter out past times
        return suggestions.filter { it.isAfter(now) }
    }
    
    private fun buildNotification(
        event: CountdownEvent,
        config: NotificationConfig,
        channelId: String,
        notificationType: NotificationType,
        milestone: Milestone? = null,
        groupKey: String? = null
    ): Notification {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(getNotificationIcon(notificationType))
            .setContentTitle(getNotificationTitle(event, notificationType, milestone))
            .setContentText(getNotificationContent(event, notificationType, milestone))
            .setPriority(getPriority(config.channelType))
            .setAutoCancel(true)
            .setContentIntent(createContentIntent(event))
        
        // Apply custom configuration
        if (config.enableVibration) {
            builder.setVibrate(config.vibrationPattern)
        }
        
        if (config.enableLed) {
            builder.setLights(config.ledColor ?: Color.WHITE, 1000, 1000)
        }
        
        if (config.customSoundUri != null && config.enableSound) {
            builder.setSound(Uri.parse(config.customSoundUri))
        }
        
        // Add quick actions
        if (config.enableQuickActions) {
            addNotificationActions(builder, event, config)
        }
        
        // Set group for bundling
        if (groupKey != null) {
            builder.setGroup(groupKey)
        }
        
        return builder.build()
    }
    
    private fun buildSummaryNotification(notifications: List<NotificationBundle>): Notification {
        val inboxStyle = NotificationCompat.InboxStyle()
            .setBigContentTitle("${notifications.size} Event Updates")
        
        notifications.take(5).forEach { bundle ->
            inboxStyle.addLine("${bundle.event.title}: ${getShortDescription(bundle)}")
        }
        
        if (notifications.size > 5) {
            inboxStyle.setSummaryText("+${notifications.size - 5} more")
        }
        
        return NotificationCompat.Builder(context, CHANNEL_SUMMARY)
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
            .setContentTitle("Event Updates")
            .setContentText("You have ${notifications.size} event updates")
            .setStyle(inboxStyle)
            .setGroup("event_summary")
            .setGroupSummary(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
    }
    
    private fun addNotificationActions(
        builder: NotificationCompat.Builder,
        event: CountdownEvent,
        config: NotificationConfig
    ) {
        // Snooze action
        config.snoozeOptions.firstOrNull()?.let { defaultSnooze ->
            val snoozeIntent = Intent(context, NotificationActionReceiver::class.java).apply {
                action = ACTION_SNOOZE
                putExtra(EXTRA_EVENT_ID, event.id)
                putExtra(EXTRA_SNOOZE_MINUTES, defaultSnooze)
            }
            val snoozePendingIntent = PendingIntent.getBroadcast(
                context, 
                event.id.toInt(), 
                snoozeIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(
                android.R.drawable.ic_menu_recent_history,
                "Snooze ${defaultSnooze / 60}h",
                snoozePendingIntent
            )
        }
        
        // Share action
        if (config.enableShare) {
            val shareIntent = Intent(context, NotificationActionReceiver::class.java).apply {
                action = ACTION_SHARE
                putExtra(EXTRA_EVENT_ID, event.id)
            }
            val sharePendingIntent = PendingIntent.getBroadcast(
                context,
                event.id.toInt() + 1000,
                shareIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(
                android.R.drawable.ic_menu_share,
                "Share",
                sharePendingIntent
            )
        }
        
        // Quick view action
        if (config.enableQuickView) {
            val quickViewIntent = Intent(context, NotificationActionReceiver::class.java).apply {
                action = ACTION_QUICK_VIEW
                putExtra(EXTRA_EVENT_ID, event.id)
            }
            val quickViewPendingIntent = PendingIntent.getBroadcast(
                context,
                event.id.toInt() + 2000,
                quickViewIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(
                android.R.drawable.ic_menu_view,
                "View",
                quickViewPendingIntent
            )
        }
    }
    
    private fun createContentIntent(event: CountdownEvent): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_EVENT_ID, event.id)
        }
        
        return PendingIntent.getActivity(
            context,
            event.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun shouldNotifyDuringQuietHours(
        config: NotificationConfig,
        type: NotificationType
    ): Boolean {
        if (!config.quietHoursEnabled) return true
        
        // Always notify for urgent events if override is enabled
        if (config.overrideQuietHoursForUrgent && 
            config.channelType == NotificationChannelType.URGENT) {
            return true
        }
        
        val now = LocalTime.now()
        val start = config.quietHoursStart ?: return true
        val end = config.quietHoursEnd ?: return true
        
        // Handle quiet hours that span midnight
        return if (start < end) {
            now < start || now > end
        } else {
            now < start && now > end
        }
    }
    
    private fun getChannelForType(type: NotificationChannelType): String {
        return when (type) {
            NotificationChannelType.URGENT -> CHANNEL_URGENT
            NotificationChannelType.MILESTONES -> CHANNEL_MILESTONES
            NotificationChannelType.REMINDERS -> CHANNEL_REMINDERS
            NotificationChannelType.SUMMARY -> CHANNEL_SUMMARY
            NotificationChannelType.SILENT -> CHANNEL_SILENT
        }
    }
    
    private fun getPriority(type: NotificationChannelType): Int {
        return when (type) {
            NotificationChannelType.URGENT -> NotificationCompat.PRIORITY_HIGH
            NotificationChannelType.MILESTONES -> NotificationCompat.PRIORITY_DEFAULT
            NotificationChannelType.REMINDERS -> NotificationCompat.PRIORITY_DEFAULT
            NotificationChannelType.SUMMARY -> NotificationCompat.PRIORITY_LOW
            NotificationChannelType.SILENT -> NotificationCompat.PRIORITY_MIN
        }
    }
    
    private fun getNotificationIcon(type: NotificationType): Int {
        return when (type) {
            NotificationType.MILESTONE -> android.R.drawable.star_on
            NotificationType.REMINDER -> android.R.drawable.ic_menu_my_calendar
            NotificationType.URGENT -> android.R.drawable.ic_dialog_alert
            NotificationType.SUMMARY -> android.R.drawable.ic_menu_agenda
        }
    }
    
    private fun getNotificationTitle(
        event: CountdownEvent,
        type: NotificationType,
        milestone: Milestone?
    ): String {
        return when (type) {
            NotificationType.MILESTONE -> "🎯 ${milestone?.title ?: "Milestone Reached"}"
            NotificationType.REMINDER -> "⏰ Reminder: ${event.title}"
            NotificationType.URGENT -> "🚨 Urgent: ${event.title}"
            NotificationType.SUMMARY -> "📋 Event Summary"
        }
    }
    
    private fun getNotificationContent(
        event: CountdownEvent,
        type: NotificationType,
        milestone: Milestone?
    ): String {
        val daysRemaining = event.getDaysRemaining()
        return when (type) {
            NotificationType.MILESTONE -> "${event.title}: ${milestone?.message ?: ""}. $daysRemaining days remaining"
            NotificationType.REMINDER -> "$daysRemaining days until ${event.title}"
            NotificationType.URGENT -> "Only $daysRemaining days left!"
            NotificationType.SUMMARY -> "Check your upcoming events"
        }
    }
    
    private fun getShortDescription(bundle: NotificationBundle): String {
        return when (bundle.type) {
            NotificationType.MILESTONE -> "Milestone reached"
            NotificationType.REMINDER -> "Reminder"
            NotificationType.URGENT -> "Urgent"
            NotificationType.SUMMARY -> "Update"
        }
    }
    
    private fun generateNotificationId(event: CountdownEvent, milestone: Milestone?): Int {
        return NOTIFICATION_BASE_ID + event.id.toInt() + (milestone?.id?.hashCode() ?: 0)
    }
}

data class NotificationBundle(
    val event: CountdownEvent,
    val type: NotificationType,
    val milestone: Milestone? = null
)

enum class NotificationType {
    MILESTONE,
    REMINDER,
    URGENT,
    SUMMARY
}