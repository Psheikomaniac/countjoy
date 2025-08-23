package com.countjoy.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.data.local.preferences.SharedPreferencesManager
import com.countjoy.service.SoundHapticService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SoundHapticUiState(
    val soundEnabled: Boolean = true,
    val soundVolume: Float = 0.7f,
    val hapticEnabled: Boolean = true,
    val hapticIntensity: Int = 128,
    val milestoneNotifications: Boolean = true,
    val completionCelebration: Boolean = true,
    val buttonClickFeedback: Boolean = false
)

@HiltViewModel
class SoundHapticSettingsViewModel @Inject constructor(
    private val preferencesManager: SharedPreferencesManager,
    private val soundHapticService: SoundHapticService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SoundHapticUiState())
    val uiState: StateFlow<SoundHapticUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = SoundHapticUiState(
                soundEnabled = preferencesManager.getSoundEnabled(),
                soundVolume = preferencesManager.getSoundVolume(),
                hapticEnabled = preferencesManager.getHapticEnabled(),
                hapticIntensity = preferencesManager.getHapticIntensity(),
                milestoneNotifications = preferencesManager.getMilestoneNotifications(),
                completionCelebration = preferencesManager.getCompletionCelebration(),
                buttonClickFeedback = preferencesManager.getButtonClickFeedback()
            )
        }
    }
    
    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setSoundEnabled(enabled)
            soundHapticService.setSoundEnabled(enabled)
            _uiState.value = _uiState.value.copy(soundEnabled = enabled)
        }
    }
    
    fun setSoundVolume(volume: Float) {
        viewModelScope.launch {
            preferencesManager.setSoundVolume(volume)
            _uiState.value = _uiState.value.copy(soundVolume = volume)
        }
    }
    
    fun setHapticEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setHapticEnabled(enabled)
            soundHapticService.setHapticEnabled(enabled)
            _uiState.value = _uiState.value.copy(hapticEnabled = enabled)
        }
    }
    
    fun setHapticIntensity(intensity: Int) {
        viewModelScope.launch {
            preferencesManager.setHapticIntensity(intensity)
            _uiState.value = _uiState.value.copy(hapticIntensity = intensity)
        }
    }
    
    fun setMilestoneNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setMilestoneNotifications(enabled)
            _uiState.value = _uiState.value.copy(milestoneNotifications = enabled)
        }
    }
    
    fun setCompletionCelebration(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setCompletionCelebration(enabled)
            _uiState.value = _uiState.value.copy(completionCelebration = enabled)
        }
    }
    
    fun setButtonClickFeedback(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setButtonClickFeedback(enabled)
            _uiState.value = _uiState.value.copy(buttonClickFeedback = enabled)
        }
    }
    
    fun testSound() {
        soundHapticService.playSound(
            SoundHapticService.SoundType.NOTIFICATION,
            volume = _uiState.value.soundVolume
        )
    }
    
    fun testHaptic(pattern: SoundHapticService.HapticPattern) {
        soundHapticService.triggerHaptic(
            pattern,
            intensity = _uiState.value.hapticIntensity
        )
    }
}