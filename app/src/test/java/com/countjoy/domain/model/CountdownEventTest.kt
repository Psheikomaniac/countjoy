package com.countjoy.domain.model

import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class CountdownEventTest {
    
    @Test
    fun `test CountdownEvent creation with default values`() {
        val now = LocalDateTime.now()
        val targetDate = now.plusDays(7)
        
        val event = CountdownEvent(
            title = "Test Event",
            targetDateTime = targetDate
        )
        
        assertEquals(0L, event.id)
        assertEquals("Test Event", event.title)
        assertNull(event.description)
        assertEquals(targetDate, event.targetDateTime)
        assertTrue(event.isActive)
        assertNotNull(event.createdAt)
    }
    
    @Test
    fun `test CountdownEvent creation with all values`() {
        val now = LocalDateTime.now()
        val targetDate = now.plusDays(7)
        val createdDate = now.minusDays(1)
        
        val event = CountdownEvent(
            id = 123,
            title = "Birthday Party",
            description = "John's 30th birthday",
            targetDateTime = targetDate,
            createdAt = createdDate,
            isActive = false
        )
        
        assertEquals(123L, event.id)
        assertEquals("Birthday Party", event.title)
        assertEquals("John's 30th birthday", event.description)
        assertEquals(targetDate, event.targetDateTime)
        assertEquals(createdDate, event.createdAt)
        assertFalse(event.isActive)
    }
    
    @Test
    fun `test isValid returns true for valid future event`() {
        val event = CountdownEvent(
            title = "Future Event",
            targetDateTime = LocalDateTime.now().plusDays(1)
        )
        
        assertTrue(event.isValid())
    }
    
    @Test
    fun `test isValid returns false for past event`() {
        val event = CountdownEvent(
            title = "Past Event",
            targetDateTime = LocalDateTime.now().minusDays(1)
        )
        
        assertFalse(event.isValid())
    }
    
    @Test
    fun `test isValid returns false for blank title`() {
        val event = CountdownEvent(
            title = "   ",
            targetDateTime = LocalDateTime.now().plusDays(1)
        )
        
        assertFalse(event.isValid())
    }
    
    @Test
    fun `test hasExpired returns true for past event`() {
        val event = CountdownEvent(
            title = "Past Event",
            targetDateTime = LocalDateTime.now().minusHours(1)
        )
        
        assertTrue(event.hasExpired())
    }
    
    @Test
    fun `test hasExpired returns false for future event`() {
        val event = CountdownEvent(
            title = "Future Event",
            targetDateTime = LocalDateTime.now().plusHours(1)
        )
        
        assertFalse(event.hasExpired())
    }
    
    @Test
    fun `test getDisplayTitle with short title`() {
        val event = CountdownEvent(
            title = "Short Title",
            targetDateTime = LocalDateTime.now().plusDays(1)
        )
        
        assertEquals("Short Title", event.getDisplayTitle())
        assertEquals("Short Title", event.getDisplayTitle(20))
    }
    
    @Test
    fun `test getDisplayTitle with long title truncates`() {
        val event = CountdownEvent(
            title = "This is a very long title that should be truncated",
            targetDateTime = LocalDateTime.now().plusDays(1)
        )
        
        val truncated = event.getDisplayTitle(20)
        assertEquals(20, truncated.length)
        assertTrue(truncated.endsWith("..."))
        assertEquals("This is a very long...", truncated)
    }
    
    @Test
    fun `test getDisplayTitle with exact length`() {
        val event = CountdownEvent(
            title = "Exactly 10",
            targetDateTime = LocalDateTime.now().plusDays(1)
        )
        
        assertEquals("Exactly 10", event.getDisplayTitle(10))
    }
    
    @Test
    fun `test getDaysRemaining for future event`() {
        val now = LocalDateTime.now()
        val targetDate = now.plusDays(5).plusHours(12)
        
        val event = CountdownEvent(
            title = "Future Event",
            targetDateTime = targetDate
        )
        
        val daysRemaining = event.getDaysRemaining()
        assertTrue(daysRemaining >= 5)
    }
    
    @Test
    fun `test getDaysRemaining for past event returns 0`() {
        val event = CountdownEvent(
            title = "Past Event",
            targetDateTime = LocalDateTime.now().minusDays(3)
        )
        
        assertEquals(0L, event.getDaysRemaining())
    }
    
    @Test
    fun `test getDaysRemaining for today`() {
        val now = LocalDateTime.now()
        val laterToday = now.plusHours(6)
        
        val event = CountdownEvent(
            title = "Today Event",
            targetDateTime = laterToday
        )
        
        assertEquals(0L, event.getDaysRemaining())
    }
    
    @Test
    fun `test getDaysRemaining for tomorrow`() {
        val now = LocalDateTime.now()
        val tomorrow = now.plusDays(1)
        
        val event = CountdownEvent(
            title = "Tomorrow Event",
            targetDateTime = tomorrow
        )
        
        val daysRemaining = event.getDaysRemaining()
        assertTrue(daysRemaining == 0L || daysRemaining == 1L)
    }
    
    @Test
    fun `test data class copy functionality`() {
        val original = CountdownEvent(
            id = 1,
            title = "Original",
            description = "Original description",
            targetDateTime = LocalDateTime.now().plusDays(1),
            isActive = true
        )
        
        val modified = original.copy(
            title = "Modified",
            isActive = false
        )
        
        assertEquals(1L, modified.id)
        assertEquals("Modified", modified.title)
        assertEquals("Original description", modified.description)
        assertEquals(original.targetDateTime, modified.targetDateTime)
        assertFalse(modified.isActive)
    }
    
    @Test
    fun `test equals and hashCode`() {
        val date = LocalDateTime.now().plusDays(1)
        val created = LocalDateTime.now()
        
        val event1 = CountdownEvent(
            id = 1,
            title = "Event",
            description = "Description",
            targetDateTime = date,
            createdAt = created,
            isActive = true
        )
        
        val event2 = CountdownEvent(
            id = 1,
            title = "Event",
            description = "Description",
            targetDateTime = date,
            createdAt = created,
            isActive = true
        )
        
        val event3 = CountdownEvent(
            id = 2,
            title = "Event",
            description = "Description",
            targetDateTime = date,
            createdAt = created,
            isActive = true
        )
        
        assertEquals(event1, event2)
        assertEquals(event1.hashCode(), event2.hashCode())
        assertNotEquals(event1, event3)
    }
}