package com.countjoy.data.mapper

import com.countjoy.data.local.entity.RecurrenceRuleEntity
import com.countjoy.domain.model.RecurrenceEndType
import com.countjoy.domain.model.RecurrencePattern
import com.countjoy.domain.model.RecurrenceRule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object RecurrenceRuleMapper {
    private val DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
    
    fun RecurrenceRuleEntity.toDomain(): RecurrenceRule {
        return RecurrenceRule(
            id = id,
            eventId = eventId,
            pattern = RecurrencePattern.valueOf(pattern),
            interval = intervalValue,
            daysOfWeek = daysOfWeek?.split(",")
                ?.mapNotNull { day -> 
                    try { DayOfWeek.valueOf(day.trim()) } catch (e: Exception) { null }
                }
                ?.toSet() ?: emptySet(),
            dayOfMonth = dayOfMonth,
            weekOfMonth = weekOfMonth,
            monthOfYear = monthOfYear,
            endType = RecurrenceEndType.valueOf(endType),
            endDate = endDate,
            occurrenceCount = occurrenceCount,
            exceptions = exceptions?.split(",")
                ?.mapNotNull { date ->
                    try { LocalDate.parse(date.trim(), DATE_FORMATTER) } catch (e: Exception) { null }
                } ?: emptyList(),
            skipWeekends = skipWeekends,
            skipHolidays = skipHolidays
        )
    }
    
    fun RecurrenceRule.toEntity(): RecurrenceRuleEntity {
        return RecurrenceRuleEntity(
            id = id,
            eventId = eventId,
            pattern = pattern.name,
            intervalValue = interval,
            daysOfWeek = if (daysOfWeek.isNotEmpty()) {
                daysOfWeek.joinToString(",") { it.name }
            } else null,
            dayOfMonth = dayOfMonth,
            weekOfMonth = weekOfMonth,
            monthOfYear = monthOfYear,
            endType = endType.name,
            endDate = endDate,
            occurrenceCount = occurrenceCount,
            exceptions = if (exceptions.isNotEmpty()) {
                exceptions.joinToString(",") { it.format(DATE_FORMATTER) }
            } else null,
            skipWeekends = skipWeekends,
            skipHolidays = skipHolidays,
            lastOccurrenceDate = null,
            nextOccurrenceDate = null
        )
    }
}