package com.countjoy.domain.usecase.milestone

import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.Milestone
import com.countjoy.domain.model.MilestoneType
import com.countjoy.domain.repository.EventRepository
import com.countjoy.domain.repository.MilestoneRepository
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class CheckMilestonesUseCase @Inject constructor(
    private val milestoneRepository: MilestoneRepository,
    private val eventRepository: EventRepository
) {
    
    data class AchievedMilestone(
        val milestone: Milestone,
        val event: CountdownEvent
    )
    
    suspend operator fun invoke(eventId: String): List<AchievedMilestone> {
        val event = eventRepository.getEvent(eventId) ?: return emptyList()
        val unachievedMilestones = milestoneRepository.getUnachievedMilestones(eventId)
        val achievedMilestones = mutableListOf<AchievedMilestone>()
        
        val now = Instant.now()
        val totalDuration = Duration.between(event.createdAt, event.targetDateTime)
        val elapsedDuration = Duration.between(event.createdAt, now)
        
        for (milestone in unachievedMilestones) {
            val isAchieved = when (milestone.type) {
                MilestoneType.PERCENTAGE_BASED -> {
                    val progressPercentage = if (totalDuration.isZero) {
                        100f
                    } else {
                        (elapsedDuration.toMillis().toFloat() / totalDuration.toMillis().toFloat()) * 100f
                    }
                    progressPercentage >= milestone.value
                }
                MilestoneType.TIME_BASED -> {
                    val remainingDays = Duration.between(now, event.targetDateTime).toDays()
                    remainingDays <= milestone.value.toLong() && remainingDays >= 0
                }
                MilestoneType.CUSTOM -> {
                    // Custom milestones need specific logic based on the event
                    // For now, we'll skip custom milestones
                    false
                }
            }
            
            if (isAchieved) {
                milestoneRepository.markMilestoneAsAchieved(milestone.id, now.toEpochMilli())
                achievedMilestones.add(
                    AchievedMilestone(
                        milestone = milestone.copy(
                            isAchieved = true,
                            achievedAt = now
                        ),
                        event = event
                    )
                )
            }
        }
        
        return achievedMilestones
    }
}