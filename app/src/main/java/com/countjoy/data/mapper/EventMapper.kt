package com.countjoy.data.mapper

import com.countjoy.data.local.entity.CountdownEventEntity
import com.countjoy.domain.model.CountdownEvent
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Maps between domain models and data entities
 */

/**
 * Convert Entity to Domain model
 */
fun CountdownEventEntity.toDomain(): CountdownEvent {
    return CountdownEvent(
        id = id,
        title = title,
        description = description,
        category = category,
        targetDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(targetDateTime),
            ZoneId.systemDefault()
        ),
        reminderEnabled = reminderEnabled,
        reminderTime = reminderTime,
        color = color,
        icon = icon,
        createdAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(createdAt),
            ZoneId.systemDefault()
        ),
        updatedAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(updatedAt),
            ZoneId.systemDefault()
        ),
        isActive = isActive,
        priority = priority
    )
}

/**
 * Convert Domain model to Entity
 */
fun CountdownEvent.toEntity(): CountdownEventEntity {
    return CountdownEventEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        targetDateTime = targetDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        reminderEnabled = reminderEnabled,
        reminderTime = reminderTime,
        color = color,
        icon = icon,
        createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        updatedAt = updatedAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        isActive = isActive,
        priority = priority
    )
}