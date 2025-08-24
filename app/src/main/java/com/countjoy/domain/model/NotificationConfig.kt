package com.countjoy.domain.model

import java.time.LocalTime

/**
 * Configuration for smart notifications system
 */
data class NotificationConfig(
    val id: Long = 0,
    val eventId: Long,
    
    // Notification channels
    val channelType: NotificationChannelType = NotificationChannelType.MILESTONES,
    
    // Custom sound and vibration
    val customSoundUri: String? = null,
    val vibrationPattern: LongArray = longArrayOf(0, 500, 100, 500),
    val ledColor: Int? = null,
    val enableSound: Boolean = true,
    val enableVibration: Boolean = true,
    val enableLed: Boolean = true,
    
    // Quiet hours
    val quietHoursEnabled: Boolean = false,
    val quietHoursStart: LocalTime? = null,
    val quietHoursEnd: LocalTime? = null,
    val overrideQuietHoursForUrgent: Boolean = true,
    
    // Smart reminders
    val smartRemindersEnabled: Boolean = true,
    val adaptiveScheduling: Boolean = true,
    val bundleNotifications: Boolean = true,
    val maxBundleSize: Int = 5,
    
    // Milestone configuration
    val milestoneIntervals: List<Int> = listOf(365, 180, 90, 60, 30, 14, 7, 3, 1),
    val percentageBasedMilestones: List<Int> = listOf(50, 25, 10, 5),
    val hourBasedMilestones: List<Int> = listOf(24, 12, 6, 3, 1),
    
    // Repeating reminders
    val repeatingReminders: List<RepeatingReminder> = emptyList(),
    
    // Actions
    val enableQuickActions: Boolean = true,
    val snoozeOptions: List<Int> = listOf(60, 180, 1440), // minutes: 1h, 3h, 1 day
    val enableShare: Boolean = true,
    val enableQuickView: Boolean = true
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificationConfig

        if (id != other.id) return false
        if (eventId != other.eventId) return false
        if (channelType != other.channelType) return false
        if (customSoundUri != other.customSoundUri) return false
        if (!vibrationPattern.contentEquals(other.vibrationPattern)) return false
        if (ledColor != other.ledColor) return false
        if (enableSound != other.enableSound) return false
        if (enableVibration != other.enableVibration) return false
        if (enableLed != other.enableLed) return false
        if (quietHoursEnabled != other.quietHoursEnabled) return false
        if (quietHoursStart != other.quietHoursStart) return false
        if (quietHoursEnd != other.quietHoursEnd) return false
        if (overrideQuietHoursForUrgent != other.overrideQuietHoursForUrgent) return false
        if (smartRemindersEnabled != other.smartRemindersEnabled) return false
        if (adaptiveScheduling != other.adaptiveScheduling) return false
        if (bundleNotifications != other.bundleNotifications) return false
        if (maxBundleSize != other.maxBundleSize) return false
        if (milestoneIntervals != other.milestoneIntervals) return false
        if (percentageBasedMilestones != other.percentageBasedMilestones) return false
        if (hourBasedMilestones != other.hourBasedMilestones) return false
        if (repeatingReminders != other.repeatingReminders) return false
        if (enableQuickActions != other.enableQuickActions) return false
        if (snoozeOptions != other.snoozeOptions) return false
        if (enableShare != other.enableShare) return false
        if (enableQuickView != other.enableQuickView) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + eventId.hashCode()
        result = 31 * result + channelType.hashCode()
        result = 31 * result + (customSoundUri?.hashCode() ?: 0)
        result = 31 * result + vibrationPattern.contentHashCode()
        result = 31 * result + (ledColor ?: 0)
        result = 31 * result + enableSound.hashCode()
        result = 31 * result + enableVibration.hashCode()
        result = 31 * result + enableLed.hashCode()
        result = 31 * result + quietHoursEnabled.hashCode()
        result = 31 * result + (quietHoursStart?.hashCode() ?: 0)
        result = 31 * result + (quietHoursEnd?.hashCode() ?: 0)
        result = 31 * result + overrideQuietHoursForUrgent.hashCode()
        result = 31 * result + smartRemindersEnabled.hashCode()
        result = 31 * result + adaptiveScheduling.hashCode()
        result = 31 * result + bundleNotifications.hashCode()
        result = 31 * result + maxBundleSize
        result = 31 * result + milestoneIntervals.hashCode()
        result = 31 * result + percentageBasedMilestones.hashCode()
        result = 31 * result + hourBasedMilestones.hashCode()
        result = 31 * result + repeatingReminders.hashCode()
        result = 31 * result + enableQuickActions.hashCode()
        result = 31 * result + snoozeOptions.hashCode()
        result = 31 * result + enableShare.hashCode()
        result = 31 * result + enableQuickView.hashCode()
        return result
    }
}

enum class NotificationChannelType {
    URGENT,      // High priority events, always notify
    MILESTONES,  // Standard milestone alerts
    REMINDERS,   // Custom user reminders
    SUMMARY,     // Daily/weekly summaries
    SILENT       // Visual only, no sound/vibration
}

data class RepeatingReminder(
    val id: Long = 0,
    val interval: ReminderInterval,
    val customIntervalMinutes: Int? = null,
    val enabled: Boolean = true
)

enum class ReminderInterval {
    HOURLY,
    DAILY,
    WEEKLY,
    CUSTOM
}