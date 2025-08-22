package com.countjoy.data.repository

import com.countjoy.data.local.dao.CountdownEventDao
import com.countjoy.data.mapper.toDomain
import com.countjoy.data.mapper.toEntity
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of EventRepository using Room database
 */
@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventDao: CountdownEventDao
) : EventRepository {
    
    override fun getAllEvents(): Flow<List<CountdownEvent>> {
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getActiveEvents(): Flow<List<CountdownEvent>> {
        return eventDao.getActiveEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getEventById(id: Long): CountdownEvent? {
        return eventDao.getEventById(id)?.toDomain()
    }
    
    override suspend fun createEvent(event: CountdownEvent): Long {
        return eventDao.insertEvent(event.toEntity())
    }
    
    override suspend fun updateEvent(event: CountdownEvent) {
        eventDao.updateEvent(event.toEntity())
    }
    
    override suspend fun deleteEvent(id: Long) {
        eventDao.deleteEvent(id)
    }
    
    override suspend fun updateEventActiveStatus(id: Long, isActive: Boolean) {
        eventDao.updateEventActiveStatus(id, isActive)
    }
}