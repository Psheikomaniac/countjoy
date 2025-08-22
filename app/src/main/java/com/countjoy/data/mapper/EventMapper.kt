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
        targetDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(targetDateTime),
            ZoneId.systemDefault()
        ),
        createdAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(createdAt),
            ZoneId.systemDefault()
        ),
        isActive = isActive
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
        targetDateTime = targetDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        isActive = isActive
    )
}