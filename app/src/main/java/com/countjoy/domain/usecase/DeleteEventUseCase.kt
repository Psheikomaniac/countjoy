package com.countjoy.domain.usecase

import com.countjoy.domain.repository.EventRepository
import javax.inject.Inject

/**
 * Use case for deleting countdown events
 */
class DeleteEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    /**
     * Deletes a countdown event by ID
     * @param id The ID of the event to delete
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(id: Long): Result<Unit> {
        return try {
            if (id <= 0) {
                return Result.failure(IllegalArgumentException("Invalid event ID"))
            }
            
            // Check if event exists
            val existingEvent = repository.getEventById(id)
            if (existingEvent == null) {
                return Result.failure(IllegalArgumentException("Event not found"))
            }
            
            // Delete the event
            repository.deleteEvent(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}