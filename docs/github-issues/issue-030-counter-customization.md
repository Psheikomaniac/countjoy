# Issue #30: Implement Counter Display Customization

## Description
Allow users to customize how countdowns are displayed, including format options, update frequencies, visual themes, and precision settings.

## Acceptance Criteria
- [ ] Multiple display format options (days only, full breakdown, compact, percentage)
- [ ] Configurable update frequency (1s, 10s, 1m, 1h)
- [ ] Visual theme selection for counters
- [ ] Font style and size customization
- [ ] Color scheme selection
- [ ] Animation preferences
- [ ] Time zone management for international events
- [ ] Show/hide specific time units (years, months, days, hours, minutes, seconds)
- [ ] Number formatting options (leading zeros, separators)

## Technical Requirements
- Create CountdownFormat enum with display options
- Implement format preview system
- Store customization preferences per event
- Create theme templates system
- Optimize update frequency for battery life
- Handle time zone conversions properly

## Implementation Steps
1. Design counter customization UI
2. Create format selection dialog
3. Implement preview panel
4. Add update frequency settings
5. Create visual theme picker
6. Implement font customization
7. Add color palette selection
8. Create animation settings
9. Implement time zone selector
10. Add format templates
11. Test performance with different update frequencies

## Priority: Medium
## Estimated Effort: 4 days
## Labels: feature, customization, ui