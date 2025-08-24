package com.countjoy.data.local.dao

import androidx.room.*
import com.countjoy.data.local.entity.CountdownEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountdownEventDao {
    
    @Query("SELECT * FROM countdown_events WHERE is_active = 1 ORDER BY target_date_time ASC")
    fun getAllActiveEvents(): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT * FROM countdown_events ORDER BY target_date_time ASC")
    fun getAllEvents(): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT * FROM countdown_events WHERE id = :id")
    suspend fun getEventById(id: Long): CountdownEventEntity?
    
    @Query("SELECT * FROM countdown_events WHERE id = :id")
    fun observeEventById(id: Long): Flow<CountdownEventEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CountdownEventEntity): Long
    
    @Update
    suspend fun updateEvent(event: CountdownEventEntity)
    
    @Delete
    suspend fun deleteEvent(event: CountdownEventEntity)
    
    @Query("DELETE FROM countdown_events WHERE id = :id")
    suspend fun deleteEventById(id: Long)
    
    @Query("UPDATE countdown_events SET is_active = :isActive WHERE id = :id")
    suspend fun updateEventActiveStatus(id: Long, isActive: Boolean)
    
    // Multi-event support queries
    @Query("""
        SELECT * FROM countdown_events 
        WHERE (:searchQuery IS NULL OR title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%')
        AND (:category IS NULL OR category = :category)
        AND (:priority IS NULL OR priority = :priority)
        AND is_active = 1
        ORDER BY 
            CASE WHEN :sortBy = 'priority' THEN priority END DESC,
            CASE WHEN :sortBy = 'date' THEN target_date_time END ASC,
            CASE WHEN :sortBy = 'name' THEN title END ASC,
            target_date_time ASC
    """)
    fun searchAndFilterEvents(
        searchQuery: String? = null,
        category: String? = null,
        priority: Int? = null,
        sortBy: String = "date"
    ): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT DISTINCT category FROM countdown_events WHERE is_active = 1")
    fun getAllCategories(): Flow<List<String>>
    
    @Query("SELECT * FROM countdown_events WHERE category = :category AND is_active = 1 ORDER BY target_date_time ASC")
    fun getEventsByCategory(category: String): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT * FROM countdown_events WHERE priority = :priority AND is_active = 1 ORDER BY target_date_time ASC")
    fun getEventsByPriority(priority: Int): Flow<List<CountdownEventEntity>>
    
    @Query("""
        SELECT * FROM countdown_events 
        WHERE target_date_time >= :startDate AND target_date_time <= :endDate 
        AND is_active = 1 
        ORDER BY target_date_time ASC
    """)
    fun getEventsByDateRange(startDate: Long, endDate: Long): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT * FROM countdown_events WHERE target_date_time < :currentTime AND is_active = 1 ORDER BY target_date_time DESC")
    fun getPastEvents(currentTime: Long): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT * FROM countdown_events WHERE target_date_time >= :currentTime AND is_active = 1 ORDER BY target_date_time ASC LIMIT :limit")
    fun getUpcomingEvents(currentTime: Long, limit: Int = 10): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT COUNT(*) FROM countdown_events WHERE is_active = 1")
    suspend fun getActiveEventCount(): Int
    
    @Query("SELECT COUNT(*) FROM countdown_events WHERE category = :category AND is_active = 1")
    suspend fun getEventCountByCategory(category: String): Int
    
    @Insert
    suspend fun insertEvents(events: List<CountdownEventEntity>): List<Long>
    
    @Query("UPDATE countdown_events SET priority = :priority WHERE id = :id")
    suspend fun updateEventPriority(id: Long, priority: Int)
    
    @Transaction
    suspend fun duplicateEvent(eventId: Long): Long {
        val originalEvent = getEventById(eventId) ?: return -1
        val duplicatedEvent = originalEvent.copy(
            id = 0,
            title = "${originalEvent.title} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return insertEvent(duplicatedEvent)
    }
}