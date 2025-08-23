# GitHub Issue #4: Notification System Overhaul

## Summary
Implement a sophisticated notification system with smart timing, customizable alerts, quiet hours, and rich notification features for Android.

## Description
Replace the basic notification system with an advanced one featuring multiple reminder intervals, smart timing suggestions, custom sounds, vibration patterns, and respect for Do Not Disturb settings.

## Acceptance Criteria
- [ ] Multiple reminder intervals per event
- [ ] Custom notification sounds (30+ options)
- [ ] Vibration pattern customization
- [ ] Quiet hours configuration
- [ ] Rich notifications with actions
- [ ] Smart timing AI suggestions
- [ ] Notification history log
- [ ] Per-category notification settings

## Technical Implementation

### 1. Enhanced Notification Manager
```kotlin
@Singleton
class CountJoyNotificationManager @Inject constructor(
    private val context: Context,
    private val workManager: WorkManager,
    private val notificationRepository: NotificationRepository
) {
    fun scheduleNotification(
        event: Event,
        config: NotificationConfig,
        smartTiming: Boolean = false
    )
    
    fun createRichNotification(
        event: Event,
        actions: List<NotificationAction>
    ): Notification
    
    fun respectQuietHours(): Boolean
    fun suggestOptimalTiming(event: Event): List<Instant>
}
```

### 2. Notification Configuration
```kotlin
data class NotificationConfig(
    val id: Long,
    val eventId: Long,
    val intervals: List<ReminderInterval>,
    val sound: NotificationSound?,
    val vibrationPattern: VibrationPattern,
    val ledColor: Int?,
    val priority: NotificationPriority,
    val style: NotificationStyle,
    val actions: List<NotificationAction>
)

data class ReminderInterval(
    val value: Int,
    val unit: TimeUnit,
    val type: ReminderType // BEFORE, AFTER, EXACT
)

enum class NotificationStyle {
    STANDARD, BIG_TEXT, INBOX, MESSAGING, MEDIA, BIG_PICTURE
}
```

### 3. Notification Channels Setup
```kotlin
private fun createNotificationChannels() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channels = listOf(
            NotificationChannel(
                CHANNEL_URGENT,
                "Urgent Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            },
            NotificationChannel(
                CHANNEL_DEFAULT,
                "Regular Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            NotificationChannel(
                CHANNEL_SILENT,
                "Silent Updates",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setShowBadge(false)
            }
        )
        
        notificationManager.createNotificationChannels(channels)
    }
}
```

### 4. Smart Timing Algorithm
```kotlin
class SmartTimingEngine @Inject constructor(
    private val userBehaviorAnalyzer: UserBehaviorAnalyzer,
    private val calendarIntegration: CalendarIntegration
) {
    fun suggestReminderTimes(event: Event): List<SuggestedTime> {
        val userPatterns = analyzeUserPatterns()
        val calendarEvents = getCalendarEvents()
        val eventImportance = calculateImportance(event)
        
        return generateOptimalTimes(
            userPatterns,
            calendarEvents,
            eventImportance
        )
    }
}
```

## UI Components

### Notification Settings Screen
```
Notifications & Alerts
├── Master Switch [On/Off]
├── Default Settings
│   ├── Sound [System/Custom/Silent]
│   ├── Vibration [Pattern selector]
│   └── LED Color [Color picker]
├── Smart Features
│   ├── AI Timing [Toggle]
│   ├── Bundle Similar [Toggle]
│   └── Auto-snooze [Toggle]
├── Quiet Hours
│   ├── Enabled [Toggle]
│   ├── Start Time [Time picker]
│   ├── End Time [Time picker]
│   └── Days [Day selector]
├── Categories
│   └── [Per-category overrides]
└── Notification History
    └── [List of past notifications]
```

### Rich Notification Actions
- **View Details**: Open event in app
- **Snooze**: Quick snooze options (5m, 15m, 1h, 1d)
- **Mark Complete**: Complete the countdown
- **Share**: Share event with others
- **Edit**: Quick edit reminder time

## Implementation Steps

1. **Phase 1: Core Infrastructure (2 days)**
   - Implement WorkManager scheduling
   - Create notification channels
   - Build NotificationConfig system

2. **Phase 2: Customization (2 days)**
   - Sound selection UI
   - Vibration pattern creator
   - LED color picker
   - Priority settings

3. **Phase 3: Smart Features (2 days)**
   - Quiet hours implementation
   - Smart timing algorithm
   - Notification bundling
   - Auto-snooze logic

4. **Phase 4: Rich Notifications (1 day)**
   - Action buttons
   - Expanded layouts
   - Quick reply support
   - Media style notifications

5. **Phase 5: Testing & Polish (1 day)**
   - Battery impact testing
   - Delivery reliability testing
   - UI/UX refinement

## Database Schema
```sql
CREATE TABLE notification_configs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id INTEGER NOT NULL REFERENCES events(id),
    is_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE reminder_intervals (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    config_id INTEGER NOT NULL REFERENCES notification_configs(id),
    interval_value INTEGER NOT NULL,
    interval_unit TEXT NOT NULL,
    reminder_type TEXT NOT NULL,
    FOREIGN KEY (config_id) REFERENCES notification_configs(id) ON DELETE CASCADE
);

CREATE TABLE quiet_hours (
    id INTEGER PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    days_of_week TEXT NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE notification_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id INTEGER,
    sent_at TIMESTAMP NOT NULL,
    was_interacted BOOLEAN DEFAULT FALSE,
    action_taken TEXT,
    delivery_status TEXT NOT NULL
);
```

## Testing Checklist
- [ ] Notifications fire at correct times
- [ ] Quiet hours are respected
- [ ] Custom sounds play correctly
- [ ] Vibration patterns work
- [ ] Rich notifications display properly
- [ ] Battery impact < 1%
- [ ] 99.9% delivery success rate
- [ ] Smart timing provides relevant suggestions

## Performance Requirements
- Notification scheduling: < 100ms
- Battery impact: < 1% daily
- Memory usage: < 10MB for notification service
- Delivery success rate: > 99.9%

## Dependencies
- WorkManager for reliable scheduling
- NotificationCompat for backwards compatibility
- MediaPlayer for custom sounds
- Vibrator service for patterns

## Priority
🔴 **HIGH** - Core feature enhancement

## Labels
`enhancement`, `notifications`, `high-priority`, `user-engagement`

## Milestone
v2.2.0 - Smart Notifications

## Estimated Time
8 days

## Related PRDs
- [Notifications & Alerts PRD](../PRDs/04-notifications-alerts-prd.md)

## Notes
- Consider Firebase Cloud Messaging for cross-device sync
- Plan A/B testing for smart timing effectiveness
- Monitor notification interaction rates