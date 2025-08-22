package com.countjoy.domain.usecase

import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.repository.EventRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Use case for updating existing countdown events
 */
class UpdateEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    /**
     * Updates an existing countdown event
     * @param event The event to update
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(event: CountdownEvent): Result<Unit> {
        return try {
            // Validate event
            when {
                event.id <= 0 -> {
                    return Result.failure(IllegalArgumentException("Invalid event ID"))
                }
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
            
            // Check if event exists
            val existingEvent = repository.getEventById(event.id)
            if (existingEvent == null) {
                return Result.failure(IllegalArgumentException("Event not found"))
            }
            
            // Update the event
            repository.updateEvent(event)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Updates the active status of an event
     * @param id The event ID
     * @param isActive The new active status
     */
    suspend fun updateActiveStatus(id: Long, isActive: Boolean): Result<Unit> {
        return try {
            repository.updateEventActiveStatus(id, isActive)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}