package com.countjoy.presentation.countdown

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages real-time countdown updates for the UI.
 * Optimizes update frequency based on time remaining to save battery.
 */
@Singleton
class CountdownUpdateManager @Inject constructor() {
    
    private val _updateTrigger = MutableSharedFlow<Unit>()
    val updateTrigger: SharedFlow<Unit> = _updateTrigger.asSharedFlow()
    
    private var updateJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    /**
     * Starts the countdown update timer.
     * Updates are triggered based on the shortest time remaining among active events.
     */
    fun startUpdates(events: List<EventWithCountdown>) {
        stopUpdates()
        
        if (events.isEmpty()) return
        
        updateJob = coroutineScope.launch {
            while (isActive) {
                // Find the event with the least time remaining
                val shortestTimeRemaining = events
                    .filter { !it.event.hasExpired() }
                    .minOfOrNull { event ->
                        Duration.between(LocalDateTime.now(), event.event.targetDateTime)
                    }
                
                if (shortestTimeRemaining == null) {
                    // All events expired
                    break
                }
                
                // Calculate update interval based on time remaining
                val updateInterval = calculateUpdateInterval(shortestTimeRemaining)
                
                // Wait for the calculated interval
                delay(updateInterval)
                
                // Trigger UI update
                _updateTrigger.emit(Unit)
            }
        }
    }
    
    /**
     * Stops the countdown update timer.
     */
    fun stopUpdates() {
        updateJob?.cancel()
        updateJob = null
    }
    
    /**
     * Calculates the optimal update interval based on time remaining.
     * More frequent updates for imminent events, less frequent for distant events.
     */
    private fun calculateUpdateInterval(timeRemaining: Duration): Long {
        return when {
            timeRemaining.toHours() < 1 -> 1_000L // Update every second for last hour
            timeRemaining.toDays() < 1 -> 60_000L // Update every minute for last day
            timeRemaining.toDays() < 7 -> 300_000L // Update every 5 minutes for last week
            timeRemaining.toDays() < 30 -> 3_600_000L // Update every hour for last month
            else -> 21_600_000L // Update every 6 hours for distant events
        }
    }
    
    /**
     * Lifecycle-aware start method for when app comes to foreground
     */
    fun onResume(events: List<EventWithCountdown>) {
        startUpdates(events)
    }
    
    /**
     * Lifecycle-aware stop method for when app goes to background
     */
    fun onPause() {
        stopUpdates()
    }
    
    /**
     * Clean up resources
     */
    fun onDestroy() {
        stopUpdates()
        coroutineScope.cancel()
    }
}

/**
 * Data class to hold event with its countdown state
 */
data class EventWithCountdown(
    val event: com.countjoy.domain.model.CountdownEvent,
    val isUpdating: Boolean = true
)