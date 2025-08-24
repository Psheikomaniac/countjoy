package com.countjoy.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "milestones",
    foreignKeys = [
        ForeignKey(
            entity = CountdownEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("eventId")]
)
data class MilestoneEntity(
    @PrimaryKey
    val id: String,
    val eventId: String,
    val type: String, // MilestoneType enum as string
    val value: Float,
    val title: String,
    val message: String,
    val isNotificationEnabled: Boolean,
    val isAchieved: Boolean,
    val achievedAt: Instant?,
    val celebrationEffect: String // CelebrationEffect enum as string
)