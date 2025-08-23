# GitHub Issue #6: Comprehensive Accessibility Improvements

## Summary
Implement comprehensive accessibility features including screen reader support, high contrast modes, voice control, and motor accessibility options to ensure CountJoy is usable by everyone.

## Description
Make CountJoy fully accessible by implementing WCAG 2.1 AA compliance, adding screen reader support, voice control, adjustable touch targets, and various visual accessibility options.

## Acceptance Criteria
- [ ] Full TalkBack/screen reader support
- [ ] Voice control for core functions
- [ ] Adjustable font sizes (5 levels)
- [ ] High contrast mode
- [ ] Color blind friendly modes
- [ ] Reduced motion options
- [ ] Keyboard navigation support
- [ ] WCAG 2.1 AA compliance

## Technical Implementation

### 1. Accessibility Manager
```kotlin
@Singleton
class AccessibilityManager @Inject constructor(
    private val context: Context,
    private val preferencesManager: SharedPreferencesManager
) {
    fun applyAccessibilitySettings() {
        when (getAccessibilityMode()) {
            AccessibilityMode.HIGH_CONTRAST -> applyHighContrast()
            AccessibilityMode.LARGE_TEXT -> applyLargeText()
            AccessibilityMode.REDUCED_MOTION -> disableAnimations()
            AccessibilityMode.SCREEN_READER -> optimizeForScreenReader()
        }
    }
    
    fun announceForAccessibility(message: String) {
        if (isScreenReaderEnabled()) {
            view.announceForAccessibility(message)
        }
    }
}
```

### 2. Compose Accessibility Semantics
```kotlin
@Composable
fun AccessibleCountdownCard(
    event: Event,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .semantics {
                contentDescription = buildString {
                    append("${event.name} countdown. ")
                    append("${event.daysRemaining} days remaining. ")
                    append("Expires on ${event.formattedDate}.")
                }
                stateDescription = if (event.isExpired) "Expired" else "Active"
                
                customActions = listOf(
                    CustomAccessibilityAction("Edit") { 
                        onEdit()
                        true 
                    },
                    CustomAccessibilityAction("Delete") { 
                        onDelete()
                        true 
                    }
                )
            }
            .focusable()
    ) {
        // Card content
    }
}
```

### 3. Voice Control Integration
```kotlin
class VoiceCommandHandler @Inject constructor(
    private val speechRecognizer: SpeechRecognizer,
    private val eventRepository: EventRepository
) {
    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, 
                    "Say a command: Create event, Show settings, etc.")
        }
        
        speechRecognizer.startListening(intent)
    }
    
    fun processCommand(command: String) {
        when {
            command.contains("create", ignoreCase = true) -> createEvent()
            command.contains("show", ignoreCase = true) -> navigateToScreen()
            command.contains("delete", ignoreCase = true) -> deleteEvent()
            command.contains("edit", ignoreCase = true) -> editEvent()
        }
    }
}
```

### 4. Color Blind Modes
```kotlin
enum class ColorBlindMode {
    NONE,
    PROTANOPIA,    // Red-blind
    DEUTERANOPIA,  // Green-blind
    TRITANOPIA,    // Blue-blind
    ACHROMATOPSIA  // Complete color blindness
}

@Composable
fun ColorBlindFilter(
    mode: ColorBlindMode,
    content: @Composable () -> Unit
) {
    val colorMatrix = when (mode) {
        ColorBlindMode.PROTANOPIA -> protanopiaMatrix()
        ColorBlindMode.DEUTERANOPIA -> deuteranopiaMatrix()
        ColorBlindMode.TRITANOPIA -> tritanopiaMatrix()
        ColorBlindMode.ACHROMATOPSIA -> grayscaleMatrix()
        else -> null
    }
    
    if (colorMatrix != null) {
        Box(modifier = Modifier.colorFilter(ColorFilter.colorMatrix(colorMatrix))) {
            content()
        }
    } else {
        content()
    }
}
```

### 5. Touch Target Adjustments
```kotlin
@Composable
fun AccessibleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minTouchTarget = if (isLargeTouchTargetsEnabled()) 48.dp else 36.dp
    
    Button(
        onClick = onClick,
        modifier = modifier
            .sizeIn(minHeight = minTouchTarget, minWidth = minTouchTarget)
            .semantics {
                role = Role.Button
                onClick(label = text) { 
                    onClick()
                    true 
                }
            }
    ) {
        Text(
            text = text,
            fontSize = getAccessibleFontSize()
        )
    }
}
```

