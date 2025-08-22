package com.countjoy.domain.model

import org.junit.Test
import org.junit.Assert.*

class CountdownTimeTest {
    
    @Test
    fun `test CountdownTime creation with default values`() {
        val countdown = CountdownTime()
        
        assertEquals(0L, countdown.days)
        assertEquals(0, countdown.hours)
        assertEquals(0, countdown.minutes)
        assertEquals(0, countdown.seconds)
        assertEquals(0L, countdown.totalSeconds)
        assertFalse(countdown.isExpired)
    }
    
    @Test
    fun `test CountdownTime creation with all values`() {
        val countdown = CountdownTime(
            days = 5,
            hours = 10,
            minutes = 30,
            seconds = 45,
            totalSeconds = 469845L,
            isExpired = false
        )
        
        assertEquals(5L, countdown.days)
        assertEquals(10, countdown.hours)
        assertEquals(30, countdown.minutes)
        assertEquals(45, countdown.seconds)
        assertEquals(469845L, countdown.totalSeconds)
        assertFalse(countdown.isExpired)
    }
    
    @Test
    fun `test CountdownTime expired state`() {
        val countdown = CountdownTime(isExpired = true)
        
        assertEquals(0L, countdown.days)
        assertEquals(0, countdown.hours)
        assertEquals(0, countdown.minutes)
        assertEquals(0, countdown.seconds)
        assertEquals(0L, countdown.totalSeconds)
        assertTrue(countdown.isExpired)
    }
    
    @Test
    fun `test fromSeconds with 0 seconds returns expired`() {
        val countdown = CountdownTime.fromSeconds(0)
        
        assertTrue(countdown.isExpired)
        assertEquals(0L, countdown.totalSeconds)
    }
    
    @Test
    fun `test fromSeconds with negative seconds returns expired`() {
        val countdown = CountdownTime.fromSeconds(-100)
        
        assertTrue(countdown.isExpired)
        assertEquals(0L, countdown.totalSeconds)
    }
    
    @Test
    fun `test fromSeconds with seconds only`() {
        val countdown = CountdownTime.fromSeconds(45)
        
        assertEquals(0L, countdown.days)
        assertEquals(0, countdown.hours)
        assertEquals(0, countdown.minutes)
        assertEquals(45, countdown.seconds)
        assertEquals(45L, countdown.totalSeconds)
        assertFalse(countdown.isExpired)
    }
    
    @Test
    fun `test fromSeconds with minutes and seconds`() {
        val countdown = CountdownTime.fromSeconds(125) // 2 minutes 5 seconds
        
        assertEquals(0L, countdown.days)
        assertEquals(0, countdown.hours)
        assertEquals(2, countdown.minutes)
        assertEquals(5, countdown.seconds)
        assertEquals(125L, countdown.totalSeconds)
        assertFalse(countdown.isExpired)
    }
    
    @Test
    fun `test fromSeconds with hours minutes and seconds`() {
        val countdown = CountdownTime.fromSeconds(7325) // 2 hours 2 minutes 5 seconds
        
        assertEquals(0L, countdown.days)
        assertEquals(2, countdown.hours)
        assertEquals(2, countdown.minutes)
        assertEquals(5, countdown.seconds)
        assertEquals(7325L, countdown.totalSeconds)
        assertFalse(countdown.isExpired)
    }
    
    @Test
    fun `test fromSeconds with days hours minutes and seconds`() {
        val countdown = CountdownTime.fromSeconds(93785) // 1 day 2 hours 3 minutes 5 seconds
        
        assertEquals(1L, countdown.days)
        assertEquals(2, countdown.hours)
        assertEquals(3, countdown.minutes)
        assertEquals(5, countdown.seconds)
        assertEquals(93785L, countdown.totalSeconds)
        assertFalse(countdown.isExpired)
    }
    
    @Test
    fun `test fromSeconds with large number`() {
        val countdown = CountdownTime.fromSeconds(864000) // Exactly 10 days
        
        assertEquals(10L, countdown.days)
        assertEquals(0, countdown.hours)
        assertEquals(0, countdown.minutes)
        assertEquals(0, countdown.seconds)
        assertEquals(864000L, countdown.totalSeconds)
        assertFalse(countdown.isExpired)
    }
    
    @Test
    fun `test toFormattedString when expired`() {
        val countdown = CountdownTime(isExpired = true)
        
        assertEquals("Expired", countdown.toFormattedString())
        assertEquals("Expired", countdown.toFormattedString(false))
    }
    
    @Test
    fun `test toFormattedString with days`() {
        val countdown = CountdownTime(
            days = 5,
            hours = 10,
            minutes = 30,
            seconds = 45,
            totalSeconds = 469845L
        )
        
        assertEquals("5d 10h 30m 45s", countdown.toFormattedString(true))
        assertEquals("5d 10h 30m", countdown.toFormattedString(false))
    }
    
