package com.countjoy.domain.model

/**
 * Predefined event categories with associated icons and colors
 */
enum class EventCategory(
    val displayName: String,
    val icon: String,
    val defaultColor: Int
) {
    GENERAL("General", "event", 0xFF6200EE.toInt()),
    PERSONAL("Personal", "person", 0xFF2196F3.toInt()),
    WORK("Work", "work", 0xFF4CAF50.toInt()),
    BIRTHDAY("Birthday", "cake", 0xFFE91E63.toInt()),
    ANNIVERSARY("Anniversary", "favorite", 0xFFFF5722.toInt()),
    HOLIDAY("Holiday", "beach_access", 0xFF009688.toInt()),
    MEETING("Meeting", "groups", 0xFF3F51B5.toInt()),
    DEADLINE("Deadline", "schedule", 0xFFF44336.toInt()),
    TRAVEL("Travel", "flight", 0xFF00BCD4.toInt()),
    EDUCATION("Education", "school", 0xFF9C27B0.toInt()),
    HEALTH("Health", "health_and_safety", 0xFF8BC34A.toInt()),
    SPORTS("Sports", "sports", 0xFFFF9800.toInt()),
    ENTERTAINMENT("Entertainment", "movie", 0xFF795548.toInt()),
    FINANCE("Finance", "attach_money", 0xFFFFEB3B.toInt()),
    CUSTOM("Custom", "star", 0xFF607D8B.toInt());
    
    companion object {
        /**
         * Get category by name, returns GENERAL if not found
         */
        fun fromName(name: String): EventCategory {
            return values().find { it.name == name || it.displayName == name } ?: GENERAL
        }
        
        /**
         * Get all category display names
         */
        fun getAllDisplayNames(): List<String> {
            return values().map { it.displayName }
        }
    }
}