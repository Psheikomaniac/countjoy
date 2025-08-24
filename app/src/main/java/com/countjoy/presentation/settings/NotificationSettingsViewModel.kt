package com.countjoy.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.countjoy.data.local.preferences.SharedPreferencesManager
import com.countjoy.domain.model.*
import com.countjoy.service.SmartNotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

/**
 * ViewModel for notification settings screen
 */
@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val preferencesManager: SharedPreferencesManager,
    private val smartNotificationService: SmartNotificationService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotificationSettingsUiState())
    val uiState: StateFlow<NotificationSettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    urgentChannelEnabled = preferencesManager.getBoolean("channel_urgent", true),
                    milestonesChannelEnabled = preferencesManager.getBoolean("channel_milestones", true),
                    remindersChannelEnabled = preferencesManager.getBoolean("channel_reminders", true),
                    summaryChannelEnabled = preferencesManager.getBoolean("channel_summary", false),
                    smartRemindersEnabled = preferencesManager.getBoolean("smart_reminders", true),
                    adaptiveSchedulingEnabled = preferencesManager.getBoolean("adaptive_scheduling", true),
                    bundleNotificationsEnabled = preferencesManager.getBoolean("bundle_notifications", true),
                    quietHoursEnabled = preferencesManager.getBoolean("quiet_hours_enabled", false),
                    quietHoursStart = preferencesManager.getString("quiet_hours_start")?.let {
                        LocalTime.parse(it)
                    } ?: LocalTime.of(22, 0),
                    quietHoursEnd = preferencesManager.getString("quiet_hours_end")?.let {
                        LocalTime.parse(it)
                    } ?: LocalTime.of(7, 0),
                    overrideQuietHoursForUrgent = preferencesManager.getBoolean("override_quiet_urgent", true),
                    quickActionsEnabled = preferencesManager.getBoolean("quick_actions", true),
                    snoozeEnabled = preferencesManager.getBoolean("snooze_enabled", true),
                    shareEnabled = preferencesManager.getBoolean("share_enabled", true),
                    quickViewEnabled = preferencesManager.getBoolean("quick_view_enabled", true),
                    selectedSnoozeOptions = preferencesManager.getStringSet("snooze_options") 
                        ?: setOf("1h", "3h", "1d")
                )
            }
        }
    }
    
    fun toggleChannel(channelType: NotificationChannelType, enabled: Boolean) {
        viewModelScope.launch {
            val key = when (channelType) {
                NotificationChannelType.URGENT -> "channel_urgent"
                NotificationChannelType.MILESTONES -> "channel_milestones"
                NotificationChannelType.REMINDERS -> "channel_reminders"
                NotificationChannelType.SUMMARY -> "channel_summary"
                NotificationChannelType.SILENT -> "channel_silent"
            }
            preferencesManager.putBoolean(key, enabled)
            
            _uiState.update { state ->
                when (channelType) {
                    NotificationChannelType.URGENT -> state.copy(urgentChannelEnabled = enabled)
                    NotificationChannelType.MILESTONES -> state.copy(milestonesChannelEnabled = enabled)
                    NotificationChannelType.REMINDERS -> state.copy(remindersChannelEnabled = enabled)
                    NotificationChannelType.SUMMARY -> state.copy(summaryChannelEnabled = enabled)
                    else -> state
                }
            }
        }
    }
    
    fun toggleSmartReminders(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("smart_reminders", enabled)
            _uiState.update { it.copy(smartRemindersEnabled = enabled) }
        }
    }
    
    fun toggleAdaptiveScheduling(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("adaptive_scheduling", enabled)
            _uiState.update { it.copy(adaptiveSchedulingEnabled = enabled) }
        }
    }
    
    fun toggleBundleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("bundle_notifications", enabled)
            _uiState.update { it.copy(bundleNotificationsEnabled = enabled) }
        }
    }
    
    fun toggleQuietHours(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("quiet_hours_enabled", enabled)
            _uiState.update { it.copy(quietHoursEnabled = enabled) }
        }
    }
    
    fun toggleOverrideQuietHours(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("override_quiet_urgent", enabled)
            _uiState.update { it.copy(overrideQuietHoursForUrgent = enabled) }
        }
    }
    
    fun toggleQuickActions(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("quick_actions", enabled)
            _uiState.update { it.copy(quickActionsEnabled = enabled) }
        }
    }
    
    fun toggleSnooze(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("snooze_enabled", enabled)
            _uiState.update { it.copy(snoozeEnabled = enabled) }
        }
    }
    
    fun toggleShare(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("share_enabled", enabled)
            _uiState.update { it.copy(shareEnabled = enabled) }
        }
    }
    
    fun toggleQuickView(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.putBoolean("quick_view_enabled", enabled)
            _uiState.update { it.copy(quickViewEnabled = enabled) }
        }
    }
    
    fun toggleSnoozeOption(option: String) {
        viewModelScope.launch {
            val currentOptions = _uiState.value.selectedSnoozeOptions.toMutableSet()
            if (option in currentOptions) {
                currentOptions.remove(option)
            } else {
                currentOptions.add(option)
            }
            
            preferencesManager.putStringSet("snooze_options", currentOptions)
            _uiState.update { it.copy(selectedSnoozeOptions = currentOptions) }
        }
    }
    
    fun showStartTimePicker() {
        // This would typically show a time picker dialog
        // For now, we'll just cycle through some preset times
        viewModelScope.launch {
            val currentStart = _uiState.value.quietHoursStart ?: LocalTime.of(22, 0)
            val newStart = currentStart.plusHours(1)
            
            preferencesManager.putString("quiet_hours_start", newStart.toString())
            _uiState.update { it.copy(quietHoursStart = newStart) }
        }
    }
    
    fun showEndTimePicker() {
        // This would typically show a time picker dialog
        // For now, we'll just cycle through some preset times
        viewModelScope.launch {
            val currentEnd = _uiState.value.quietHoursEnd ?: LocalTime.of(7, 0)
            val newEnd = currentEnd.plusHours(1)
            
            preferencesManager.putString("quiet_hours_end", newEnd.toString())
            _uiState.update { it.copy(quietHoursEnd = newEnd) }
        }
    }
    
    fun sendTestNotification() {
        viewModelScope.launch {
            // Create a test event
            val testEvent = CountdownEvent(
                id = 999,
                title = "Test Event",
                description = "This is a test notification",
                category = "Test",
                targetDateTime = LocalDateTime.now().plusDays(7),
                priority = 1
            )
            
            // Create test config with current settings
            val config = NotificationConfig(
                eventId = testEvent.id,
                channelType = NotificationChannelType.MILESTONES,
                enableSound = true,
                enableVibration = true,
                enableLed = true,
                quietHoursEnabled = _uiState.value.quietHoursEnabled,
                quietHoursStart = _uiState.value.quietHoursStart,
                quietHoursEnd = _uiState.value.quietHoursEnd,
                overrideQuietHoursForUrgent = _uiState.value.overrideQuietHoursForUrgent,
                smartRemindersEnabled = _uiState.value.smartRemindersEnabled,
                adaptiveScheduling = _uiState.value.adaptiveSchedulingEnabled,
                bundleNotifications = _uiState.value.bundleNotificationsEnabled,
                enableQuickActions = _uiState.value.quickActionsEnabled,
                enableShare = _uiState.value.shareEnabled,
                enableQuickView = _uiState.value.quickViewEnabled,
                snoozeOptions = listOf(60, 180, 1440) // 1h, 3h, 1 day in minutes
            )
            
            // Create test milestone
            val testMilestone = Milestone(
                id = "test_milestone",
                eventId = testEvent.id.toString(),
                title = "Test Milestone",
                message = "This is a test of the notification system",
                triggerDays = 7,
                isAchieved = true,
                achievedAt = LocalDateTime.now(),
                isNotificationEnabled = true
            )
            
            // Send test notification
            smartNotificationService.showSmartNotification(
                event = testEvent,
                config = config,
                notificationType = NotificationType.MILESTONE,
                milestone = testMilestone
            )
        }
    }
}

data class NotificationSettingsUiState(
    val urgentChannelEnabled: Boolean = true,
    val milestonesChannelEnabled: Boolean = true,
    val remindersChannelEnabled: Boolean = true,
    val summaryChannelEnabled: Boolean = false,
    val smartRemindersEnabled: Boolean = true,
    val adaptiveSchedulingEnabled: Boolean = true,
    val bundleNotificationsEnabled: Boolean = true,
    val quietHoursEnabled: Boolean = false,
    val quietHoursStart: LocalTime? = null,
    val quietHoursEnd: LocalTime? = null,
    val overrideQuietHoursForUrgent: Boolean = true,
    val quickActionsEnabled: Boolean = true,
    val snoozeEnabled: Boolean = true,
    val shareEnabled: Boolean = true,
    val quickViewEnabled: Boolean = true,
    val selectedSnoozeOptions: Set<String> = setOf("1h", "3h", "1d")
)