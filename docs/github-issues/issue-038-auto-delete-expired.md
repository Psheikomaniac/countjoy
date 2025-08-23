# Issue #38: Implement Auto-Delete Expired Events

## Description
Add functionality to automatically remove expired countdown events based on user preferences, with options for grace periods and selective deletion.

## Acceptance Criteria
- [ ] Toggle to enable/disable auto-delete
- [ ] Grace period configuration (days after expiry)
- [ ] Selective auto-delete by category
- [ ] Warning before deletion
- [ ] Undo capability within time window
- [ ] Archive option instead of delete
- [ ] Deletion log/history
- [ ] Bulk manual deletion of expired events

## Technical Requirements
- Use WorkManager for scheduled cleanup
- Implement soft delete with recovery period
- Create archive storage separate from active events
- Store deletion preferences per category
- Log all deletion activities

## Implementation Steps
1. Add auto-delete setting to Settings screen
2. Create deletion service with WorkManager
3. Implement grace period logic
4. Add category-based settings
5. Create archive functionality
6. Implement undo mechanism
7. Add deletion history log
8. Create bulk delete UI
9. Test scheduled deletion
10. Verify data recovery

## Priority: Low
## Estimated Effort: 3 days
## Labels: feature, data-management, automation