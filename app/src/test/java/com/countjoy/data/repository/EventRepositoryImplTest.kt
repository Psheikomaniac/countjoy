package com.countjoy.data.repository

import com.countjoy.data.local.dao.CountdownEventDao
import com.countjoy.data.local.entity.CountdownEventEntity
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.repository.EventRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(MockitoJUnitRunner::class)
class EventRepositoryImplTest {
    
    @Mock
    private lateinit var eventDao: CountdownEventDao
    
    private lateinit var repository: EventRepository
    
    @Before
    fun setUp() {
        repository = EventRepositoryImpl(eventDao)
    }
    
    @Test
    fun `getAllEvents returns mapped domain models`() = runTest {
        // Given
        val entities = listOf(
            createTestEntity(1, "Event 1"),
            createTestEntity(2, "Event 2")
        )
        whenever(eventDao.getAllEvents()).thenReturn(flowOf(entities))
        
        // When
        val result = repository.getAllEvents().first()
        
        // Then
        assertEquals(2, result.size)
        assertEquals("Event 1", result[0].title)
        assertEquals("Event 2", result[1].title)
    }
    
    @Test
    fun `getActiveEvents returns only active events`() = runTest {
        // Given
        val entities = listOf(
            createTestEntity(1, "Active Event", isActive = true),
            createTestEntity(2, "Inactive Event", isActive = false)
        )
        whenever(eventDao.getAllActiveEvents()).thenReturn(flowOf(entities.filter { it.isActive }))
        
        // When
        val result = repository.getActiveEvents().first()
        
        // Then
        assertEquals(1, result.size)
        assertEquals("Active Event", result[0].title)
    }
    
    @Test
    fun `getEventById returns event when found`() = runTest {
        // Given
        val entity = createTestEntity(1, "Test Event")
        whenever(eventDao.getEventById(1)).thenReturn(entity)
        
        // When
        val result = repository.getEventById(1)
        
        // Then
        assertNotNull(result)
        assertEquals("Test Event", result.title)
    }
    
    @Test
    fun `getEventById returns null when not found`() = runTest {
        // Given
        whenever(eventDao.getEventById(999)).thenReturn(null)
        
        // When
        val result = repository.getEventById(999)
        
        // Then
        assertNull(result)
    }
    
    @Test
    fun `createEvent inserts entity and returns id`() = runTest {
        // Given
        val event = createTestDomainEvent(title = "New Event")
        whenever(eventDao.insertEvent(any())).thenReturn(42L)
        
        // When
        val result = repository.createEvent(event)
        
        // Then
        assertEquals(42L, result)
        verify(eventDao).insertEvent(any())
    }
    
    @Test
    fun `updateEvent updates entity in database`() = runTest {
        // Given
        val event = createTestDomainEvent(id = 1, title = "Updated Event")
        
        // When
        repository.updateEvent(event)
        
        // Then
        verify(eventDao).updateEvent(any())
    }
    
    @Test
    fun `deleteEvent removes entity from database`() = runTest {
        // When
        repository.deleteEvent(1)
        
        // Then
        verify(eventDao).deleteEventById(1)
    }
    
    @Test
    fun `updateEventActiveStatus updates status in database`() = runTest {
        // When
        repository.updateEventActiveStatus(1, false)
        
        // Then
        verify(eventDao).updateEventActiveStatus(1, false)
    }
    
    // Helper functions
    private fun createTestEntity(
        id: Long = 0,
        title: String = "Test Event",
        category: String = "General",
        isActive: Boolean = true
    ): CountdownEventEntity {
        return CountdownEventEntity(
            id = id,
            title = title,
            description = "Test description",
            category = category,
            targetDateTime = System.currentTimeMillis() + 86400000, // 1 day from now
            reminderEnabled = false,
            reminderTime = null,
            color = null,
            icon = null,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            isActive = isActive,
            priority = 0
        )
    }
    
    private fun createTestDomainEvent(
        id: Long = 0,
        title: String = "Test Event"
    ): CountdownEvent {
        return CountdownEvent(
            id = id,
            title = title,
            description = "Test description",
            category = "General",
            targetDateTime = LocalDateTime.now().plusDays(1),
            reminderEnabled = false,
            reminderTime = null,
            color = null,
            icon = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isActive = true,
            priority = 0
        )
    }
}