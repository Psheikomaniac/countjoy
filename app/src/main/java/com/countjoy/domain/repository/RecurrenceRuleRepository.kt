package com.countjoy.domain.repository

import com.countjoy.domain.model.RecurrenceRule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface RecurrenceRuleRepository {
    suspend fun getRecurrenceRuleByEventId(eventId: String): RecurrenceRule?
    fun observeRecurrenceRuleByEventId(eventId: String): Flow<RecurrenceRule?>
    suspend fun getAllRecurrenceRules(): List<RecurrenceRule>
    suspend fun getRecurrenceRulesDueBy(date: LocalDate): List<RecurrenceRule>
    suspend fun insertRecurrenceRule(rule: RecurrenceRule)
    suspend fun updateRecurrenceRule(rule: RecurrenceRule)
    suspend fun updateOccurrenceDates(ruleId: String, lastDate: LocalDate, nextDate: LocalDate)
    suspend fun deleteRecurrenceRule(rule: RecurrenceRule)
    suspend fun deleteRecurrenceRuleByEventId(eventId: String)
}