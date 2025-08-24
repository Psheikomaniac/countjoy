package com.countjoy.data.local.dao

import androidx.room.*
import com.countjoy.data.local.entity.MilestoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM milestones WHERE eventId = :eventId")
    fun getMilestonesByEventId(eventId: String): Flow<List<MilestoneEntity>>
    
    @Query("SELECT * FROM milestones WHERE eventId = :eventId AND isAchieved = 0")
    suspend fun getUnachievedMilestones(eventId: String): List<MilestoneEntity>
    
    @Query("SELECT * FROM milestones WHERE id = :milestoneId")
    suspend fun getMilestoneById(milestoneId: String): MilestoneEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: MilestoneEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<MilestoneEntity>)
    
    @Update
    suspend fun updateMilestone(milestone: MilestoneEntity)
    
    @Query("UPDATE milestones SET isAchieved = 1, achievedAt = :achievedAt WHERE id = :milestoneId")
    suspend fun markMilestoneAsAchieved(milestoneId: String, achievedAt: Long)
    
    @Delete
    suspend fun deleteMilestone(milestone: MilestoneEntity)
    
    @Query("DELETE FROM milestones WHERE eventId = :eventId")
    suspend fun deleteMilestonesByEventId(eventId: String)
    
    @Query("SELECT * FROM milestones WHERE isAchieved = 1 ORDER BY achievedAt DESC")
    fun getAchievementHistory(): Flow<List<MilestoneEntity>>
}