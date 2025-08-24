package com.countjoy.data.repository

import com.countjoy.data.local.dao.MilestoneDao
import com.countjoy.data.mapper.MilestoneMapper.toDomain
import com.countjoy.data.mapper.MilestoneMapper.toEntity
import com.countjoy.domain.model.Milestone
import com.countjoy.domain.repository.MilestoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MilestoneRepositoryImpl @Inject constructor(
    private val milestoneDao: MilestoneDao
) : MilestoneRepository {
    
    override fun getMilestonesByEventId(eventId: String): Flow<List<Milestone>> {
        return milestoneDao.getMilestonesByEventId(eventId).map { entities ->
            entities.toDomain()
        }
    }
    
    override suspend fun getUnachievedMilestones(eventId: String): List<Milestone> {
        return milestoneDao.getUnachievedMilestones(eventId).toDomain()
    }
    
    override suspend fun getMilestoneById(milestoneId: String): Milestone? {
        return milestoneDao.getMilestoneById(milestoneId)?.toDomain()
    }
    
    override suspend fun insertMilestone(milestone: Milestone) {
        milestoneDao.insertMilestone(milestone.toEntity())
    }
    
    override suspend fun insertMilestones(milestones: List<Milestone>) {
        milestoneDao.insertMilestones(milestones.toEntity())
    }
    
    override suspend fun updateMilestone(milestone: Milestone) {
        milestoneDao.updateMilestone(milestone.toEntity())
    }
    
    override suspend fun markMilestoneAsAchieved(milestoneId: String, achievedAt: Long) {
        milestoneDao.markMilestoneAsAchieved(milestoneId, achievedAt)
    }
    
    override suspend fun deleteMilestone(milestone: Milestone) {
        milestoneDao.deleteMilestone(milestone.toEntity())
    }
    
    override suspend fun deleteMilestonesByEventId(eventId: String) {
        milestoneDao.deleteMilestonesByEventId(eventId)
    }
    
    override fun getAchievementHistory(): Flow<List<Milestone>> {
        return milestoneDao.getAchievementHistory().map { entities ->
            entities.toDomain()
        }
    }
}