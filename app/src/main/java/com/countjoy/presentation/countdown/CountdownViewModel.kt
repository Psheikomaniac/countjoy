package com.countjoy.presentation.countdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.CountdownTime
import com.countjoy.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject

data class CountdownUiState(
    val events: List<CountdownEventWithTime> = emptyList(),
    val activeEvents: List<CountdownEventWithTime> = emptyList(),
    val expiredEvents: List<CountdownEventWithTime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedFilter: EventFilter = EventFilter.ALL,
    val sortOrder: SortOrder = SortOrder.NEAREST_FIRST
)

data class CountdownEventWithTime(
    val event: CountdownEvent,
    val countdownTime: CountdownTime
)

enum class EventFilter {
    ALL, ACTIVE, EXPIRED, TODAY, THIS_WEEK, THIS_MONTH
}

enum class SortOrder {
    NEAREST_FIRST, FURTHEST_FIRST, ALPHABETICAL, CREATION_DATE
}

@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val getEventUseCase: GetEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val calculateCountdownUseCase: CalculateCountdownUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CountdownUiState())
    val uiState: StateFlow<CountdownUiState> = _uiState.asStateFlow()
    
    private val _uiEvent = MutableSharedFlow<CountdownUiEvent>()
    val uiEvent: SharedFlow<CountdownUiEvent> = _uiEvent.asSharedFlow()
    
    private var countdownUpdateJob: Job? = null
    
    init {
        loadEvents()
        startCountdownUpdates()
    }
    
    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                getEventUseCase.getAllEvents()
                    .catch { exception ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = exception.message
                            )
                        }
                    }
                    .collect { events ->
                        val eventsWithTime = calculateCountdowns(events)
                        val (active, expired) = eventsWithTime.partition { !it.countdownTime.isExpired }
                        
                        _uiState.update {
                            it.copy(
                                events = applyFiltersAndSort(eventsWithTime, it.selectedFilter, it.sortOrder),
                                activeEvents = active,
                                expiredEvents = expired,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
    
    private fun startCountdownUpdates() {
        countdownUpdateJob?.cancel()
        countdownUpdateJob = viewModelScope.launch {
            while (isActive) {
                delay(1000) // Update every second
                updateCountdowns()
            }
        }
    }
    
    private suspend fun updateCountdowns() {
        val currentState = _uiState.value
        if (currentState.events.isNotEmpty()) {
            val updatedEvents = calculateCountdowns(currentState.events.map { it.event })
            val (active, expired) = updatedEvents.partition { !it.countdownTime.isExpired }
            
            _uiState.update {
                it.copy(
                    events = applyFiltersAndSort(updatedEvents, it.selectedFilter, it.sortOrder),
                    activeEvents = active,
                    expiredEvents = expired
                )
            }
        }
    }
    
    private fun calculateCountdowns(events: List<CountdownEvent>): List<CountdownEventWithTime> {
        val currentTime = LocalDateTime.now()
        return events.map { event ->
            CountdownEventWithTime(
                event = event,
                countdownTime = calculateCountdownUseCase(event, currentTime)
            )
        }
    }
    
    private fun applyFiltersAndSort(
        events: List<CountdownEventWithTime>,
        filter: EventFilter,
        sortOrder: SortOrder
    ): List<CountdownEventWithTime> {
        val filtered = when (filter) {
            EventFilter.ALL -> events
            EventFilter.ACTIVE -> events.filter { !it.countdownTime.isExpired && it.event.isActive }
            EventFilter.EXPIRED -> events.filter { it.countdownTime.isExpired }
            EventFilter.TODAY -> events.filter { 
                val now = LocalDateTime.now()
                it.event.targetDateTime.toLocalDate() == now.toLocalDate()
            }
            EventFilter.THIS_WEEK -> events.filter {
                val now = LocalDateTime.now()
                val weekFromNow = now.plusWeeks(1)
                it.event.targetDateTime.isAfter(now) && it.event.targetDateTime.isBefore(weekFromNow)
            }
            EventFilter.THIS_MONTH -> events.filter {
                val now = LocalDateTime.now()
                val monthFromNow = now.plusMonths(1)
                it.event.targetDateTime.isAfter(now) && it.event.targetDateTime.isBefore(monthFromNow)
            }
        }
        
        return when (sortOrder) {
            SortOrder.NEAREST_FIRST -> filtered.sortedBy { it.event.targetDateTime }
            SortOrder.FURTHEST_FIRST -> filtered.sortedByDescending { it.event.targetDateTime }
            SortOrder.ALPHABETICAL -> filtered.sortedBy { it.event.title.lowercase() }
            SortOrder.CREATION_DATE -> filtered.sortedByDescending { it.event.createdAt }
        }
    }
    
    fun deleteEvent(eventId: Long) {
        viewModelScope.launch {
            deleteEventUseCase(eventId).fold(
                onSuccess = {
                    _uiEvent.emit(CountdownUiEvent.EventDeleted)
                },
                onFailure = { exception ->
                    _uiEvent.emit(CountdownUiEvent.ShowError(exception.message ?: "Failed to delete event"))
                }
            )
        }
    }
    
    fun toggleEventActiveStatus(eventId: Long, isActive: Boolean) {
        viewModelScope.launch {
            updateEventUseCase.updateActiveStatus(eventId, isActive).fold(
                onSuccess = {
                    _uiEvent.emit(CountdownUiEvent.EventUpdated)
                },
                onFailure = { exception ->
                    _uiEvent.emit(CountdownUiEvent.ShowError(exception.message ?: "Failed to update event"))
                }
            )
        }
    }
    
    fun setFilter(filter: EventFilter) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedFilter = filter,
                events = applyFiltersAndSort(
                    currentState.events,
                    filter,
                    currentState.sortOrder
                )
            )
        }
    }
    
    fun setSortOrder(sortOrder: SortOrder) {
        _uiState.update { currentState ->
            currentState.copy(
                sortOrder = sortOrder,
                events = applyFiltersAndSort(
                    currentState.events,
                    currentState.selectedFilter,
                    sortOrder
                )
            )
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    override fun onCleared() {
        super.onCleared()
        countdownUpdateJob?.cancel()
    }
}

sealed class CountdownUiEvent {
    object EventDeleted : CountdownUiEvent()
    object EventUpdated : CountdownUiEvent()
    data class ShowError(val message: String) : CountdownUiEvent()
}