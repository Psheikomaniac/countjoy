package com.countjoy.presentation.event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.usecase.CreateEventUseCase
import com.countjoy.domain.usecase.GetEventUseCase
import com.countjoy.domain.usecase.UpdateEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class EventInputUiState(
    val eventId: Long? = null,
    val title: String = "",
    val description: String = "",
    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val dateError: String? = null,
    val timeError: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isEditMode: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val enableNotification: Boolean = true,
    val notificationMinutesBefore: Int = 15
)

@HiltViewModel
class EventInputViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getEventUseCase: GetEventUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(EventInputUiState())
    val uiState: StateFlow<EventInputUiState> = _uiState.asStateFlow()
    
    private val _uiEvent = Channel<EventInputUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()
    
    init {
        val eventId = savedStateHandle.get<String>("eventId")?.toLongOrNull()
        if (eventId != null && eventId != -1L) {
            loadEvent(eventId)
        } else {
            // Set default date to tomorrow
            _uiState.update {
                it.copy(
                    selectedDate = LocalDate.now().plusDays(1),
                    selectedTime = LocalTime.of(12, 0)
                )
            }
        }
    }
    
    private fun loadEvent(eventId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                getEventUseCase.getEventById(eventId)?.let { event ->
                    _uiState.update {
                        EventInputUiState(
                            eventId = event.id,
                            title = event.title,
                            description = event.description ?: "",
                            selectedDate = event.targetDateTime.toLocalDate(),
                            selectedTime = event.targetDateTime.toLocalTime(),
                            isLoading = false,
                            isEditMode = true
                        )
                    }
                } ?: run {
                    _uiEvent.send(EventInputUiEvent.ShowError("Event not found"))
                    _uiEvent.send(EventInputUiEvent.NavigateBack)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _uiEvent.send(EventInputUiEvent.ShowError(e.message ?: "Failed to load event"))
            }
        }
    }
    
    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            titleError = null
        )
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }
    
    fun updateDate(date: LocalDate?) {
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            dateError = null
        )
    }
    
    fun updateTime(time: LocalTime?) {
        _uiState.value = _uiState.value.copy(selectedTime = time)
    }
    
    fun saveEvent() {
        if (!validateInput()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val state = _uiState.value
            val dateTime = LocalDateTime.of(
                state.selectedDate!!,
                state.selectedTime ?: LocalTime.of(0, 0)
            )
            
            val event = CountdownEvent(
                id = state.eventId ?: 0,
                title = state.title.trim(),
                description = state.description.trim().takeIf { it.isNotEmpty() },
                targetDateTime = dateTime,
                createdAt = if (state.isEditMode) LocalDateTime.now() else LocalDateTime.now(),
                isActive = true
            )
            
            val result = if (state.isEditMode) {
                updateEventUseCase(event)
            } else {
                createEventUseCase(event).map { Unit }
            }
            
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSaved = true
                        )
                    }
                    _uiEvent.send(EventInputUiEvent.SaveSuccess)
                    _uiEvent.send(EventInputUiEvent.NavigateBack)
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.send(EventInputUiEvent.ShowError(exception.message ?: "Failed to save event"))
                }
            )
        }
    }
    
    private fun validateInput(): Boolean {
        val state = _uiState.value
        val errors = mutableMapOf<String, String>()
        
        // Title validation
        when {
            state.title.isBlank() -> errors["title"] = "Event name is required"
            state.title.length < 3 -> errors["title"] = "Event name must be at least 3 characters"
            state.title.length > 100 -> errors["title"] = "Event name cannot exceed 100 characters"
        }
        
        // Description validation
        if (state.description.length > 500) {
            errors["description"] = "Description cannot exceed 500 characters"
        }
        
        // Date validation
        when {
            state.selectedDate == null -> errors["date"] = "Date is required"
            !state.isEditMode && state.selectedDate.isBefore(LocalDate.now()) -> {
                errors["date"] = "Date cannot be in the past"
            }
        }
        
        // Time validation for today's date
        if (state.selectedDate == LocalDate.now() && state.selectedTime != null) {
            if (state.selectedTime.isBefore(LocalTime.now())) {
                errors["time"] = "Time cannot be in the past for today"
            }
        }
        
        // Update UI state with errors
        _uiState.update {
            it.copy(
                titleError = errors["title"],
                descriptionError = errors["description"],
                dateError = errors["date"],
                timeError = errors["time"]
            )
        }
        
        return errors.isEmpty()
    }
    
    fun showDatePicker() {
        _uiState.update { it.copy(showDatePicker = true) }
    }
    
    fun hideDatePicker() {
        _uiState.update { it.copy(showDatePicker = false) }
    }
    
    fun showTimePicker() {
        _uiState.update { it.copy(showTimePicker = true) }
    }
    
    fun hideTimePicker() {
        _uiState.update { it.copy(showTimePicker = false) }
    }
    
    fun updateNotificationSettings(enabled: Boolean, minutesBefore: Int = 15) {
        _uiState.update {
            it.copy(
                enableNotification = enabled,
                notificationMinutesBefore = minutesBefore
            )
        }
    }
    
    fun getFormattedDate(): String {
        val date = _uiState.value.selectedDate ?: return "Select date"
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    }
    
    fun getFormattedTime(): String {
        val time = _uiState.value.selectedTime ?: return "Select time (optional)"
        return time.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }
}

sealed class EventInputUiEvent {
    object SaveSuccess : EventInputUiEvent()
    object NavigateBack : EventInputUiEvent()
    data class ShowError(val message: String) : EventInputUiEvent()
    data class ShowSnackbar(val message: String) : EventInputUiEvent()