package com.countjoy.domain.repository

import com.countjoy.domain.model.CountdownEvent
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing countdown events
 */
interface EventRepository {
    /**
     * Get all events as a Flow
     */
    fun getAllEvents(): Flow<List<CountdownEvent>>
    
    /**
     * Get only active events as a Flow
     */
    fun getActiveEvents(): Flow<List<CountdownEvent>>
    
    /**
     * Get all active events (alias for compatibility)
     */
    fun getAllActiveEvents(): Flow<List<CountdownEvent>> = getActiveEvents()
    
    /**
     * Get a single event by ID
     */
    suspend fun getEventById(id: Long): CountdownEvent?
    
    /**
     * Get a single event by string ID
     */
    suspend fun getEvent(id: String): CountdownEvent?
    
    /**
     * Create a new event
     */
    suspend fun createEvent(event: CountdownEvent): Long
    
    /**
     * Update an existing event
     */
    suspend fun updateEvent(event: CountdownEvent)
    
    /**
     * Delete an event
     */
    suspend fun deleteEvent(event: CountdownEvent)
    
    /**
     * Delete an event by ID
     */
    suspend fun deleteEvent(id: Long)
    
    /**
     * Update the active status of an event
     */
    suspend fun updateEventActiveStatus(id: Long, isActive: Boolean)
    
    /**
     * Get all distinct categories
     */
    fun getAllCategories(): Flow<List<String>>
    
    /**
     * Get events by category
     */
    fun getEventsByCategory(category: String): Flow<List<CountdownEvent>>
    
    /**
     * Get events by priority
     */
    fun getEventsByPriority(priority: Int): Flow<List<CountdownEvent>>
    
    /**
     * Search and filter events
     */
    fun searchAndFilterEvents(
        searchQuery: String? = null,
        category: String? = null,
        priority: Int? = null,
        sortBy: String = "date"
    ): Flow<List<CountdownEvent>>
    
    /**
     * Get events in date range
     */
    fun getEventsByDateRange(startDate: Long, endDate: Long): Flow<List<CountdownEvent>>
    
    /**
     * Get past events
     */
    fun getPastEvents(currentTime: Long = System.currentTimeMillis()): Flow<List<CountdownEvent>>
    
    /**
     * Get upcoming events
     */
    fun getUpcomingEvents(currentTime: Long = System.currentTimeMillis(), limit: Int = 10): Flow<List<CountdownEvent>>
    
    /**
     * Get active event count
     */
    suspend fun getActiveEventCount(): Int
    
    /**
     * Get event count by category
     */
    suspend fun getEventCountByCategory(category: String): Int
    
    /**
     * Duplicate an event
     */
    suspend fun duplicateEvent(eventId: Long): Long
    
    /**
     * Update event priority
     */
    suspend fun updateEventPriority(id: Long, priority: Int)
}