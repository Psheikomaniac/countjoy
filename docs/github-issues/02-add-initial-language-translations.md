# GitHub Issue #2: Add Initial Language Translations

## Summary
Add professional translations for the first batch of supported languages: Spanish, French, German, and Portuguese.

## Description
After implementing the language switching infrastructure (#1), we need to add actual translations for the initial set of languages. This issue covers creating translated string resources for the most commonly requested languages.

## Acceptance Criteria
- [ ] Complete Spanish (es) translations
- [ ] Complete French (fr) translations  
- [ ] Complete German (de) translations
- [ ] Complete Portuguese (pt) translations
- [ ] All translations professionally reviewed
- [ ] Cultural appropriateness validated
- [ ] Plurals handled correctly in each language

## Languages to Add

### Spanish (es)
- Locale: `values-es/`
- Region variants: `values-es-rMX/` (Mexican Spanish)
- Special considerations: Formal vs informal address

### French (fr)
- Locale: `values-fr/`
- Region variants: `values-fr-rCA/` (Canadian French)
- Special considerations: Gender agreement

### German (de)
- Locale: `values-de/`
- Region variants: `values-de-rCH/` (Swiss German)
- Special considerations: Formal "Sie" vs informal "du"

### Portuguese (pt)
- Locale: `values-pt/`
- Region variants: `values-pt-rBR/` (Brazilian Portuguese)
- Special considerations: European vs Brazilian differences

## Translation Guidelines

### Quality Standards
1. Use professional translation service or native speakers
2. Maintain consistent terminology throughout
3. Consider cultural context and idioms
4. Keep translations concise (UI space constraints)
5. Test in actual UI to ensure proper fit

### String Categories to Translate

#### Core UI (Priority 1)
- Navigation labels
- Button text
- Menu items
- Dialog titles and messages
- Error messages

#### Settings (Priority 2)
- Setting labels
- Setting descriptions
- Option values
- Help text

#### Features (Priority 3)
- Countdown labels
- Time units (days, hours, minutes)
- Event types
- Category names

#### Notifications (Priority 4)
- Notification titles
- Notification body text
- Action buttons

## Implementation Steps

1. **Preparation (1 day)**
   - Export base `strings.xml` to translation format
   - Prepare translation guidelines document
   - Set up translation review process

2. **Translation (3 days)**
   - Send strings to translation service
   - Review and refine translations
   - Handle special cases and context

3. **Integration (1 day)**
   - Create locale-specific resource directories
   - Add translated `strings.xml` files
   - Test each language in the app

4. **Validation (1 day)**
   - Native speaker review
   - UI fit testing
   - Cultural appropriateness check

## Resource Files Structure

```xml
<!-- values-es/strings.xml -->
<resources>
    <string name="app_name">CountJoy</string>
    <string name="add_event">Añadir Evento</string>
    <string name="settings">Configuración</string>
    <string name="days">días</string>
    <plurals name="days_count">
        <item quantity="one">%d día</item>
        <item quantity="other">%d días</item>
    </plurals>
    <!-- ... more translations ... -->
</resources>
```

## Translation Tracking

| String Key | English | Spanish | French | German | Portuguese |
|------------|---------|---------|--------|--------|------------|
| app_name | CountJoy | CountJoy | CountJoy | CountJoy | CountJoy |
| add_event | Add Event | Añadir Evento | Ajouter Événement | Ereignis hinzufügen | Adicionar Evento |
| settings | Settings | Configuración | Paramètres | Einstellungen | Configurações |
| ... | ... | ... | ... | ... | ... |

## Testing Checklist
- [ ] All strings display correctly in each language
- [ ] No text truncation or overflow
- [ ] Plurals work correctly
- [ ] Date/time formats appropriate for locale
- [ ] Special characters display properly
- [ ] RTL preparation for future Arabic support

## Dependencies
- Depends on #1 (Language Switching Infrastructure)
- Translation service or native speakers

## Priority
🟡 **MEDIUM** - Important for international users

## Labels
`enhancement`, `i18n`, `translations`, `medium-priority`

## Milestone
v2.0.0 - International Release

## Estimated Time
6 days

## Notes
- Consider using Crowdin or Lokalise for ongoing translation management
- Set up automated translation workflow for future updates
- Create glossary of CountJoy-specific terms

## Related Issues
- #1: Implement Language Switching Infrastructure
- #3: Add RTL Language Support
- #4: Implement Regional Format Settings