package com.countjoy.domain.repository

import com.countjoy.domain.model.Milestone
import kotlinx.coroutines.flow.Flow

interface MilestoneRepository {
    fun getMilestonesByEventId(eventId: String): Flow<List<Milestone>>
    suspend fun getUnachievedMilestones(eventId: String): List<Milestone>
    suspend fun getMilestoneById(milestoneId: String): Milestone?
    suspend fun insertMilestone(milestone: Milestone)
    suspend fun insertMilestones(milestones: List<Milestone>)
    suspend fun updateMilestone(milestone: Milestone)
    suspend fun markMilestoneAsAchieved(milestoneId: String, achievedAt: Long)
    suspend fun deleteMilestone(milestone: Milestone)
    suspend fun deleteMilestonesByEventId(eventId: String)
    fun getAchievementHistory(): Flow<List<Milestone>>
}