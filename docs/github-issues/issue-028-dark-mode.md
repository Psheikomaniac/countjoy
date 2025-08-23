# Issue #28: Implement Dark Mode Theme Switcher

## Description
Add Dark Mode support with Light/Dark/System theme options in Settings, allowing users to switch between different visual themes for better viewing comfort.

## Acceptance Criteria
- [ ] Settings screen includes theme selector with three options: System, Light, Dark
- [ ] Theme preference persists across app restarts using SharedPreferences
- [ ] Theme changes apply immediately without requiring app restart
- [ ] System option follows device theme settings
- [ ] All screens properly support both light and dark themes
- [ ] Status bar and navigation bar colors adapt to selected theme

## Technical Requirements
- Use Material3 theming system
- Store theme preference in SharedPreferences with key `theme_mode`
- Implement theme observation in MainActivity
- Update all composables to use theme-aware colors
- Test on Android 10+ for system theme detection

## Implementation Steps
1. ✅ Create SettingsScreen composable with theme selector UI
2. ✅ Create SettingsViewModel for managing preferences
3. ✅ Update Theme.kt to observe SharedPreferences
4. ✅ Add Settings navigation route
5. ✅ Add Settings icon to main screen app bar
6. Test theme switching across all screens
7. Verify theme persistence after app restart

## Priority: High
## Estimated Effort: 3 days
## Labels: feature, ui, settings