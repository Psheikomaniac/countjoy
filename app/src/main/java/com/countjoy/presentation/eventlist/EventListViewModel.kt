package com.countjoy.presentation.eventlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.EventCategory
import com.countjoy.domain.model.EventPriority
import com.countjoy.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    
    // Search and filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<EventCategory?>(null)
    val selectedCategory: StateFlow<EventCategory?> = _selectedCategory.asStateFlow()
    
    private val _selectedPriority = MutableStateFlow<EventPriority?>(null)
    val selectedPriority: StateFlow<EventPriority?> = _selectedPriority.asStateFlow()
    
    private val _sortBy = MutableStateFlow(SortOption.DATE)
    val sortBy: StateFlow<SortOption> = _sortBy.asStateFlow()
    
    private val _showPastEvents = MutableStateFlow(false)
    val showPastEvents: StateFlow<Boolean> = _showPastEvents.asStateFlow()
    
    // UI state
    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState: StateFlow<EventListUiState> = _uiState.asStateFlow()
    
    // Combined flow for filtered events
    val filteredEvents: StateFlow<List<CountdownEvent>> = 
        eventRepository.getAllActiveEvents().combine(_searchQuery) { events, query ->
            Pair(events, query)
        }.combine(_selectedCategory) { (events, query), category ->
            Triple(events, query, category)
        }.combine(_selectedPriority) { (events, query, category), priority ->
            Pair(Pair(events, query), Pair(category, priority))
        }.combine(_sortBy) { (eventsQuery, categoryPriority), sortOption ->
            Triple(eventsQuery, categoryPriority, sortOption)
        }.combine(_showPastEvents) { (eventsQuery, categoryPriority, sortOption), showPast ->
            val (events, query) = eventsQuery
            val (category, priority) = categoryPriority
            filterAndSortEvents(events, query, category, priority, sortOption, showPast)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Categories from database
    val availableCategories: StateFlow<List<String>> = eventRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    init {
        loadEvents()
    }
    
    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Events are loaded through the filteredEvents flow
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load events"
                    )
                }
            }
        }
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    fun onCategorySelected(category: EventCategory?) {
        _selectedCategory.value = category
    }
    
    fun onPrioritySelected(priority: EventPriority?) {
        _selectedPriority.value = priority
    }
    
    fun onSortOptionChanged(sortOption: SortOption) {
        _sortBy.value = sortOption
    }
    
    fun toggleShowPastEvents() {
        _showPastEvents.value = !_showPastEvents.value
    }
    
    fun deleteEvent(event: CountdownEvent) {
        viewModelScope.launch {
            try {
                eventRepository.deleteEvent(event)
                _uiState.update { 
                    it.copy(
                        successMessage = "Event deleted successfully"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "Failed to delete event"
                    )
                }
            }
        }
    }
    
    fun duplicateEvent(event: CountdownEvent) {
        viewModelScope.launch {
            try {
                val duplicatedEvent = event.copy(
                    id = 0,
                    title = "${event.title} (Copy)",
                    createdAt = java.time.LocalDateTime.now(),
                    updatedAt = java.time.LocalDateTime.now()
                )
                eventRepository.createEvent(duplicatedEvent)
                _uiState.update { 
                    it.copy(
                        successMessage = "Event duplicated successfully"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "Failed to duplicate event"
                    )
                }
            }
        }
    }
    
    fun updateEventPriority(event: CountdownEvent, priority: EventPriority) {
        viewModelScope.launch {
            try {
                val updatedEvent = event.copy(priority = priority.value)
                eventRepository.updateEvent(updatedEvent)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "Failed to update priority"
                    )
                }
            }
        }
    }
    
    fun clearMessages() {
        _uiState.update { 
            it.copy(
                error = null,
                successMessage = null
            )
        }
    }
    
    private fun filterAndSortEvents(
        events: List<CountdownEvent>,
        query: String,
        category: EventCategory?,
        priority: EventPriority?,
        sortOption: SortOption,
        showPast: Boolean
    ): List<CountdownEvent> {
        var filteredList = events
        
        // Filter by search query
        if (query.isNotBlank()) {
            filteredList = filteredList.filter { event ->
                event.title.contains(query, ignoreCase = true) ||
                event.description?.contains(query, ignoreCase = true) == true
            }
        }
        
        // Filter by category
        if (category != null) {
            filteredList = filteredList.filter { it.category == category.displayName }
        }
        
        // Filter by priority
        if (priority != null) {
            filteredList = filteredList.filter { it.priority == priority.value }
        }
        
        // Filter past events
        if (!showPast) {
            filteredList = filteredList.filter { !it.hasExpired() }
        }
        
        // Sort events
        return when (sortOption) {
            SortOption.DATE -> filteredList.sortedBy { it.targetDateTime }
            SortOption.NAME -> filteredList.sortedBy { it.title.lowercase() }
            SortOption.PRIORITY -> filteredList.sortedByDescending { it.priority }
            SortOption.CATEGORY -> filteredList.sortedBy { it.category }
        }
    }
}

data class EventListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

enum class SortOption(val displayName: String) {
    DATE("Date"),
    NAME("Name"),
    PRIORITY("Priority"),
    CATEGORY("Category")
}