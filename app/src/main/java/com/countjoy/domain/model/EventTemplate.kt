package com.countjoy.domain.model

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Template for quick event creation
 */
data class EventTemplate(
    val id: String,
    val name: String,
    val description: String,
    val category: EventCategory,
    val priority: EventPriority,
    val defaultTitle: String,
    val defaultDescription: String?,
    val daysFromNow: Long? = null,
    val reminderDaysBefore: Long? = null,
    val icon: String,
    val color: Int? = null
) {
    /**
     * Create a CountdownEvent from this template
     */
    fun toCountdownEvent(
        customTitle: String? = null,
        customDate: LocalDateTime? = null
    ): CountdownEvent {
        val targetDate = customDate ?: daysFromNow?.let {
            LocalDateTime.now().plusDays(it)
        } ?: LocalDateTime.now().plusDays(30)
        
        val reminderTime = reminderDaysBefore?.let {
            it * 24 * 60 * 60 * 1000 // Convert days to milliseconds
        }
        
        return CountdownEvent(
            title = customTitle ?: defaultTitle,
            description = defaultDescription,
            category = category.displayName,
            targetDateTime = targetDate,
            reminderEnabled = reminderTime != null,
            reminderTime = reminderTime,
            color = color ?: category.defaultColor,
            icon = icon,
            priority = priority.value
        )
    }
    
    companion object {
        /**
         * Get pre-built templates for common events
         */
        fun getDefaultTemplates(): List<EventTemplate> = listOf(
            // Birthday Templates
            EventTemplate(
                id = "birthday_family",
                name = "Family Birthday",
                description = "Birthday celebration for a family member",
                category = EventCategory.BIRTHDAY,
                priority = EventPriority.HIGH,
                defaultTitle = "Birthday",
                defaultDescription = "Don't forget to get a gift!",
                daysFromNow = 30,
                reminderDaysBefore = 7,
                icon = "cake",
                color = 0xFFE91E63.toInt()
            ),
            EventTemplate(
                id = "birthday_friend",
                name = "Friend's Birthday",
                description = "Birthday celebration for a friend",
                category = EventCategory.BIRTHDAY,
                priority = EventPriority.MEDIUM,
                defaultTitle = "Friend's Birthday",
                defaultDescription = "Send birthday wishes!",
                daysFromNow = 30,
                reminderDaysBefore = 1,
                icon = "cake",
                color = 0xFFE91E63.toInt()
            ),
            
            // Anniversary Templates
            EventTemplate(
                id = "wedding_anniversary",
                name = "Wedding Anniversary",
                description = "Wedding anniversary celebration",
                category = EventCategory.ANNIVERSARY,
                priority = EventPriority.HIGH,
                defaultTitle = "Wedding Anniversary",
                defaultDescription = "Plan something special!",
                daysFromNow = 365,
                reminderDaysBefore = 14,
                icon = "favorite",
                color = 0xFFF44336.toInt()
            ),
            EventTemplate(
                id = "work_anniversary",
                name = "Work Anniversary",
                description = "Work anniversary milestone",
                category = EventCategory.ANNIVERSARY,
                priority = EventPriority.LOW,
                defaultTitle = "Work Anniversary",
                defaultDescription = null,
                daysFromNow = 365,
                reminderDaysBefore = 1,
                icon = "work",
                color = 0xFF607D8B.toInt()
            ),
            
            // Holiday Templates
            EventTemplate(
                id = "christmas",
                name = "Christmas",
                description = "Christmas holiday",
                category = EventCategory.HOLIDAY,
                priority = EventPriority.MEDIUM,
                defaultTitle = "Christmas",
                defaultDescription = "Time for family and gifts!",
                daysFromNow = null,
                reminderDaysBefore = 7,
                icon = "beach_access",
                color = 0xFF4CAF50.toInt()
            ),
            EventTemplate(
                id = "new_year",
                name = "New Year",
                description = "New Year celebration",
                category = EventCategory.HOLIDAY,
                priority = EventPriority.MEDIUM,
                defaultTitle = "New Year",
                defaultDescription = "Time to celebrate!",
                daysFromNow = null,
                reminderDaysBefore = 1,
                icon = "beach_access",
                color = 0xFF4CAF50.toInt()
            ),
            
            // Work Templates
            EventTemplate(
                id = "project_deadline",
                name = "Project Deadline",
                description = "Important project deadline",
                category = EventCategory.DEADLINE,
                priority = EventPriority.HIGH,
                defaultTitle = "Project Deadline",
                defaultDescription = "Complete and submit the project",
                daysFromNow = 14,
                reminderDaysBefore = 3,
                icon = "schedule",
                color = 0xFFFF9800.toInt()
            ),
            EventTemplate(
                id = "meeting",
                name = "Important Meeting",
                description = "Business or team meeting",
                category = EventCategory.MEETING,
                priority = EventPriority.MEDIUM,
                defaultTitle = "Meeting",
                defaultDescription = "Prepare agenda and materials",
                daysFromNow = 7,
                reminderDaysBefore = 1,
                icon = "groups",
                color = 0xFF2196F3.toInt()
            ),
            
            // Travel Templates
            EventTemplate(
                id = "vacation",
                name = "Vacation",
                description = "Vacation trip",
                category = EventCategory.TRAVEL,
                priority = EventPriority.MEDIUM,
                defaultTitle = "Vacation Trip",
                defaultDescription = "Pack and prepare for the trip",
                daysFromNow = 30,
                reminderDaysBefore = 7,
                icon = "flight",
                color = 0xFF00BCD4.toInt()
            ),
            EventTemplate(
                id = "business_trip",
                name = "Business Trip",
                description = "Business travel",
                category = EventCategory.TRAVEL,
                priority = EventPriority.HIGH,
                defaultTitle = "Business Trip",
                defaultDescription = "Prepare documents and materials",
                daysFromNow = 14,
                reminderDaysBefore = 3,
                icon = "flight",
                color = 0xFF00BCD4.toInt()
            ),
            
            // Education Templates
            EventTemplate(
                id = "exam",
                name = "Exam",
                description = "Important exam or test",
                category = EventCategory.EDUCATION,
                priority = EventPriority.HIGH,
                defaultTitle = "Exam",
                defaultDescription = "Study and prepare!",
                daysFromNow = 7,
                reminderDaysBefore = 3,
                icon = "school",
                color = 0xFF9C27B0.toInt()
            ),
            EventTemplate(
                id = "graduation",
                name = "Graduation",
                description = "Graduation ceremony",
                category = EventCategory.EDUCATION,
                priority = EventPriority.HIGH,
                defaultTitle = "Graduation",
                defaultDescription = "Celebrate the achievement!",
                daysFromNow = 90,
                reminderDaysBefore = 14,
                icon = "school",
                color = 0xFF9C27B0.toInt()
            ),
            
            // Health Templates
            EventTemplate(
                id = "doctor_appointment",
                name = "Doctor Appointment",
                description = "Medical appointment",
                category = EventCategory.HEALTH,
                priority = EventPriority.MEDIUM,
                defaultTitle = "Doctor Appointment",
                defaultDescription = "Bring medical records",
                daysFromNow = 7,
                reminderDaysBefore = 1,
                icon = "health_and_safety",
                color = 0xFF4CAF50.toInt()
            ),
            EventTemplate(
                id = "fitness_goal",
                name = "Fitness Goal",
                description = "Fitness or health goal deadline",
                category = EventCategory.HEALTH,
                priority = EventPriority.MEDIUM,
                defaultTitle = "Fitness Goal",
                defaultDescription = "Stay on track!",
                daysFromNow = 30,
                reminderDaysBefore = 7,
                icon = "health_and_safety",
                color = 0xFF4CAF50.toInt()
            ),
            
            // Sports Templates
            EventTemplate(
                id = "sports_event",
                name = "Sports Event",
                description = "Sports game or competition",
                category = EventCategory.SPORTS,
                priority = EventPriority.LOW,
                defaultTitle = "Sports Event",
                defaultDescription = "Don't miss the game!",
                daysFromNow = 7,
                reminderDaysBefore = 1,
                icon = "sports",
                color = 0xFFFF5722.toInt()
            ),
            
            // Entertainment Templates
            EventTemplate(
                id = "concert",
                name = "Concert",
                description = "Music concert or show",
                category = EventCategory.ENTERTAINMENT,
                priority = EventPriority.MEDIUM,
                defaultTitle = "Concert",
                defaultDescription = "Get tickets ready!",
                daysFromNow = 30,
                reminderDaysBefore = 3,
                icon = "movie",
                color = 0xFF673AB7.toInt()
            ),
            EventTemplate(
                id = "movie_release",
                name = "Movie Release",
                description = "Movie or show release date",
                category = EventCategory.ENTERTAINMENT,
                priority = EventPriority.LOW,
                defaultTitle = "Movie Release",
                defaultDescription = "Book tickets in advance!",
                daysFromNow = 14,
                reminderDaysBefore = 1,
                icon = "movie",
                color = 0xFF673AB7.toInt()
            ),
            
            // Finance Templates
            EventTemplate(
                id = "bill_payment",
                name = "Bill Payment",
                description = "Monthly bill payment due",
                category = EventCategory.FINANCE,
                priority = EventPriority.HIGH,
                defaultTitle = "Bill Payment Due",
                defaultDescription = "Pay before the due date",
                daysFromNow = 30,
                reminderDaysBefore = 3,
                icon = "attach_money",
                color = 0xFFFFEB3B.toInt()
            ),
            EventTemplate(
                id = "tax_deadline",
                name = "Tax Deadline",
                description = "Tax filing deadline",
                category = EventCategory.FINANCE,
                priority = EventPriority.HIGH,
                defaultTitle = "Tax Filing Deadline",
                defaultDescription = "Prepare and submit tax documents",
                daysFromNow = 90,
                reminderDaysBefore = 14,
                icon = "attach_money",
                color = 0xFFFFEB3B.toInt()
            )
        )
        
        /**
         * Get templates by category
         */
        fun getTemplatesByCategory(category: EventCategory): List<EventTemplate> {
            return getDefaultTemplates().filter { it.category == category }
        }
        
        /**
         * Get popular templates (most commonly used)
         */
        fun getPopularTemplates(): List<EventTemplate> {
            return getDefaultTemplates().take(6)
        }
    }
}