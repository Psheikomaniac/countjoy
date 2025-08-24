package com.countjoy.data.repository

import com.countjoy.data.local.dao.RecurrenceRuleDao
import com.countjoy.data.mapper.RecurrenceRuleMapper.toDomain
import com.countjoy.data.mapper.RecurrenceRuleMapper.toEntity
import com.countjoy.domain.model.RecurrenceRule
import com.countjoy.domain.repository.RecurrenceRuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class RecurrenceRuleRepositoryImpl @Inject constructor(
    private val recurrenceRuleDao: RecurrenceRuleDao
) : RecurrenceRuleRepository {
    
    override suspend fun getRecurrenceRuleByEventId(eventId: String): RecurrenceRule? {
        return recurrenceRuleDao.getRecurrenceRuleByEventId(eventId)?.toDomain()
    }
    
    override fun observeRecurrenceRuleByEventId(eventId: String): Flow<RecurrenceRule?> {
        return recurrenceRuleDao.observeRecurrenceRuleByEventId(eventId).map { entity ->
            entity?.toDomain()
        }
    }
    
    override suspend fun getAllRecurrenceRules(): List<RecurrenceRule> {
        return recurrenceRuleDao.getAllRecurrenceRules().map { it.toDomain() }
    }
    
    override suspend fun getRecurrenceRulesDueBy(date: LocalDate): List<RecurrenceRule> {
        return recurrenceRuleDao.getRecurrenceRulesDueBy(date).map { it.toDomain() }
    }
    
    override suspend fun insertRecurrenceRule(rule: RecurrenceRule) {
        recurrenceRuleDao.insertRecurrenceRule(rule.toEntity())
    }
    
    override suspend fun updateRecurrenceRule(rule: RecurrenceRule) {
        recurrenceRuleDao.updateRecurrenceRule(rule.toEntity())
    }
    
    override suspend fun updateOccurrenceDates(ruleId: String, lastDate: LocalDate, nextDate: LocalDate) {
        recurrenceRuleDao.updateOccurrenceDates(ruleId, lastDate, nextDate)
    }
    
    override suspend fun deleteRecurrenceRule(rule: RecurrenceRule) {
        recurrenceRuleDao.deleteRecurrenceRule(rule.toEntity())
    }
    
    override suspend fun deleteRecurrenceRuleByEventId(eventId: String) {
        recurrenceRuleDao.deleteRecurrenceRuleByEventId(eventId)
    }
}