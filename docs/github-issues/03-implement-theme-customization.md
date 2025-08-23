# GitHub Issue #3: Implement Theme Customization System

## Summary
Add comprehensive theme customization allowing users to personalize CountJoy's appearance with Material You support, custom themes, and various display options.

## Description
Enhance the existing basic theme switching (Light/Dark/System) with advanced customization options including Material You dynamic colors, custom color schemes, font adjustments, and display density settings.

## Acceptance Criteria
- [ ] Material You dynamic color extraction (Android 12+)
- [ ] Custom theme creator with color picker
- [ ] Font size adjustment (5 levels)
- [ ] Display density modes (Compact/Comfortable/Spacious)
- [ ] High contrast mode for accessibility
- [ ] Theme preview before applying
- [ ] Per-event theme override option
- [ ] Theme persistence across sessions

## Technical Requirements

### 1. Enhanced Theme Manager
```kotlin
@Singleton
class ThemeManager @Inject constructor(
    private val preferencesManager: SharedPreferencesManager,
    private val context: Context
) {
    fun applyTheme(theme: AppTheme)
    fun createCustomTheme(colors: ColorPalette): AppTheme
    fun extractWallpaperColors(): ColorPalette // Android 12+
    fun saveCustomTheme(theme: AppTheme)
    fun getPresetThemes(): List<AppTheme>
}
```

### 2. Material You Integration
```kotlin
@RequiresApi(Build.VERSION_CODES.S)
fun extractDynamicColors(): ColorScheme {
    val dynamicColor = DynamicColors.Builder()
        .setContentBasedSource(wallpaperManager.drawable)
        .build()
    
    return dynamicColorScheme(dynamicColor)
}
```

### 3. Custom Theme Data Model
```kotlin
data class AppTheme(
    val id: String,
    val name: String,
    val isDynamic: Boolean,
    val colorPalette: ColorPalette,
    val typography: TypographySettings,
    val shapes: ShapeSettings,
    val elevation: ElevationSettings
)

data class ColorPalette(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color,
    val error: Color
)
```

### 4. UI Components

#### Theme Selection Screen
- Grid of preset themes with preview
- "Create Custom" option
- Material You toggle (Android 12+)
- Import/Export theme functionality

#### Custom Theme Creator
- Interactive color picker
- Live preview panel
- Color harmony suggestions
- Save with custom name

#### Typography Settings
- Font size slider (XS to XL)
- Font weight selection
- Line spacing adjustment
- Preview text samples

## Implementation Steps

1. **Phase 1: Core Theme System (2 days)**
   - Enhance ThemeManager with custom theme support
   - Implement theme persistence
   - Create preset theme collection

2. **Phase 2: Material You (1 day)**
   - Detect Android 12+ capability
   - Implement wallpaper color extraction
   - Create dynamic color schemes

3. **Phase 3: Custom Theme Creator (2 days)**
   - Build color picker UI
   - Implement live preview
   - Add save/load functionality

4. **Phase 4: Typography & Display (1 day)**
   - Font size adjustment system
   - Display density implementation
   - High contrast mode

5. **Phase 5: Testing & Polish (1 day)**
   - Cross-device testing
   - Accessibility validation
   - Performance optimization

## UI Mockup Structure
```
Settings > Appearance
├── Theme Mode [Light/Dark/System/Schedule]
├── Color Scheme
│   ├── Material You (Android 12+)
│   ├── Preset Themes (10+ options)
│   └── Custom Themes
├── Typography
│   ├── Font Size [XS|S|M|L|XL]
│   ├── Font Weight [Light|Regular|Medium|Bold]
│   └── Text Spacing [Compact|Normal|Relaxed]
├── Display
│   ├── Density [Compact|Comfortable|Spacious]
│   ├── Card Style [Rounded|Square|Stadium]
│   └── Animations [On|Reduced|Off]
└── Accessibility
    ├── High Contrast [Toggle]
    └── Color Blind Mode [None|Protanopia|Deuteranopia]
```

## Database Schema
```sql
CREATE TABLE custom_themes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    is_active BOOLEAN DEFAULT FALSE,
    is_preset BOOLEAN DEFAULT FALSE,
    color_scheme TEXT NOT NULL, -- JSON
    typography_settings TEXT, -- JSON
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE theme_preferences (
    id INTEGER PRIMARY KEY,
    theme_mode TEXT DEFAULT 'system',
    active_theme_id INTEGER REFERENCES custom_themes(id),
    font_size_scale REAL DEFAULT 1.0,
    display_density TEXT DEFAULT 'comfortable',
    high_contrast_enabled BOOLEAN DEFAULT FALSE,
    animations_enabled BOOLEAN DEFAULT TRUE,
    material_you_enabled BOOLEAN DEFAULT FALSE
);
```

## Testing Checklist
- [ ] Theme changes apply instantly
- [ ] Custom themes persist across sessions
- [ ] Material You works on Android 12+
- [ ] Graceful fallback on older devices
- [ ] All UI elements respect theme
- [ ] Accessibility standards maintained
- [ ] No performance degradation

## Dependencies
- Material Design 3 components
- Android 12 Dynamic Color APIs
- Color picker library
- Palette API for color extraction

## Priority
🟡 **MEDIUM** - Enhances user experience significantly

## Labels
`enhancement`, `ui`, `customization`, `material-you`, `medium-priority`

## Milestone
v2.1.0 - Customization Update

## Estimated Time
7 days

## Related PRDs
- [Display & Appearance PRD](../PRDs/02-display-appearance-prd.md)

## Notes
- Consider A/B testing different preset themes
- Monitor theme usage analytics
- Plan for theme sharing feature in future