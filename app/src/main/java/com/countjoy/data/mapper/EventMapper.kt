package com.countjoy.data.mapper

import com.countjoy.data.local.entity.CountdownEventEntity
import com.countjoy.domain.model.CountdownEvent

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
        targetDateTime = targetDateTime,
        createdAt = createdAt,
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
        targetDateTime = targetDateTime,
        createdAt = createdAt,
        isActive = isActive
    )
}