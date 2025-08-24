package com.countjoy.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RecurrenceRule(
    val id: String = UUID.randomUUID().toString(),
    val eventId: String,
    val pattern: RecurrencePattern,
    val interval: Int = 1, // Every X days/weeks/months/years
    val daysOfWeek: Set<DayOfWeek> = emptySet(), // For weekly pattern
    val dayOfMonth: Int? = null, // For monthly by date
    val weekOfMonth: Int? = null, // For monthly by day (e.g., 2nd Tuesday)
    val monthOfYear: Int? = null, // For yearly
    val endType: RecurrenceEndType = RecurrenceEndType.NEVER,
    val endDate: LocalDate? = null,
    val occurrenceCount: Int? = null,
    val exceptions: List<LocalDate> = emptyList(),
    val skipWeekends: Boolean = false,
    val skipHolidays: Boolean = false
)

enum class RecurrencePattern {
    DAILY,
    WEEKLY,
    MONTHLY_BY_DATE, // Same date each month (e.g., 15th)
    MONTHLY_BY_DAY, // Same day each month (e.g., 2nd Tuesday)
    YEARLY
}

enum class RecurrenceEndType {
    NEVER,
    BY_DATE,
    AFTER_OCCURRENCES
}

object RecurrenceTemplates {
    val templates = listOf(
        RecurrenceTemplate(
            name = "Daily",
            description = "Every day",
            rule = RecurrenceRule(
                eventId = "",
                pattern = RecurrencePattern.DAILY,
                interval = 1
            )
        ),
        RecurrenceTemplate(
            name = "Weekdays",
            description = "Monday to Friday",
            rule = RecurrenceRule(
                eventId = "",
                pattern = RecurrencePattern.WEEKLY,
                interval = 1,
                daysOfWeek = setOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY
                )
            )
        ),
        RecurrenceTemplate(
            name = "Weekly",
            description = "Every week on the same day",
            rule = RecurrenceRule(
                eventId = "",
                pattern = RecurrencePattern.WEEKLY,
                interval = 1
            )
        ),
        RecurrenceTemplate(
            name = "Bi-weekly",
            description = "Every two weeks",
            rule = RecurrenceRule(
                eventId = "",
                pattern = RecurrencePattern.WEEKLY,
                interval = 2
            )
        ),
        RecurrenceTemplate(
            name = "Monthly",
            description = "Same date every month",
            rule = RecurrenceRule(
                eventId = "",
                pattern = RecurrencePattern.MONTHLY_BY_DATE,
                interval = 1
            )
        ),
        RecurrenceTemplate(
            name = "Quarterly",
            description = "Every 3 months",
            rule = RecurrenceRule(
                eventId = "",
                pattern = RecurrencePattern.MONTHLY_BY_DATE,
                interval = 3
            )
        ),
        RecurrenceTemplate(
            name = "Yearly",
            description = "Same date every year",
            rule = RecurrenceRule(
                eventId = "",
                pattern = RecurrencePattern.YEARLY,
                interval = 1
            )
        )
    )
}

data class RecurrenceTemplate(
    val name: String,
    val description: String,
    val rule: RecurrenceRule
)

class RecurrenceCalculator {
    
    fun calculateNextOccurrences(
        rule: RecurrenceRule,
        startDate: LocalDateTime,
        count: Int = 10
    ): List<LocalDateTime> {
        val occurrences = mutableListOf<LocalDateTime>()
        var currentDate = startDate
        var occurrenceCount = 0
        
        while (occurrences.size < count && !shouldStop(rule, currentDate.toLocalDate(), occurrenceCount)) {
            currentDate = calculateNextOccurrence(rule, currentDate)
            
            if (!isException(rule, currentDate.toLocalDate()) && 
                !shouldSkip(rule, currentDate.toLocalDate())) {
                occurrences.add(currentDate)
                occurrenceCount++
            }
        }
        
        return occurrences
    }
    
    private fun calculateNextOccurrence(
        rule: RecurrenceRule,
        currentDate: LocalDateTime
    ): LocalDateTime {
        return when (rule.pattern) {
            RecurrencePattern.DAILY -> currentDate.plusDays(rule.interval.toLong())
            RecurrencePattern.WEEKLY -> calculateNextWeeklyOccurrence(rule, currentDate)
            RecurrencePattern.MONTHLY_BY_DATE -> currentDate.plusMonths(rule.interval.toLong())
            RecurrencePattern.MONTHLY_BY_DAY -> calculateNextMonthlyByDayOccurrence(rule, currentDate)
            RecurrencePattern.YEARLY -> currentDate.plusYears(rule.interval.toLong())
        }
    }
    
    private fun calculateNextWeeklyOccurrence(
        rule: RecurrenceRule,
        currentDate: LocalDateTime
    ): LocalDateTime {
        if (rule.daysOfWeek.isEmpty()) {
            return currentDate.plusWeeks(rule.interval.toLong())
        }
        
        var nextDate = currentDate.plusDays(1)
        while (true) {
            if (nextDate.dayOfWeek in rule.daysOfWeek) {
                return nextDate
            }
            nextDate = nextDate.plusDays(1)
        }
    }
    
    private fun calculateNextMonthlyByDayOccurrence(
        rule: RecurrenceRule,
        currentDate: LocalDateTime
    ): LocalDateTime {
        // Implementation for "2nd Tuesday" type recurrence
        // This is simplified - full implementation would be more complex
        return currentDate.plusMonths(rule.interval.toLong())
    }
    
    private fun shouldStop(
        rule: RecurrenceRule,
        currentDate: LocalDate,
        occurrenceCount: Int
    ): Boolean {
        return when (rule.endType) {
            RecurrenceEndType.NEVER -> false
            RecurrenceEndType.BY_DATE -> rule.endDate?.let { currentDate.isAfter(it) } ?: false
            RecurrenceEndType.AFTER_OCCURRENCES -> 
                rule.occurrenceCount?.let { occurrenceCount >= it } ?: false
        }
    }
    
    private fun isException(rule: RecurrenceRule, date: LocalDate): Boolean {
        return date in rule.exceptions
    }
    
    private fun shouldSkip(rule: RecurrenceRule, date: LocalDate): Boolean {
        if (rule.skipWeekends) {
            val dayOfWeek = date.dayOfWeek
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                return true
            }
        }
        
        // Holiday checking would require a holiday calendar service
        // if (rule.skipHolidays && isHoliday(date)) return true
        
        return false
    }
}