package com.countjoy.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.data.local.preferences.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: Int = SharedPreferencesManager.THEME_MODE_SYSTEM,
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val use24HourFormat: Boolean = false,
    val dateFormat: String = SharedPreferencesManager.DATE_FORMAT_DEFAULT,
    val autoDeleteExpired: Boolean = false,
    val countdownUpdateInterval: Long = 1000L
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _uiState.update { currentState ->
            currentState.copy(
                themeMode = preferencesManager.getThemeMode(),
                notificationsEnabled = preferencesManager.isNotificationEnabled(),
                soundEnabled = preferencesManager.isSoundEnabled(),
                vibrationEnabled = preferencesManager.isVibrationEnabled(),
                use24HourFormat = preferencesManager.is24HourFormat(),
                dateFormat = preferencesManager.getDateFormat(),
                autoDeleteExpired = preferencesManager.isAutoDeleteExpired(),
                countdownUpdateInterval = preferencesManager.getCountdownUpdateInterval()
            )
        }
    }

    fun onThemeModeChanged(mode: Int) {
        viewModelScope.launch {
            preferencesManager.setThemeMode(mode)
            _uiState.update { it.copy(themeMode = mode) }
        }
    }

    fun onNotificationsChanged(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setNotificationEnabled(enabled)
            _uiState.update { it.copy(notificationsEnabled = enabled) }
            
            // If notifications are disabled, also disable sound and vibration
            if (!enabled) {
                preferencesManager.setSoundEnabled(false)
                preferencesManager.setVibrationEnabled(false)
                _uiState.update { 
                    it.copy(
                        soundEnabled = false,
                        vibrationEnabled = false
                    )
                }
            }
        }
    }

    fun onSoundChanged(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setSoundEnabled(enabled)
            _uiState.update { it.copy(soundEnabled = enabled) }
        }
    }

    fun onVibrationChanged(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setVibrationEnabled(enabled)
            _uiState.update { it.copy(vibrationEnabled = enabled) }
        }
    }

    fun on24HourFormatChanged(use24Hour: Boolean) {
        viewModelScope.launch {
            preferencesManager.set24HourFormat(use24Hour)
            _uiState.update { it.copy(use24HourFormat = use24Hour) }
        }
    }

    fun onDateFormatChanged(format: String) {
        viewModelScope.launch {
            preferencesManager.setDateFormat(format)
            _uiState.update { it.copy(dateFormat = format) }
        }
    }

    fun onAutoDeleteChanged(autoDelete: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAutoDeleteExpired(autoDelete)
            _uiState.update { it.copy(autoDeleteExpired = autoDelete) }
        }
    }

    fun onCountdownIntervalChanged(interval: Long) {
        viewModelScope.launch {
            preferencesManager.setCountdownUpdateInterval(interval)
            _uiState.update { it.copy(countdownUpdateInterval = interval) }
        }
    }
}