package com.countjoy.data.local.dao

import androidx.room.*
import com.countjoy.data.local.entity.RecurrenceRuleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RecurrenceRuleDao {
    @Query("SELECT * FROM recurrence_rules WHERE eventId = :eventId")
    suspend fun getRecurrenceRuleByEventId(eventId: String): RecurrenceRuleEntity?
    
    @Query("SELECT * FROM recurrence_rules WHERE eventId = :eventId")
    fun observeRecurrenceRuleByEventId(eventId: String): Flow<RecurrenceRuleEntity?>
    
    @Query("SELECT * FROM recurrence_rules")
    suspend fun getAllRecurrenceRules(): List<RecurrenceRuleEntity>
    
    @Query("SELECT * FROM recurrence_rules WHERE nextOccurrenceDate <= :date")
    suspend fun getRecurrenceRulesDueBy(date: LocalDate): List<RecurrenceRuleEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurrenceRule(rule: RecurrenceRuleEntity)
    
    @Update
    suspend fun updateRecurrenceRule(rule: RecurrenceRuleEntity)
    
    @Query("UPDATE recurrence_rules SET lastOccurrenceDate = :lastDate, nextOccurrenceDate = :nextDate WHERE id = :ruleId")
    suspend fun updateOccurrenceDates(ruleId: String, lastDate: LocalDate, nextDate: LocalDate)
    
    @Delete
    suspend fun deleteRecurrenceRule(rule: RecurrenceRuleEntity)
    
    @Query("DELETE FROM recurrence_rules WHERE eventId = :eventId")
    suspend fun deleteRecurrenceRuleByEventId(eventId: String)
}