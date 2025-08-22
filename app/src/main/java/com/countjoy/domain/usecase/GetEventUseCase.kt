package com.countjoy.domain.usecase

import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving countdown events
 */
class GetEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    /**
     * Get all events
     */
    fun getAllEvents(): Flow<List<CountdownEvent>> {
        return repository.getAllEvents()
    }
    
    /**
     * Get only active events
     */
    fun getActiveEvents(): Flow<List<CountdownEvent>> {
        return repository.getActiveEvents()
    }
    
    /**
     * Get a single event by ID
     */
    suspend fun getEventById(id: Long): CountdownEvent? {
        return repository.getEventById(id)
    }
}