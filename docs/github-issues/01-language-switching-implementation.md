# GitHub Issue #1: Implement Language Switching Infrastructure

## Summary
Add comprehensive language switching capability to CountJoy, allowing users to change the app language at runtime without losing data or requiring restart.

## Description
Currently, CountJoy has hardcoded English strings throughout the UI and no infrastructure for supporting multiple languages. This issue implements the core infrastructure for internationalization (i18n) and runtime language switching.

## Acceptance Criteria
- [ ] All hardcoded strings extracted to `strings.xml` resources
- [ ] Language selection UI in Settings screen
- [ ] Runtime language switching without app restart
- [ ] Proper RTL (Right-to-Left) support for Arabic/Hebrew
- [ ] Language preference persisted across app sessions
- [ ] Fallback to English for missing translations

## Technical Requirements

### 1. String Extraction
- Extract ALL hardcoded strings from Kotlin files
- Create comprehensive `strings.xml` with proper naming conventions
- Add plurals support for quantity strings
- Include string formatting placeholders where needed

### 2. Locale Management
```kotlin
// Create LocaleManager singleton
class LocaleManager @Inject constructor(
    private val context: Context,
    private val preferencesManager: SharedPreferencesManager
) {
    fun setAppLocale(languageCode: String)
    fun getCurrentLocale(): Locale
    fun applyLocaleToContext(context: Context): Context
}
```

### 3. Compose Integration
- Implement `LocaleProvider` composable
- Handle configuration changes properly
- Update all `stringResource()` calls
- Add `LocalLayoutDirection` for RTL support

### 4. Settings UI
- Add Language section to SettingsScreen
- Display languages in their native names
- Show flags for visual identification
- Include translation completeness percentage

### 5. Resource Structure
```
res/
├── values/           # Default (English)
├── values-es/        # Spanish
├── values-fr/        # French
├── values-de/        # German
├── values-ar/        # Arabic (RTL)
└── values-[locale]/  # Other languages
```

## Implementation Steps

1. **Phase 1: String Extraction (2 days)**
   - Scan all `.kt` files for hardcoded strings
   - Create base `strings.xml` with all strings
   - Replace hardcoded strings with `stringResource()` calls

2. **Phase 2: Locale Infrastructure (2 days)**
   - Implement `LocaleManager` class
   - Add locale persistence to SharedPreferences
   - Create `updateBaseContextLocale()` extension

3. **Phase 3: UI Implementation (1 day)**
   - Add Language Picker to Settings
   - Implement language list with native names
   - Add change confirmation dialog

4. **Phase 3: Testing (1 day)**
   - Test configuration changes
   - Verify RTL layout behavior
   - Ensure data persistence across language changes

## Files to Modify
- `app/src/main/java/com/countjoy/MainActivity.kt`
- `app/src/main/java/com/countjoy/presentation/settings/SettingsScreen.kt`
- `app/src/main/java/com/countjoy/presentation/settings/SettingsViewModel.kt`
- `app/src/main/java/com/countjoy/data/local/preferences/SharedPreferencesManager.kt`
- `app/src/main/res/values/strings.xml`
- All screen files with hardcoded strings

## Dependencies
- No external libraries required
- Uses Android's built-in locale system

## Testing Checklist
- [ ] Language changes apply immediately
- [ ] All screens display in selected language  
- [ ] RTL languages display correctly
- [ ] Data persists after language change
- [ ] Fallback to English works for missing translations
- [ ] Widget text updates with language change

## Priority
🔴 **HIGH** - Core functionality affecting all users

## Labels
`enhancement`, `i18n`, `settings`, `high-priority`

## Milestone
v2.0.0 - International Release

## Estimated Time
6 days

## Related PRDs
- [Language & Localization PRD](../PRDs/01-language-localization-prd.md)