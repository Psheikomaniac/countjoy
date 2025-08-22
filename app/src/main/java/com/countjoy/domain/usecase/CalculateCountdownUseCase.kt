package com.countjoy.domain.usecase

import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.CountdownTime
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * Use case for calculating countdown time from events
 */
class CalculateCountdownUseCase @Inject constructor() {
    
    /**
     * Calculates the countdown time for a given event
     * @param event The countdown event
     * @param currentTime Optional current time for testing (defaults to now)
     * @return CountdownTime representing the time remaining
     */
    operator fun invoke(
        event: CountdownEvent,
        currentTime: LocalDateTime = LocalDateTime.now()
    ): CountdownTime {
        // Check if event has expired
        if (event.targetDateTime.isBefore(currentTime) || event.targetDateTime.isEqual(currentTime)) {
            return CountdownTime(isExpired = true)
        }
        
        // Calculate duration between now and target
        val duration = Duration.between(currentTime, event.targetDateTime)
        val totalSeconds = duration.seconds
        
        return CountdownTime.fromSeconds(totalSeconds)
    }
    
    /**
     * Calculates countdown considering timezone changes
     * @param event The countdown event
     * @param timezone The timezone to use for calculation
     * @return CountdownTime adjusted for timezone
     */
    fun invokeWithTimezone(
        event: CountdownEvent,
        timezone: ZoneId = ZoneId.systemDefault()
    ): CountdownTime {
        val currentZonedTime = ZonedDateTime.now(timezone)
        val targetZonedTime = event.targetDateTime.atZone(timezone)
        
        // Check if event has expired
        if (targetZonedTime.isBefore(currentZonedTime) || targetZonedTime.isEqual(currentZonedTime)) {
            return CountdownTime(isExpired = true)
        }
        
        // Calculate duration
        val duration = Duration.between(currentZonedTime, targetZonedTime)
        val totalSeconds = duration.seconds
        
        return CountdownTime.fromSeconds(totalSeconds)
    }
    
    /**
     * Calculates multiple countdowns for a list of events
     * @param events List of countdown events
     * @return Map of event ID to CountdownTime
     */
    fun calculateMultiple(
        events: List<CountdownEvent>
    ): Map<Long, CountdownTime> {
        val currentTime = LocalDateTime.now()
        return events.associate { event ->
            event.id to invoke(event, currentTime)
        }
    }
    
    /**
     * Gets the next upcoming event from a list
     * @param events List of countdown events
     * @return The next upcoming event or null if all are expired
     */
    fun getNextUpcoming(events: List<CountdownEvent>): CountdownEvent? {
        val currentTime = LocalDateTime.now()
        return events
            .filter { it.targetDateTime.isAfter(currentTime) && it.isActive }
            .minByOrNull { it.targetDateTime }
    }
}