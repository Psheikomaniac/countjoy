package com.countjoy.presentation.milestone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.domain.model.Milestone
import com.countjoy.domain.repository.MilestoneRepository
import com.countjoy.domain.usecase.milestone.CheckMilestonesUseCase
import com.countjoy.domain.usecase.milestone.CreateMilestonesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val milestoneRepository: MilestoneRepository,
    private val createMilestonesUseCase: CreateMilestonesUseCase,
    private val checkMilestonesUseCase: CheckMilestonesUseCase
) : ViewModel() {
    
    private val _achievedMilestones = MutableStateFlow<List<CheckMilestonesUseCase.AchievedMilestone>>(emptyList())
    val achievedMilestones: StateFlow<List<CheckMilestonesUseCase.AchievedMilestone>> = _achievedMilestones.asStateFlow()
    
    private val _achievementHistory = MutableStateFlow<List<Milestone>>(emptyList())
    val achievementHistory: StateFlow<List<Milestone>> = _achievementHistory.asStateFlow()
    
    init {
        loadAchievementHistory()
    }
    
    fun getMilestonesForEvent(eventId: String): Flow<List<Milestone>> {
        return milestoneRepository.getMilestonesByEventId(eventId)
    }
    
    fun createMilestones(
        eventId: String,
        usePercentageTemplates: Boolean = true,
        useTimeTemplates: Boolean = false,
        customMilestones: List<Milestone> = emptyList()
    ) {
        viewModelScope.launch {
            createMilestonesUseCase(
                eventId = eventId,
                usePercentageTemplates = usePercentageTemplates,
                useTimeTemplates = useTimeTemplates,
                customMilestones = customMilestones
            )
        }
    }
    
    fun checkMilestones(eventId: String) {
        viewModelScope.launch {
            val achieved = checkMilestonesUseCase(eventId)
            _achievedMilestones.value = achieved
        }
    }
    
    fun deleteMilestone(milestone: Milestone) {
        viewModelScope.launch {
            milestoneRepository.deleteMilestone(milestone)
        }
    }
    
    fun clearAchievedMilestones() {
        _achievedMilestones.value = emptyList()
    }
    
    private fun loadAchievementHistory() {
        viewModelScope.launch {
            milestoneRepository.getAchievementHistory()
                .collect { history ->
                    _achievementHistory.value = history
                }
        }
    }
}