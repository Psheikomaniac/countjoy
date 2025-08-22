package com.countjoy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.countjoy.domain.usecase.GetEventUseCase
import com.countjoy.service.CountdownService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * BroadcastReceiver to handle timezone changes and recalculate countdowns
 */
@AndroidEntryPoint
class TimezoneChangeReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var getEventUseCase: GetEventUseCase
    
    private val receiverScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_TIME_CHANGED -> {
                handleTimezoneChange(context)
            }
        }
    }
    
    private fun handleTimezoneChange(context: Context) {
        receiverScope.launch {
            // Restart the countdown service to recalculate with new timezone
            restartCountdownService(context)
            
            // Notify any active UI components about the timezone change
            sendTimezoneChangeBroadcast(context)
        }
    }
    
    private fun restartCountdownService(context: Context) {
        // Stop the existing service
        val stopIntent = Intent(context, CountdownService::class.java).apply {
            action = CountdownService.ACTION_STOP
        }
        context.stopService(stopIntent)
        
        // Start the service again with updated timezone
        val startIntent = Intent(context, CountdownService::class.java).apply {
            action = CountdownService.ACTION_START
        }
        context.startService(startIntent)
    }
    
    private fun sendTimezoneChangeBroadcast(context: Context) {
        val intent = Intent("com.countjoy.TIMEZONE_CHANGED")
        context.sendBroadcast(intent)
    }
}

/**
 * Helper class to handle timezone-aware countdown calculations
 */
class TimezoneManager @Inject constructor(
    private val context: Context
) {
    companion object {
        const val ACTION_TIMEZONE_UPDATED = "com.countjoy.TIMEZONE_UPDATED"
    }
    
    /**
     * Register receiver for timezone changes
     */
    fun registerTimezoneReceiver() {
        val filter = android.content.IntentFilter().apply {
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
            addAction(Intent.ACTION_TIME_CHANGED)
        }
        context.registerReceiver(TimezoneChangeReceiver(), filter)
    }
    
    /**
     * Unregister receiver
     */
    fun unregisterTimezoneReceiver(receiver: BroadcastReceiver) {
        try {
            context.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            // Receiver was not registered
        }
    }
    
    /**
     * Get current timezone offset in milliseconds
     */
    fun getCurrentTimezoneOffset(): Int {
        val tz = java.util.TimeZone.getDefault()
        return tz.getOffset(System.currentTimeMillis())
    }
    
    /**
     * Get timezone display name
     */
    fun getTimezoneDisplayName(): String {
        val tz = java.util.TimeZone.getDefault()
        return tz.getDisplayName(false, java.util.TimeZone.SHORT)
    }
    
    /**
     * Check if daylight saving time is active
     */
    fun isDaylightSavingTime(): Boolean {
        val tz = java.util.TimeZone.getDefault()
        return tz.inDaylightTime(java.util.Date())
    }
}