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
     * Delete an event by ID
     */
    suspend fun deleteEvent(id: Long)
    
    /**
     * Update the active status of an event
     */
    suspend fun updateEventActiveStatus(id: Long, isActive: Boolean)
}