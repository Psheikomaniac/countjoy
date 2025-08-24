package com.countjoy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ShareCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.countjoy.MainActivity
import com.countjoy.domain.repository.EventRepository
import com.countjoy.service.SmartNotificationService
import com.countjoy.worker.SnoozeReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Handles notification actions for smart notifications
 */
@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var eventRepository: EventRepository
    
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val eventId = intent.getLongExtra(SmartNotificationService.EXTRA_EVENT_ID, -1)
        val notificationId = intent.getIntExtra(SmartNotificationService.EXTRA_NOTIFICATION_ID, -1)
        
        if (eventId == -1L) return
        
        when (action) {
            SmartNotificationService.ACTION_SNOOZE -> {
                handleSnooze(context, eventId, intent)
            }
            SmartNotificationService.ACTION_SHARE -> {
                handleShare(context, eventId)
            }
            SmartNotificationService.ACTION_QUICK_VIEW -> {
                handleQuickView(context, eventId)
            }
            SmartNotificationService.ACTION_MARK_SEEN -> {
                handleMarkSeen(context, notificationId)
            }
        }
    }
    
    private fun handleSnooze(context: Context, eventId: Long, intent: Intent) {
        val snoozeMinutes = intent.getIntExtra(
            SmartNotificationService.EXTRA_SNOOZE_MINUTES, 
            60 // Default 1 hour
        )
        
        // Cancel current notification
        NotificationManagerCompat.from(context).cancel(eventId.toInt())
        
        // Schedule snooze reminder
        val workRequest = OneTimeWorkRequestBuilder<SnoozeReminderWorker>()
            .setInitialDelay(snoozeMinutes.toLong(), TimeUnit.MINUTES)
            .setInputData(
                workDataOf(
                    "event_id" to eventId,
                    "snooze_minutes" to snoozeMinutes
                )
            )
            .build()
        
        WorkManager.getInstance(context).enqueue(workRequest)
        
        // Show confirmation
        val hours = if (snoozeMinutes >= 60) {
            "${snoozeMinutes / 60}h"
        } else {
            "${snoozeMinutes}min"
        }
        Toast.makeText(
            context, 
            "Reminder snoozed for $hours", 
            Toast.LENGTH_SHORT
        ).show()
    }
    
    private fun handleShare(context: Context, eventId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val event = eventRepository.getEventById(eventId.toString())
                event?.let {
                    val daysRemaining = it.getDaysRemaining()
                    val shareText = buildString {
                        append("🎯 Countdown: ${it.title}\n")
                        append("📅 Date: ${it.targetDateTime.toLocalDate()}\n")
                        append("⏰ Time: ${it.targetDateTime.toLocalTime()}\n")
                        append("📊 Days remaining: $daysRemaining\n")
                        it.description?.let { desc ->
                            append("\n📝 $desc")
                        }
                        append("\n\nShared from CountJoy - Your countdown companion")
                    }
                    
                    // Launch share intent on main thread
                    CoroutineScope(Dispatchers.Main).launch {
                        val shareIntent = ShareCompat.IntentBuilder(context)
                            .setType("text/plain")
                            .setText(shareText)
                            .setSubject("Countdown: ${it.title}")
                            .createChooserIntent()
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        
                        context.startActivity(shareIntent)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun handleQuickView(context: Context, eventId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val event = eventRepository.getEventById(eventId.toString())
                event?.let {
                    val daysRemaining = it.getDaysRemaining()
                    val hoursRemaining = ChronoUnit.HOURS.between(
                        LocalDateTime.now(), 
                        it.targetDateTime
                    )
                    
                    val quickViewText = when {
                        daysRemaining > 1 -> "$daysRemaining days until ${it.title}"
                        hoursRemaining > 0 -> "$hoursRemaining hours until ${it.title}"
                        else -> "${it.title} has started!"
                    }
                    
                    // Show quick view as toast
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            context, 
                            quickViewText, 
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    
                    // Open app for full view
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(SmartNotificationService.EXTRA_EVENT_ID, eventId)
                    }
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun handleMarkSeen(context: Context, notificationId: Int) {
        // Cancel the notification
        NotificationManagerCompat.from(context).cancel(notificationId)
        
        // Could also update database to mark as seen
        Toast.makeText(
            context, 
            "Notification marked as seen", 
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun java.time.LocalDateTime.toLocalDate() = this.toLocalDate()
private fun java.time.LocalDateTime.toLocalTime() = this.toLocalTime()
private fun java.time.temporal.ChronoUnit.between(from: java.time.LocalDateTime, to: java.time.LocalDateTime) = 
    this.between(from, to)