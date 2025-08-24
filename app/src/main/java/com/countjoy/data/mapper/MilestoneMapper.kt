package com.countjoy.data.mapper

import com.countjoy.data.local.entity.MilestoneEntity
import com.countjoy.domain.model.CelebrationEffect
import com.countjoy.domain.model.Milestone
import com.countjoy.domain.model.MilestoneType

object MilestoneMapper {
    
    fun MilestoneEntity.toDomain(): Milestone {
        return Milestone(
            id = id,
            eventId = eventId,
            type = MilestoneType.valueOf(type),
            value = value,
            title = title,
            message = message,
            isNotificationEnabled = isNotificationEnabled,
            isAchieved = isAchieved,
            achievedAt = achievedAt,
            celebrationEffect = CelebrationEffect.valueOf(celebrationEffect)
        )
    }
    
    fun Milestone.toEntity(): MilestoneEntity {
        return MilestoneEntity(
            id = id,
            eventId = eventId,
            type = type.name,
            value = value,
            title = title,
            message = message,
            isNotificationEnabled = isNotificationEnabled,
            isAchieved = isAchieved,
            achievedAt = achievedAt,
            celebrationEffect = celebrationEffect.name
        )
    }
    
    fun List<MilestoneEntity>.toDomain(): List<Milestone> {
        return map { it.toDomain() }
    }
    
    fun List<Milestone>.toEntity(): List<MilestoneEntity> {
        return map { it.toEntity() }
    }
}