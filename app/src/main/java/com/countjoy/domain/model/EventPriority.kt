package com.countjoy.domain.model

/**
 * Event priority levels for organizing and filtering events
 */
enum class EventPriority(
    val value: Int,
    val displayName: String,
    val icon: String,
    val color: Int
) {
    LOW(0, "Low", "low_priority", 0xFF4CAF50.toInt()),
    MEDIUM(1, "Medium", "priority_high", 0xFFFF9800.toInt()),
    HIGH(2, "High", "warning", 0xFFF44336.toInt());
    
    companion object {
        /**
         * Get priority by value, returns MEDIUM if not found
         */
        fun fromValue(value: Int): EventPriority {
            return values().find { it.value == value } ?: MEDIUM
        }
        
        /**
         * Get priority by name, returns MEDIUM if not found
         */
        fun fromName(name: String): EventPriority {
            return values().find { it.name == name || it.displayName == name } ?: MEDIUM
        }
        
        /**
         * Get all priority display names
         */
        fun getAllDisplayNames(): List<String> {
            return values().map { it.displayName }
        }
    }
}