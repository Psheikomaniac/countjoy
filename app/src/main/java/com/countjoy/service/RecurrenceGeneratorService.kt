package com.countjoy.service

import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.RecurrenceCalculator
import com.countjoy.domain.model.RecurrenceRule
import com.countjoy.domain.repository.EventRepository
import com.countjoy.domain.repository.RecurrenceRuleRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecurrenceGeneratorService @Inject constructor(
    private val eventRepository: EventRepository,
    private val recurrenceRuleRepository: RecurrenceRuleRepository,
    private val recurrenceCalculator: RecurrenceCalculator
) {
    
    /**
     * Generate the next occurrence of a recurring event
     */
    suspend fun generateNextOccurrence(
        originalEvent: CountdownEvent,
        recurrenceRule: RecurrenceRule
    ): CountdownEvent? {
        val nextOccurrences = recurrenceCalculator.calculateNextOccurrences(
            rule = recurrenceRule,
            startDate = originalEvent.targetDateTime,
            count = 1
        )
        
        if (nextOccurrences.isEmpty()) {
            return null
        }
        
        val nextDateTime = nextOccurrences.first()
        val nextInstant = nextDateTime.atZone(ZoneId.systemDefault()).toInstant()
        
        // Create new event based on the original
        val newEvent = originalEvent.copy(
            id = System.currentTimeMillis(),
            targetDateTime = nextDateTime,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        // Save the new event
        eventRepository.createEvent(newEvent)
        
        // Update the recurrence rule with the last and next occurrence dates
        recurrenceRuleRepository.updateOccurrenceDates(
            ruleId = recurrenceRule.id,
            lastDate = LocalDate.now(),
            nextDate = nextDateTime.toLocalDate()
        )
        
        return newEvent
    }
    
    /**
     * Check all recurring events and generate new occurrences if needed
     */
    suspend fun checkAndGenerateRecurringEvents() {
        val today = LocalDate.now()
        val dueRules = recurrenceRuleRepository.getRecurrenceRulesDueBy(today)
        
        for (rule in dueRules) {
            val originalEvent = eventRepository.getEvent(rule.eventId)
            if (originalEvent != null && originalEvent.isActive) {
                generateNextOccurrence(originalEvent, rule)
            }
        }
    }
    
    /**
     * Preview future occurrences of a recurring event
     */
    fun previewFutureOccurrences(
        event: CountdownEvent,
        rule: RecurrenceRule,
        count: Int = 10
    ): List<LocalDateTime> {
        return recurrenceCalculator.calculateNextOccurrences(
            rule = rule,
            startDate = event.targetDateTime,
            count = count
        )
    }
    
    /**
     * Delete all future occurrences of a recurring event
     */
    suspend fun deleteFutureOccurrences(eventId: String) {
        // This would need to track parent-child relationships
        // For now, just delete the recurrence rule
        recurrenceRuleRepository.deleteRecurrenceRuleByEventId(eventId)
    }
    
    /**
     * Update a single occurrence vs all occurrences
     */
    suspend fun updateOccurrence(
        event: CountdownEvent,
        updateAll: Boolean
    ) {
        if (updateAll) {
            // Update the master event and regenerate future occurrences
            eventRepository.updateEvent(event)
            // Would need to update all future occurrences
        } else {
            // Just update this single occurrence
            eventRepository.updateEvent(event)
            // Mark it as an exception in the recurrence rule
            val rule = recurrenceRuleRepository.getRecurrenceRuleByEventId(event.id.toString())
            if (rule != null) {
                val updatedRule = rule.copy(
                    exceptions = rule.exceptions + LocalDate.now()
                )
                recurrenceRuleRepository.updateRecurrenceRule(updatedRule)
            }
        }
    }
}