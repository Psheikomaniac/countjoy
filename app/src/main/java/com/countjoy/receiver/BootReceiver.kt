package com.countjoy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.countjoy.data.local.preferences.SharedPreferencesManager
import com.countjoy.service.CountdownService

/**
 * BroadcastReceiver to handle device boot completion and restart countdown service
 */
class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            
            // Create preferences manager directly (cannot use Hilt injection in BroadcastReceiver)
            val preferencesManager = SharedPreferencesManager(context)
            
            // Check if service should be restarted based on user preferences
            if (preferencesManager.isNotificationEnabled()) {
                startCountdownService(context)
            }
        }
    }
    
    private fun startCountdownService(context: Context) {
        val serviceIntent = Intent(context, CountdownService::class.java).apply {
            action = CountdownService.ACTION_START
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}