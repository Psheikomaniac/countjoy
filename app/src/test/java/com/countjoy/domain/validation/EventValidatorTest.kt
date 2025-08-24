package com.countjoy.domain.validation

import com.countjoy.domain.model.CountdownEvent
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import java.time.LocalDateTime

class EventValidatorTest {
    
    @Test
    fun `validate returns valid for correct event`() {
        // Given
        val event = createValidEvent()
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertTrue(result.isValid)
        assertTrue(result.errors.isEmpty())
    }
    
    @Test
    fun `validate returns error for blank title`() {
        // Given
        val event = createValidEvent().copy(title = "")
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals(1, result.errors.size)
        assertEquals("title", result.errors[0].field)
        assertEquals("Title cannot be empty", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for short title`() {
        // Given
        val event = createValidEvent().copy(title = "A")
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals("Title must be at least 2 characters", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for long title`() {
        // Given
        val event = createValidEvent().copy(title = "A".repeat(101))
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals("Title cannot exceed 100 characters", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for long description`() {
        // Given
        val event = createValidEvent().copy(description = "A".repeat(501))
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals("Description cannot exceed 500 characters", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for blank category`() {
        // Given
        val event = createValidEvent().copy(category = "")
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals("Category cannot be empty", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for date too far in past`() {
        // Given
        val event = createValidEvent().copy(targetDateTime = LocalDateTime.now().minusYears(11))
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals("Target date is too far in the past", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for date too far in future`() {
        // Given
        val event = createValidEvent().copy(targetDateTime = LocalDateTime.now().plusYears(101))
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals("Target date is too far in the future", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for enabled reminder without time`() {
        // Given
        val event = createValidEvent().copy(reminderEnabled = true, reminderTime = null)
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals("Reminder time must be set when reminders are enabled", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for negative reminder time`() {
        // Given
        val event = createValidEvent().copy(reminderEnabled = true, reminderTime = -1)
        
        // When
        val result = EventValidator.validate(event)
        
        // Then
        assertFalse(result.isValid)
        assertEquals("Reminder time cannot be negative", result.errors[0].message)
    }
    
    @Test
    fun `validate returns error for invalid priority`() {
        // Given
        val eventLow = createValidEvent().copy(priority = -1)
        val eventHigh = createValidEvent().copy(priority = 11)
        
        // When
        val resultLow = EventValidator.validate(eventLow)
        val resultHigh = EventValidator.validate(eventHigh)
        
        // Then
        assertFalse(resultLow.isValid)
        assertFalse(resultHigh.isValid)
        assertEquals("Priority must be between 0 and 10", resultLow.errors[0].message)
        assertEquals("Priority must be between 0 and 10", resultHigh.errors[0].message)
    }
    
    @Test
    fun `sanitize trims and limits fields`() {
        // Given
        val event = createValidEvent().copy(
            title = "  Title with spaces  ",
            description = "  Description  ",
            category = "  Category  ",
            icon = "  icon  ",
            priority = 15
        )
        
        // When
        val result = EventValidator.sanitize(event)
        
        // Then
        assertEquals("Title with spaces", result.title)
        assertEquals("Description", result.description)
        assertEquals("Category", result.category)
        assertEquals("icon", result.icon)
        assertEquals(10, result.priority)
    }
    
    @Test
    fun `sanitize truncates long title`() {
        // Given
        val longTitle = "A".repeat(105)
        val event = createValidEvent().copy(title = longTitle)
        
        // When
        val result = EventValidator.sanitize(event)
        
        // Then
        assertEquals(100, result.title.length)
    }
    
    @Test
    fun `sanitize sets default category for blank`() {
        // Given
        val event = createValidEvent().copy(category = "   ")
        
        // When
        val result = EventValidator.sanitize(event)
        
        // Then
        assertEquals("General", result.category)
    }
    
    private fun createValidEvent(): CountdownEvent {
        return CountdownEvent(
            id = 1,
            title = "Valid Event",
            description = "Valid description",
            category = "General",
            targetDateTime = LocalDateTime.now().plusDays(7),
            reminderEnabled = false,
            reminderTime = null,
            color = null,
            icon = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isActive = true,
            priority = 5
        )
    }
}