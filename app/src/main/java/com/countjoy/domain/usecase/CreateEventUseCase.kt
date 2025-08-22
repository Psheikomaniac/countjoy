package com.countjoy.domain.usecase

import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.repository.EventRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Use case for creating new countdown events with validation
 */
class CreateEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    /**
     * Creates a new countdown event
     * @param event The event to create
     * @return The ID of the created event
     * @throws IllegalArgumentException if validation fails
     */
    suspend operator fun invoke(event: CountdownEvent): Result<Long> {
        return try {
            // Validate event
            when {
                event.title.isBlank() -> {
                    return Result.failure(IllegalArgumentException("Event title cannot be empty"))
                }
                event.title.length > 100 -> {
                    return Result.failure(IllegalArgumentException("Event title cannot exceed 100 characters"))
                }
                event.targetDateTime.isBefore(LocalDateTime.now()) -> {
                    return Result.failure(IllegalArgumentException("Target date must be in the future"))
                }
                event.description != null && event.description.length > 500 -> {
                    return Result.failure(IllegalArgumentException("Description cannot exceed 500 characters"))
                }
            }
            
            // Create the event
            val id = repository.createEvent(event)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}