## UI Components

### Accessibility Settings Screen
```
Accessibility
├── Visual
│   ├── Font Size [XS|S|M|L|XL]
│   ├── High Contrast [Toggle]
│   ├── Color Blind Mode [None|Protanopia|Deuteranopia|Tritanopia]
│   ├── Reduce Transparency [Toggle]
│   └── Bold Text [Toggle]
├── Motor
│   ├── Touch Target Size [Small|Medium|Large]
│   ├── Touch & Hold Delay [Short|Medium|Long]
│   ├── Ignore Repeated Taps [Toggle]
│   └── Tap Assistance [Toggle]
├── Audio
│   ├── Screen Reader [System Setting]
│   ├── Voice Control [Toggle]
│   ├── Sound Feedback [Toggle]
│   └── Spoken Confirmations [Toggle]
├── Interaction
│   ├── Reduce Motion [Toggle]
│   ├── Auto-play Videos [Toggle]
│   ├── Keyboard Navigation [Toggle]
│   └── Switch Control [System Setting]
└── Cognitive
    ├── Simple Mode [Toggle]
    ├── Reading Assistance [Toggle]
    ├── Time Limits [Extended|Disabled]
    └── Clear Labels [Toggle]
```

### Screen Reader Optimizations
```kotlin
// Proper content descriptions
contentDescription = "Birthday countdown: 45 days remaining"

// Grouping related elements
modifier = Modifier.semantics(mergeDescendants = true) {}

// Live regions for dynamic content
modifier = Modifier.semantics {
    liveRegion = LiveRegionMode.Polite
}

// Custom actions for complex interactions
customActions = listOf(
    CustomAccessibilityAction("Edit countdown") { /* ... */ },
    CustomAccessibilityAction("Share countdown") { /* ... */ }
)
```

## Implementation Steps

1. **Phase 1: Screen Reader Support (2 days)**
   - Add content descriptions to all UI elements
   - Implement proper focus management
   - Create custom actions for complex components

2. **Phase 2: Visual Accessibility (2 days)**
   - Implement high contrast mode
   - Add color blind filters
   - Create font size adjustment system

3. **Phase 3: Motor Accessibility (1 day)**
   - Adjust touch targets
   - Implement tap assistance
   - Add keyboard navigation

4. **Phase 4: Voice Control (2 days)**
   - Integrate speech recognition
   - Create command processor
   - Add voice feedback

5. **Phase 5: Testing & Compliance (2 days)**
   - WCAG 2.1 AA audit
   - Screen reader testing
   - User testing with accessibility tools

## Testing Checklist
- [ ] All interactive elements are accessible via screen reader
- [ ] Color contrast ratios meet WCAG standards (4.5:1 for normal text)
- [ ] All functionality available via keyboard
- [ ] Voice commands work reliably
- [ ] Touch targets meet minimum size (48x48dp)
- [ ] Animations can be disabled
- [ ] Text remains readable at 200% zoom
- [ ] Focus indicators are clearly visible

## WCAG 2.1 AA Compliance Checklist
- [ ] **Perceivable**: Alt text, captions, contrast ratios
- [ ] **Operable**: Keyboard access, timing adjustable, seizure safe
- [ ] **Understandable**: Readable, predictable, input assistance
- [ ] **Robust**: Valid code, compatible with assistive tech

## Performance Considerations
- Screen reader announcements: < 100ms delay
- Voice recognition: < 2s response time
- High contrast mode: No performance impact
- Large text rendering: Maintain 60fps

## Dependencies
- Android Accessibility Services
- SpeechRecognizer API
- AccessibilityManager
- TalkBack testing tools

## Priority
🔴 **HIGH** - Legal compliance and inclusivity

## Labels
`enhancement`, `accessibility`, `wcag`, `high-priority`, `compliance`

## Milestone
v2.4.0 - Accessibility Update

## Estimated Time
9 days

## Related PRDs
- [Accessibility PRD](../PRDs/06-accessibility-prd.md)

## Notes
- Consider partnering with accessibility organizations for testing
- Plan for regular accessibility audits
- Document accessibility features for users
- Consider adding accessibility onboarding flow