package com.countjoy.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.core.accessibility.AccessibilityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessibilitySettingsViewModel @Inject constructor(
    private val accessibilityManager: AccessibilityManager
) : ViewModel() {
    
    private val _settings = MutableStateFlow(loadCurrentSettings())
    val settings: StateFlow<AccessibilitySettings> = _settings.asStateFlow()
    
    private fun loadCurrentSettings(): AccessibilitySettings {
        return AccessibilitySettings(
            fontSize = accessibilityManager.getFontSize(),
            isHighContrastEnabled = accessibilityManager.isHighContrastEnabled(),
            colorBlindMode = accessibilityManager.getColorBlindMode(),
            isReduceMotionEnabled = accessibilityManager.isReduceMotionEnabled(),
            isVoiceControlEnabled = accessibilityManager.isVoiceControlEnabled(),
            isBoldTextEnabled = accessibilityManager.isBoldTextEnabled(),
            touchTargetSize = accessibilityManager.getTouchTargetSize(),
            touchHoldDelay = accessibilityManager.getTouchHoldDelay(),
            isScreenReaderEnabled = accessibilityManager.isScreenReaderEnabled(),
            isSimpleModeEnabled = accessibilityManager.isSimpleModeEnabled()
        )
    }
    
    fun setFontSize(size: AccessibilityManager.Companion.FontSize) {
        viewModelScope.launch {
            accessibilityManager.setFontSize(size)
            _settings.value = _settings.value.copy(fontSize = size)
        }
    }
    
    fun setHighContrastEnabled(enabled: Boolean) {
        viewModelScope.launch {
            accessibilityManager.setHighContrastEnabled(enabled)
            _settings.value = _settings.value.copy(isHighContrastEnabled = enabled)
        }
    }
    
    fun setColorBlindMode(mode: AccessibilityManager.Companion.ColorBlindMode) {
        viewModelScope.launch {
            accessibilityManager.setColorBlindMode(mode)
            _settings.value = _settings.value.copy(colorBlindMode = mode)
        }
    }
    
    fun setReduceMotionEnabled(enabled: Boolean) {
        viewModelScope.launch {
            accessibilityManager.setReduceMotionEnabled(enabled)
            _settings.value = _settings.value.copy(isReduceMotionEnabled = enabled)
        }
    }
    
    fun setVoiceControlEnabled(enabled: Boolean) {
        viewModelScope.launch {
            accessibilityManager.setVoiceControlEnabled(enabled)
            _settings.value = _settings.value.copy(isVoiceControlEnabled = enabled)
        }
    }
    
    fun setBoldTextEnabled(enabled: Boolean) {
        viewModelScope.launch {
            accessibilityManager.setBoldTextEnabled(enabled)
            _settings.value = _settings.value.copy(isBoldTextEnabled = enabled)
        }
    }
    
    fun setTouchTargetSize(size: AccessibilityManager.Companion.TouchTargetSize) {
        viewModelScope.launch {
            accessibilityManager.setTouchTargetSize(size)
            _settings.value = _settings.value.copy(touchTargetSize = size)
        }
    }
    
    fun setTouchHoldDelay(delay: AccessibilityManager.Companion.TouchHoldDelay) {
        viewModelScope.launch {
            accessibilityManager.setTouchHoldDelay(delay)
            _settings.value = _settings.value.copy(touchHoldDelay = delay)
        }
    }
    
    fun setSimpleModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            accessibilityManager.setSimpleModeEnabled(enabled)
            _settings.value = _settings.value.copy(isSimpleModeEnabled = enabled)
        }
    }
    
    data class AccessibilitySettings(
        val fontSize: AccessibilityManager.Companion.FontSize,
        val isHighContrastEnabled: Boolean,
        val colorBlindMode: AccessibilityManager.Companion.ColorBlindMode,
        val isReduceMotionEnabled: Boolean,
        val isVoiceControlEnabled: Boolean,
        val isBoldTextEnabled: Boolean,
        val touchTargetSize: AccessibilityManager.Companion.TouchTargetSize,
        val touchHoldDelay: AccessibilityManager.Companion.TouchHoldDelay,
        val isScreenReaderEnabled: Boolean,
        val isSimpleModeEnabled: Boolean
    )
}