package com.countjoy.domain.model

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Domain model representing a countdown event
 * 
 * @property id Unique identifier for the event
 * @property title The title/name of the event
 * @property description Optional description of the event
 * @property category Event category for organization
 * @property targetDateTime The target date and time for the countdown
 * @property reminderEnabled Whether reminders are enabled for this event
 * @property reminderTime When to show reminder (milliseconds before target)
 * @property color Optional custom color for the event
 * @property icon Optional icon identifier for the event
 * @property createdAt When the event was created
 * @property updatedAt When the event was last updated
 * @property isActive Whether this event is currently active
 * @property priority Priority level (0 = normal, higher = more important)
 */
data class CountdownEvent(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val category: String = "General",
    val targetDateTime: LocalDateTime,
    val reminderEnabled: Boolean = false,
    val reminderTime: Long? = null,
    val color: Int? = null,
    val icon: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isActive: Boolean = true,
    val priority: Int = 0
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
    
    /**
     * Calculates detailed time units remaining until the target date
     * @return Triple of (days, hours, minutes, seconds) remaining
     */
    fun getDetailedTimeRemaining(): DetailedTime {
        val now = LocalDateTime.now()
        return if (now.isAfter(targetDateTime) || now.isEqual(targetDateTime)) {
            DetailedTime(0, 0, 0, 0, true)
        } else {
            val totalSeconds = ChronoUnit.SECONDS.between(now, targetDateTime)
            val days = totalSeconds / 86400
            val hours = ((totalSeconds % 86400) / 3600).toInt()
            val minutes = ((totalSeconds % 3600) / 60).toInt()
            val seconds = (totalSeconds % 60).toInt()
            DetailedTime(days, hours, minutes, seconds, false)
        }
    }
    
    /**
     * Gets a formatted countdown string with detailed time units
     * @param includeSeconds Whether to include seconds in the format
     * @return Formatted string like "5 days, 3 hours, 20 minutes, 15 seconds"
     */
    fun getFormattedCountdown(includeSeconds: Boolean = true): String {
        val detailed = getDetailedTimeRemaining()
        return if (detailed.isExpired) {
            "Expired"
        } else {
            buildString {
                val parts = mutableListOf<String>()
                
                if (detailed.days > 0) {
                    parts.add("${detailed.days} ${if (detailed.days == 1L) "day" else "days"}")
                }
                if (detailed.hours > 0) {
                    parts.add("${detailed.hours} ${if (detailed.hours == 1) "hour" else "hours"}")
                }
                if (detailed.minutes > 0) {
                    parts.add("${detailed.minutes} ${if (detailed.minutes == 1) "minute" else "minutes"}")
                }
                if (includeSeconds && detailed.seconds > 0) {
                    parts.add("${detailed.seconds} ${if (detailed.seconds == 1) "second" else "seconds"}")
                }
                
                when {
                    parts.isEmpty() && includeSeconds -> "${detailed.seconds} ${if (detailed.seconds == 1) "second" else "seconds"}"
                    parts.isEmpty() -> "Less than a minute"
                    else -> parts.joinToString(", ")
                }
            }
        }
    }

/**
 * Data class representing detailed time units
 */
data class DetailedTime(
    val days: Long,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val isExpired: Boolean
)
}