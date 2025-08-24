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
        return eventDao.getAllActiveEvents().map { entities ->
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
    
    override suspend fun deleteEvent(event: CountdownEvent) {
        eventDao.deleteEvent(event.toEntity())
    }
    
    override suspend fun deleteEvent(id: Long) {
        eventDao.deleteEventById(id)
    }
    
    override suspend fun updateEventActiveStatus(id: Long, isActive: Boolean) {
        eventDao.updateEventActiveStatus(id, isActive)
    }
    
    override suspend fun getEvent(id: String): CountdownEvent? {
        return try {
            val longId = id.toLongOrNull() ?: return null
            getEventById(longId)
        } catch (e: Exception) {
            null
        }
    }
    
    override fun getAllCategories(): Flow<List<String>> {
        return eventDao.getAllCategories()
    }
    
    override fun getEventsByCategory(category: String): Flow<List<CountdownEvent>> {
        return eventDao.getEventsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getEventsByPriority(priority: Int): Flow<List<CountdownEvent>> {
        return eventDao.getEventsByPriority(priority).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun searchAndFilterEvents(
        searchQuery: String?,
        category: String?,
        priority: Int?,
        sortBy: String
    ): Flow<List<CountdownEvent>> {
        return eventDao.searchAndFilterEvents(searchQuery, category, priority, sortBy)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }
    
    override fun getEventsByDateRange(startDate: Long, endDate: Long): Flow<List<CountdownEvent>> {
        return eventDao.getEventsByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getPastEvents(currentTime: Long): Flow<List<CountdownEvent>> {
        return eventDao.getPastEvents(currentTime).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getUpcomingEvents(currentTime: Long, limit: Int): Flow<List<CountdownEvent>> {
        return eventDao.getUpcomingEvents(currentTime, limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getActiveEventCount(): Int {
        return eventDao.getActiveEventCount()
    }
    
    override suspend fun getEventCountByCategory(category: String): Int {
        return eventDao.getEventCountByCategory(category)
    }
    
    override suspend fun duplicateEvent(eventId: Long): Long {
        return eventDao.duplicateEvent(eventId)
    }
    
    override suspend fun updateEventPriority(id: Long, priority: Int) {
        eventDao.updateEventPriority(id, priority)
    }
}