package com.countjoy.data.local.dao

import androidx.room.*
import com.countjoy.data.local.entity.CountdownEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountdownEventDao {
    
    @Query("SELECT * FROM countdown_events WHERE isActive = 1 ORDER BY targetDateTime ASC")
    fun getAllActiveEvents(): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT * FROM countdown_events ORDER BY targetDateTime ASC")
    fun getAllEvents(): Flow<List<CountdownEventEntity>>
    
    @Query("SELECT * FROM countdown_events WHERE id = :id")
    suspend fun getEventById(id: Long): CountdownEventEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CountdownEventEntity): Long
    
    @Update
    suspend fun updateEvent(event: CountdownEventEntity)
    
    @Delete
    suspend fun deleteEvent(event: CountdownEventEntity)
    
    @Query("DELETE FROM countdown_events WHERE id = :id")
    suspend fun deleteEventById(id: Long)
    
    @Query("UPDATE countdown_events SET isActive = :isActive WHERE id = :id")
    suspend fun updateEventActiveStatus(id: Long, isActive: Boolean)
}