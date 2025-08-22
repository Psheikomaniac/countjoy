package com.countjoy.domain.model

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Domain model representing a countdown event
 * 
 * @property id Unique identifier for the event
 * @property title The title/name of the event
 * @property description Optional description of the event
 * @property targetDateTime The target date and time for the countdown
 * @property createdAt When the event was created
 * @property isActive Whether this event is currently active
 */
data class CountdownEvent(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val targetDateTime: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isActive: Boolean = true
) {
    /**
     * Validates the countdown event
     * @return true if the event is valid, false otherwise
     */
    fun isValid(): Boolean {
        return title.isNotBlank() && 
               targetDateTime.isAfter(LocalDateTime.now())
    }
    
    /**
     * Checks if the event has expired
     * @return true if the target date/time has passed
     */
    fun hasExpired(): Boolean {
        return targetDateTime.isBefore(LocalDateTime.now())
    }
    
    /**
     * Gets a display-friendly title with truncation if needed
     * @param maxLength Maximum length for the title
     * @return Truncated title with ellipsis if needed
     */
    fun getDisplayTitle(maxLength: Int = 30): String {
        return if (title.length <= maxLength) {
            title
        } else {
            "${title.take(maxLength - 3)}..."
        }
    }
    
    /**
     * Calculates the number of days remaining until the target date
     * @return Number of days remaining, or 0 if the event has expired
     */
    fun getDaysRemaining(): Long {
        val now = LocalDateTime.now()
        return if (now.isAfter(targetDateTime)) {
            0
        } else {
            ChronoUnit.DAYS.between(now.toLocalDate(), targetDateTime.toLocalDate())
        }
    }
}