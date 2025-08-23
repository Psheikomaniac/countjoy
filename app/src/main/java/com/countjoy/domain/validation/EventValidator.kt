package com.countjoy.domain.validation

import com.countjoy.domain.model.CountdownEvent
import java.time.LocalDateTime

/**
 * Validator for countdown events with comprehensive validation rules
 */
object EventValidator {
    
    data class ValidationResult(
        val isValid: Boolean,
        val errors: List<ValidationError> = emptyList()
    )
    
    data class ValidationError(
        val field: String,
        val message: String
    )
    
    /**
     * Validate a countdown event
     */
    fun validate(event: CountdownEvent): ValidationResult {
        val errors = mutableListOf<ValidationError>()
        
        // Title validation
        when {
            event.title.isBlank() -> {
                errors.add(ValidationError("title", "Title cannot be empty"))
            }
            event.title.length < 2 -> {
                errors.add(ValidationError("title", "Title must be at least 2 characters"))
            }
            event.title.length > 100 -> {
                errors.add(ValidationError("title", "Title cannot exceed 100 characters"))
            }
        }
        
        // Description validation
        event.description?.let { desc ->
            if (desc.length > 500) {
                errors.add(ValidationError("description", "Description cannot exceed 500 characters"))
            }
        }
        
        // Category validation
        if (event.category.isBlank()) {
            errors.add(ValidationError("category", "Category cannot be empty"))
        }
        
        // Target date validation
        val now = LocalDateTime.now()
        when {
            event.targetDateTime.isBefore(now.minusYears(10)) -> {
                errors.add(ValidationError("targetDateTime", "Target date is too far in the past"))
            }
            event.targetDateTime.isAfter(now.plusYears(100)) -> {
                errors.add(ValidationError("targetDateTime", "Target date is too far in the future"))
            }
        }
        
        // Reminder validation
        if (event.reminderEnabled && event.reminderTime == null) {
            errors.add(ValidationError("reminderTime", "Reminder time must be set when reminders are enabled"))
        }
        
        event.reminderTime?.let { time ->
            if (time < 0) {
                errors.add(ValidationError("reminderTime", "Reminder time cannot be negative"))
            }
            if (time > 365L * 24 * 60 * 60 * 1000) { // More than 1 year in milliseconds
                errors.add(ValidationError("reminderTime", "Reminder time is too far in advance"))
            }
        }
        
        // Priority validation
        if (event.priority < 0 || event.priority > 10) {
            errors.add(ValidationError("priority", "Priority must be between 0 and 10"))
        }
        
        // Icon validation
        event.icon?.let { icon ->
            if (icon.length > 50) {
                errors.add(ValidationError("icon", "Icon identifier is too long"))
            }
        }
        
        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
    
    /**
     * Sanitize user input for an event
     */
    fun sanitize(event: CountdownEvent): CountdownEvent {
        return event.copy(
            title = event.title.trim().take(100),
            description = event.description?.trim()?.take(500),
            category = event.category.trim().ifBlank { "General" },
            icon = event.icon?.trim()?.take(50),
            priority = event.priority.coerceIn(0, 10)
        )
    }
}