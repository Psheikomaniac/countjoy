package com.countjoy.domain.model

import java.time.Instant
import java.util.UUID

data class Milestone(
    val id: String = UUID.randomUUID().toString(),
    val eventId: String,
    val type: MilestoneType,
    val value: Float, // Percentage (0-100) or days
    val title: String,
    val message: String,
    val isNotificationEnabled: Boolean = true,
    val isAchieved: Boolean = false,
    val achievedAt: Instant? = null,
    val celebrationEffect: CelebrationEffect = CelebrationEffect.CONFETTI
)

enum class MilestoneType {
    PERCENTAGE_BASED,
    TIME_BASED,
    CUSTOM
}

enum class CelebrationEffect {
    NONE,
    CONFETTI,
    FIREWORKS,
    STARS,
    BALLOONS,
    SPARKLES
}

object MilestoneTemplates {
    val percentageTemplates = listOf(
        MilestoneTemplate(
            type = MilestoneType.PERCENTAGE_BASED,
            value = 10f,
            title = "Just Started!",
            message = "Great beginning! 10% complete"
        ),
        MilestoneTemplate(
            type = MilestoneType.PERCENTAGE_BASED,
            value = 25f,
            title = "Quarter Way!",
            message = "You're 25% there! Keep going!"
        ),
        MilestoneTemplate(
            type = MilestoneType.PERCENTAGE_BASED,
            value = 50f,
            title = "Halfway There!",
            message = "Amazing! You've reached the halfway point!"
        ),
        MilestoneTemplate(
            type = MilestoneType.PERCENTAGE_BASED,
            value = 75f,
            title = "Three Quarters!",
            message = "Almost there! 75% complete!"
        ),
        MilestoneTemplate(
            type = MilestoneType.PERCENTAGE_BASED,
            value = 90f,
            title = "Final Stretch!",
            message = "You're in the final stretch! 90% complete!"
        )
    )
    
    val timeTemplates = listOf(
        MilestoneTemplate(
            type = MilestoneType.TIME_BASED,
            value = 365f,
            title = "One Year to Go!",
            message = "365 days remaining"
        ),
        MilestoneTemplate(
            type = MilestoneType.TIME_BASED,
            value = 100f,
            title = "100 Days!",
            message = "100 days left on your countdown"
        ),
        MilestoneTemplate(
            type = MilestoneType.TIME_BASED,
            value = 30f,
            title = "One Month!",
            message = "Just 30 days to go!"
        ),
        MilestoneTemplate(
            type = MilestoneType.TIME_BASED,
            value = 7f,
            title = "One Week!",
            message = "Only 7 days remaining!"
        ),
        MilestoneTemplate(
            type = MilestoneType.TIME_BASED,
            value = 1f,
            title = "Tomorrow!",
            message = "Just 1 day left!"
        )
    )
}

data class MilestoneTemplate(
    val type: MilestoneType,
    val value: Float,
    val title: String,
    val message: String
)