    @Test
    fun `test toFormattedString with hours only`() {
        val countdown = CountdownTime(
            hours = 3,
            minutes = 15,
            seconds = 20,
            totalSeconds = 11720L
        )
        
        assertEquals("03h 15m 20s", countdown.toFormattedString(true))
        assertEquals("03h 15m", countdown.toFormattedString(false))
    }
    
    @Test
    fun `test toFormattedString with minutes only`() {
        val countdown = CountdownTime(
            minutes = 45,
            seconds = 30,
            totalSeconds = 2730L
        )
        
        assertEquals("45m 30s", countdown.toFormattedString(true))
        assertEquals("45m", countdown.toFormattedString(false))
    }
    
    @Test
    fun `test toFormattedString with seconds only`() {
        val countdown = CountdownTime(
            seconds = 59,
            totalSeconds = 59L
        )
        
        assertEquals("59s", countdown.toFormattedString(true))
        assertEquals("59s", countdown.toFormattedString(false))
    }
    
    @Test
    fun `test toFormattedString with single digit values`() {
        val countdown = CountdownTime(
            days = 1,
            hours = 2,
            minutes = 3,
            seconds = 4,
            totalSeconds = 93784L
        )
        
        assertEquals("1d 02h 03m 04s", countdown.toFormattedString(true))
        assertEquals("1d 02h 03m", countdown.toFormattedString(false))
    }
    
    @Test
    fun `test toShortString when expired`() {
        val countdown = CountdownTime(isExpired = true)
        
        assertEquals("Expired", countdown.toShortString())
    }
    
    @Test
    fun `test toShortString with days`() {
        val countdown = CountdownTime(
            days = 5,
            hours = 10,
            minutes = 30,
            totalSeconds = 469800L
        )
        
        assertEquals("5 days", countdown.toShortString())
    }
    
    @Test
    fun `test toShortString with hours`() {
        val countdown = CountdownTime(
            hours = 12,
            minutes = 30,
            totalSeconds = 45000L
        )
        
        assertEquals("12 hours", countdown.toShortString())
    }
    
    @Test
    fun `test toShortString with minutes`() {
        val countdown = CountdownTime(
            minutes = 45,
            seconds = 30,
            totalSeconds = 2730L
        )
        
        assertEquals("45 minutes", countdown.toShortString())
    }
    
    @Test
    fun `test toShortString with seconds`() {
        val countdown = CountdownTime(
            seconds = 30,
            totalSeconds = 30L
        )
        
        assertEquals("30 seconds", countdown.toShortString())
    }
    
    @Test
    fun `test toShortString with 1 day`() {
        val countdown = CountdownTime(
            days = 1,
            hours = 5,
            totalSeconds = 104400L
        )
        
        assertEquals("1 days", countdown.toShortString())
    }
    
    @Test
    fun `test toShortString with 1 hour`() {
        val countdown = CountdownTime(
            hours = 1,
            minutes = 30,
            totalSeconds = 5400L
        )
        
        assertEquals("1 hours", countdown.toShortString())
    }
    
    @Test
    fun `test toShortString with 1 minute`() {
        val countdown = CountdownTime(
            minutes = 1,
            seconds = 30,
            totalSeconds = 90L
        )
        
        assertEquals("1 minutes", countdown.toShortString())
    }
    
    @Test
    fun `test toShortString with 1 second`() {
        val countdown = CountdownTime(
            seconds = 1,
            totalSeconds = 1L
        )
        
        assertEquals("1 seconds", countdown.toShortString())
    }
    
    @Test
    fun `test data class copy functionality`() {
        val original = CountdownTime(
            days = 5,
            hours = 10,
            minutes = 30,
            seconds = 45,
            totalSeconds = 469845L,
            isExpired = false
        )
        
        val modified = original.copy(
            days = 3,
            isExpired = true
        )
        
        assertEquals(3L, modified.days)
        assertEquals(10, modified.hours)
        assertEquals(30, modified.minutes)
        assertEquals(45, modified.seconds)
        assertEquals(469845L, modified.totalSeconds)
        assertTrue(modified.isExpired)
    }
    
    @Test
    fun `test equals and hashCode`() {
        val time1 = CountdownTime(
            days = 5,
            hours = 10,
            minutes = 30,
            seconds = 45,
            totalSeconds = 469845L,
            isExpired = false
        )
        
        val time2 = CountdownTime(
            days = 5,
            hours = 10,
            minutes = 30,
            seconds = 45,
            totalSeconds = 469845L,
            isExpired = false
        )
        
        val time3 = CountdownTime(
            days = 5,
            hours = 10,
            minutes = 30,
            seconds = 45,
            totalSeconds = 469845L,
            isExpired = true
        )
        
        assertEquals(time1, time2)
        assertEquals(time1.hashCode(), time2.hashCode())
        assertNotEquals(time1, time3)
    }
}