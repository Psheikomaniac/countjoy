package com.countjoy.domain.model

/**
 * Domain model representing the remaining time for a countdown
 * 
 * @property days Number of days remaining
 * @property hours Number of hours remaining (0-23)
 * @property minutes Number of minutes remaining (0-59)
 * @property seconds Number of seconds remaining (0-59)
 * @property totalSeconds Total number of seconds remaining
 * @property isExpired Whether the countdown has expired
 */
data class CountdownTime(
    val days: Long = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val totalSeconds: Long = 0,
    val isExpired: Boolean = false
) {
    /**
     * Creates a formatted string representation of the countdown
     * @param includeSeconds Whether to include seconds in the format
     * @return Formatted countdown string
     */
    fun toFormattedString(includeSeconds: Boolean = true): String {
        return when {
            isExpired -> "Expired"
            days > 0 -> {
                if (includeSeconds) {
                    String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds)
                } else {
                    String.format("%dd %02dh %02dm", days, hours, minutes)
                }
            }
            hours > 0 -> {
                if (includeSeconds) {
                    String.format("%02dh %02dm %02ds", hours, minutes, seconds)
                } else {
                    String.format("%02dh %02dm", hours, minutes)
                }
            }
            minutes > 0 -> {
                if (includeSeconds) {
                    String.format("%02dm %02ds", minutes, seconds)
                } else {
                    String.format("%dm", minutes)
                }
            }
            else -> String.format("%ds", seconds)
        }
    }
    
    /**
     * Gets a short formatted string (days or hours only)
     * @return Short formatted string
     */
    fun toShortString(): String {
        return when {
            isExpired -> "Expired"
            days > 0 -> "$days days"
            hours > 0 -> "$hours hours"
            minutes > 0 -> "$minutes minutes"
            else -> "$seconds seconds"
        }
    }
    
    companion object {
        /**
         * Creates a CountdownTime from total seconds
         * @param totalSeconds Total number of seconds
         * @return CountdownTime instance
         */
        fun fromSeconds(totalSeconds: Long): CountdownTime {
            if (totalSeconds <= 0) {
                return CountdownTime(isExpired = true)
            }
            
            val days = totalSeconds / 86400
            val hours = ((totalSeconds % 86400) / 3600).toInt()
            val minutes = ((totalSeconds % 3600) / 60).toInt()
            val seconds = (totalSeconds % 60).toInt()
            
            return CountdownTime(
                days = days,
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                totalSeconds = totalSeconds,
                isExpired = false
            )
        }
    }
}