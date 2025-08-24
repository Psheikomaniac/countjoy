package com.countjoy.domain.usecase.milestone

import com.countjoy.domain.model.Milestone
import com.countjoy.domain.model.MilestoneTemplates
import com.countjoy.domain.model.MilestoneType
import com.countjoy.domain.repository.MilestoneRepository
import java.util.UUID
import javax.inject.Inject

class CreateMilestonesUseCase @Inject constructor(
    private val repository: MilestoneRepository
) {
    suspend operator fun invoke(
        eventId: String,
        usePercentageTemplates: Boolean = true,
        useTimeTemplates: Boolean = false,
        customMilestones: List<Milestone> = emptyList()
    ) {
        val milestones = mutableListOf<Milestone>()
        
        // Add percentage-based templates
        if (usePercentageTemplates) {
            milestones.addAll(
                MilestoneTemplates.percentageTemplates.map { template ->
                    Milestone(
                        id = UUID.randomUUID().toString(),
                        eventId = eventId,
                        type = template.type,
                        value = template.value,
                        title = template.title,
                        message = template.message
                    )
                }
            )
        }
        
        // Add time-based templates
        if (useTimeTemplates) {
            milestones.addAll(
                MilestoneTemplates.timeTemplates.map { template ->
                    Milestone(
                        id = UUID.randomUUID().toString(),
                        eventId = eventId,
                        type = template.type,
                        value = template.value,
                        title = template.title,
                        message = template.message
                    )
                }
            )
        }
        
        // Add custom milestones
        milestones.addAll(customMilestones)
        
        // Insert all milestones
        if (milestones.isNotEmpty()) {
            repository.insertMilestones(milestones)
        }
    }
}