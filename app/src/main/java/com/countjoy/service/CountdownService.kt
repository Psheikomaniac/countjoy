package com.countjoy.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.countjoy.MainActivity
import com.countjoy.R
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.usecase.CalculateCountdownUseCase
import com.countjoy.domain.usecase.GetEventUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlin.coroutines.coroutineContext
import javax.inject.Inject

/**
 * Background service for countdown updates and notifications
 */
@AndroidEntryPoint
class CountdownService : Service() {
    
    companion object {
        const val CHANNEL_ID = "countdown_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_START = "com.countjoy.action.START"
        const val ACTION_STOP = "com.countjoy.action.STOP"
        const val EXTRA_EVENT_ID = "event_id"
    }
    
    @Inject
    lateinit var getEventUseCase: GetEventUseCase
    
    @Inject
    lateinit var calculateCountdownUseCase: CalculateCountdownUseCase
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var countdownJob: Job? = null
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val eventId = intent.getLongExtra(EXTRA_EVENT_ID, -1)
                if (eventId != -1L) {
                    startCountdown(eventId)
                } else {
                    startAllCountdowns()
                }
            }
            ACTION_STOP -> {
                stopCountdown()
            }
            else -> {
                startAllCountdowns()
            }
        }
        
        return START_STICKY
    }
    
    private fun startCountdown(eventId: Long) {
        countdownJob?.cancel()
        countdownJob = serviceScope.launch {
            val event = getEventUseCase.getEventById(eventId)
            if (event != null) {
                updateCountdownLoop(listOf(event))
            }
        }
    }
    
    private fun startAllCountdowns() {
        countdownJob?.cancel()
        countdownJob = serviceScope.launch {
            getEventUseCase.getActiveEvents().collectLatest { events ->
                updateCountdownLoop(events)
            }
        }
    }
    
    private suspend fun updateCountdownLoop(events: List<CountdownEvent>) {
        while (currentCoroutineContext().isActive) {
            val nextEvent = calculateCountdownUseCase.getNextUpcoming(events)
            if (nextEvent != null) {
                val countdown = calculateCountdownUseCase(nextEvent)
                updateNotification(nextEvent, countdown.toFormattedString())
                
                // Check for expired events
                if (countdown.isExpired) {
                    sendExpiredNotification(nextEvent)
                }
            }
            
            // Update every second
            delay(1000)
        }
    }
    
    private fun stopCountdown() {
        countdownJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Countdown Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows countdown updates"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun updateNotification(event: CountdownEvent, countdownText: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(event.title)
            .setContentText(countdownText)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSilent(true)
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun sendExpiredNotification(event: CountdownEvent) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Countdown Expired!")
            .setContentText("${event.title} has reached its target time")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(event.id.toInt(), notification)
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}