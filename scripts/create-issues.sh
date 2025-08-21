#!/bin/bash

# GitHub Issues Creation Script for CountJoy
# This script creates all the issues defined in the project plan

echo "Creating GitHub Issues for CountJoy..."
echo "Please ensure you have GitHub CLI (gh) installed and authenticated"
echo ""

# Check if gh is installed
if ! command -v gh &> /dev/null; then
    echo "GitHub CLI (gh) is not installed. Please install it first:"
    echo "brew install gh (macOS)"
    echo "or visit: https://cli.github.com/"
    exit 1
fi

# Check if we're in the right repository
if ! git remote -v | grep -q "Psheikomaniac/countjoy"; then
    echo "Error: Not in the countjoy repository"
    exit 1
fi

# Create labels first
echo "Creating labels..."
gh label create "P0" --description "Critical Priority" --color "FF0000" 2>/dev/null
gh label create "P1" --description "High Priority" --color "FFA500" 2>/dev/null
gh label create "P2" --description "Medium Priority" --color "FFFF00" 2>/dev/null
gh label create "P3" --description "Low Priority" --color "00FF00" 2>/dev/null

gh label create "feature" --description "New feature" --color "0E8A16" 2>/dev/null
gh label create "bug" --description "Bug fix" --color "D73A4A" 2>/dev/null
gh label create "enhancement" --description "Enhancement" --color "A2EEEF" 2>/dev/null
gh label create "documentation" --description "Documentation" --color "0075CA" 2>/dev/null
gh label create "test" --description "Testing" --color "FBCA04" 2>/dev/null
gh label create "hotfix" --description "Critical hotfix" --color "E11D21" 2>/dev/null

gh label create "ui" --description "UI/Presentation Layer" --color "7057FF" 2>/dev/null
gh label create "domain" --description "Domain Layer" --color "008672" 2>/dev/null
gh label create "data" --description "Data Layer" --color "006B75" 2>/dev/null
gh label create "testing" --description "Testing" --color "FEF2C0" 2>/dev/null
gh label create "ci-cd" --description "CI/CD" --color "BFD4F2" 2>/dev/null

echo "Labels created successfully!"
echo ""

# Create milestones
echo "Creating milestones..."
gh api repos/:owner/:repo/milestones -f title="Phase 1: Project Setup" -f description="Week 1-2: Project infrastructure and setup" -f due_on="2024-02-14T23:59:59Z" 2>/dev/null
gh api repos/:owner/:repo/milestones -f title="Phase 2: Core Implementation" -f description="Week 3-4: Domain and data layers" -f due_on="2024-02-28T23:59:59Z" 2>/dev/null
gh api repos/:owner/:repo/milestones -f title="Phase 3: UI Development" -f description="Week 4-5: User interface implementation" -f due_on="2024-03-07T23:59:59Z" 2>/dev/null
gh api repos/:owner/:repo/milestones -f title="Phase 4: Testing & QA" -f description="Week 5-6: Comprehensive testing" -f due_on="2024-03-14T23:59:59Z" 2>/dev/null
gh api repos/:owner/:repo/milestones -f title="Phase 5: Release Preparation" -f description="Week 7: Final preparations for release" -f due_on="2024-03-21T23:59:59Z" 2>/dev/null

echo "Milestones created successfully!"
echo ""

# Function to create an issue
create_issue() {
    local title="$1"
    local body="$2"
    local labels="$3"
    local milestone="$4"
    
    echo "Creating issue: $title"
    gh issue create \
        --title "$title" \
        --body "$body" \
        --label "$labels" \
        --milestone "$milestone" 2>/dev/null || echo "  Issue might already exist, skipping..."
}

# Phase 1 Issues
echo "Creating Phase 1 issues..."

create_issue "Initialize Android Project" \
"## Description
Create new Android project with Kotlin and Compose

## Acceptance Criteria
- [ ] Android project created with correct package name (com.countjoy)
- [ ] Kotlin configured
- [ ] Jetpack Compose enabled
- [ ] Minimum SDK 24, Target SDK 34
- [ ] Gradle configuration complete

## Branch
\`feature/ISSUE-001-project-init\`" \
"feature,P0" \
"Phase 1: Project Setup"

create_issue "Setup Dependency Injection with Hilt" \
"## Description
Configure Hilt for dependency injection

## Acceptance Criteria
- [ ] Hilt dependencies added
- [ ] Application class annotated with @HiltAndroidApp
- [ ] Basic module structure created
- [ ] MainActivity annotated with @AndroidEntryPoint

## Branch
\`feature/ISSUE-002-hilt-setup\`" \
"feature,P0" \
"Phase 1: Project Setup"

create_issue "Configure Room Database" \
"## Description
Setup Room database for local storage

## Acceptance Criteria
- [ ] Room dependencies added
- [ ] Database class created
- [ ] DAO interfaces defined
- [ ] Entity classes created
- [ ] Type converters for LocalDateTime

## Branch
\`feature/ISSUE-003-room-database\`" \
"feature,P0,data" \
"Phase 1: Project Setup"

create_issue "Setup Navigation Component" \
"## Description
Configure Navigation Compose

## Acceptance Criteria
- [ ] Navigation dependencies added
- [ ] NavHost configured
- [ ] Navigation graph defined
- [ ] Screen routes established

## Branch
\`feature/ISSUE-004-navigation-setup\`" \
"feature,P0,ui" \
"Phase 1: Project Setup"

create_issue "Create Base Theme and Styling" \
"## Description
Setup Material Design 3 theme

## Acceptance Criteria
- [ ] Color scheme defined
- [ ] Typography configured
- [ ] Dark/Light theme support
- [ ] Custom components styled

## Branch
\`feature/ISSUE-005-theme-setup\`" \
"feature,P1,ui" \
"Phase 1: Project Setup"

# Phase 2 Issues
echo "Creating Phase 2 issues..."

create_issue "Create Domain Models" \
"## Description
Define domain models for the application

## Acceptance Criteria
- [ ] CountdownEvent model created
- [ ] CountdownTime model created
- [ ] Validation logic included
- [ ] Documentation added

## Branch
\`feature/ISSUE-006-domain-models\`" \
"feature,P0,domain" \
"Phase 2: Core Implementation"

create_issue "Implement Repository Interface" \
"## Description
Define repository interface in domain layer

## Acceptance Criteria
- [ ] EventRepository interface created
- [ ] CRUD operations defined
- [ ] Flow return types used
- [ ] Error handling considered

## Branch
\`feature/ISSUE-007-repository-interface\`" \
"feature,P0,domain" \
"Phase 2: Core Implementation"

create_issue "Create Use Cases" \
"## Description
Implement business logic use cases

## Acceptance Criteria
- [ ] GetEventUseCase implemented
- [ ] CreateEventUseCase with validation
- [ ] UpdateEventUseCase implemented
- [ ] DeleteEventUseCase implemented
- [ ] CalculateCountdownUseCase created

## Branch
\`feature/ISSUE-008-use-cases\`" \
"feature,P0,domain" \
"Phase 2: Core Implementation"

create_issue "Implement Repository" \
"## Description
Create repository implementation

## Acceptance Criteria
- [ ] EventRepositoryImpl created
- [ ] DAO integration complete
- [ ] Error handling implemented
- [ ] Data mapping configured

## Branch
\`feature/ISSUE-009-repository-impl\`" \
"feature,P0,data" \
"Phase 2: Core Implementation"

create_issue "Create Data Mappers" \
"## Description
Implement mappers between entity and domain models

## Acceptance Criteria
- [ ] Entity to Domain mapper
- [ ] Domain to Entity mapper
- [ ] DateTime conversion handled
- [ ] Null safety ensured

## Branch
\`feature/ISSUE-010-data-mappers\`" \
"feature,P1,data" \
"Phase 2: Core Implementation"

# Phase 3 Issues - UI Development
echo "Creating Phase 3 issues..."

create_issue "Setup SharedPreferences Manager" \
"## Description
Create SharedPreferences wrapper for simple storage

## Acceptance Criteria
- [ ] SharedPreferencesManager class created
- [ ] Type-safe methods implemented  
- [ ] Default values handled
- [ ] Migration path considered

## Branch
\`feature/ISSUE-011-shared-preferences\`" \
"feature,P2,data" \
"Phase 2: Core Implementation"

create_issue "Create Countdown ViewModel" \
"## Description
Implement ViewModel for countdown screen

## Acceptance Criteria
- [ ] CountdownViewModel created
- [ ] StateFlow for UI state
- [ ] Countdown timer logic
- [ ] Lifecycle awareness
- [ ] Error handling

## Branch
\`feature/ISSUE-012-countdown-viewmodel\`" \
"feature,P0,ui" \
"Phase 3: UI Development"

create_issue "Create Event Input ViewModel" \
"## Description
Implement ViewModel for event input screen

## Acceptance Criteria
- [ ] EventInputViewModel created
- [ ] Form validation logic
- [ ] Event creation handling
- [ ] Navigation events
- [ ] Error states

## Branch
\`feature/ISSUE-013-event-input-viewmodel\`" \
"feature,P0,ui" \
"Phase 3: UI Development"

create_issue "Implement Countdown Screen" \
"## Description
Create main countdown display screen

## Acceptance Criteria
- [ ] Countdown display composable
- [ ] Empty state handling
- [ ] Expired state display
- [ ] FAB for adding event
- [ ] Responsive layout

## Branch
\`feature/ISSUE-014-countdown-screen\`" \
"feature,P0,ui" \
"Phase 3: UI Development"

create_issue "Implement Event Input Screen" \
"## Description
Create event creation/editing screen

## Acceptance Criteria
- [ ] Event name input field
- [ ] Date picker integration
- [ ] Optional time picker
- [ ] Form validation UI
- [ ] Save/Cancel actions

## Branch
\`feature/ISSUE-015-event-input-screen\`" \
"feature,P0,ui" \
"Phase 3: UI Development"

create_issue "Create Custom Components" \
"## Description
Build reusable UI components

## Acceptance Criteria
- [ ] CountdownDisplay component
- [ ] DateTimePicker component
- [ ] EventCard component
- [ ] Loading indicators
- [ ] Error displays

## Branch
\`feature/ISSUE-016-custom-components\`" \
"feature,P1,ui" \
"Phase 3: UI Development"

create_issue "Implement Tablet Layout Optimization" \
"## Description
Optimize UI for tablet screens

## Acceptance Criteria
- [ ] Landscape layout support
- [ ] Responsive text sizing
- [ ] Adaptive layouts
- [ ] Touch targets optimized
- [ ] Multi-pane layouts considered

## Branch
\`feature/ISSUE-017-tablet-optimization\`" \
"feature,P1,ui" \
"Phase 3: UI Development"

# Background Services
echo "Creating background service issues..."

create_issue "Create Countdown Service" \
"## Description
Implement background service for countdown updates

## Acceptance Criteria
- [ ] Service class created
- [ ] Coroutine-based updates
- [ ] Lifecycle management
- [ ] Battery optimization
- [ ] Foreground service consideration

## Branch
\`feature/ISSUE-018-countdown-service\`" \
"feature,P2" \
"Phase 3: UI Development"

create_issue "Implement Boot Receiver" \
"## Description
Handle app restart after device reboot

## Acceptance Criteria
- [ ] BroadcastReceiver created
- [ ] Boot permission added
- [ ] Service restart logic
- [ ] State restoration
- [ ] Testing strategy

## Branch
\`feature/ISSUE-019-boot-receiver\`" \
"feature,P2" \
"Phase 3: UI Development"

create_issue "Handle Timezone Changes" \
"## Description
Properly handle timezone changes

## Acceptance Criteria
- [ ] Timezone change detection
- [ ] Countdown recalculation
- [ ] UI updates triggered
- [ ] Edge cases handled
- [ ] Testing scenarios

## Branch
\`feature/ISSUE-020-timezone-handling\`" \
"feature,P1" \
"Phase 3: UI Development"

# Phase 4 - Testing
echo "Creating Phase 4 testing issues..."

create_issue "Unit Tests for Domain Layer" \
"## Description
Write unit tests for domain layer

## Acceptance Criteria
- [ ] Use case tests
- [ ] Model validation tests
- [ ] Edge case coverage
- [ ] 90% code coverage
- [ ] Documentation

## Branch
\`feature/ISSUE-021-domain-tests\`" \
"test,P0,testing" \
"Phase 4: Testing & QA"

create_issue "Unit Tests for ViewModels" \
"## Description
Write unit tests for ViewModels

## Acceptance Criteria
- [ ] State transformation tests
- [ ] Coroutine testing
- [ ] Error handling tests
- [ ] 85% code coverage
- [ ] Turbine for Flow testing

## Branch
\`feature/ISSUE-022-viewmodel-tests\`" \
"test,P0,testing" \
"Phase 4: Testing & QA"

create_issue "Integration Tests for Repository" \
"## Description
Write integration tests for data layer

## Acceptance Criteria
- [ ] Database integration tests
- [ ] Repository tests
- [ ] Mapper tests
- [ ] Error scenarios
- [ ] Performance tests

## Branch
\`feature/ISSUE-023-repository-tests\`" \
"test,P1,testing" \
"Phase 4: Testing & QA"

create_issue "UI Tests for Compose Screens" \
"## Description
Write UI tests for Compose screens

## Acceptance Criteria
- [ ] Screen navigation tests
- [ ] User interaction tests
- [ ] State verification
- [ ] Accessibility tests
- [ ] Screenshot tests

## Branch
\`feature/ISSUE-024-ui-tests\`" \
"test,P1,testing" \
"Phase 4: Testing & QA"

# Phase 5 - Polish & Optimization
echo "Creating Phase 5 optimization issues..."

create_issue "Performance Optimization" \
"## Description
Optimize app performance

## Acceptance Criteria
- [ ] Startup time < 2 seconds
- [ ] Smooth animations (60 FPS)
- [ ] Memory usage < 50MB
- [ ] Battery optimization
- [ ] Baseline profiles

## Branch
\`feature/ISSUE-025-performance-optimization\`" \
"enhancement,P1" \
"Phase 4: Testing & QA"

create_issue "Accessibility Improvements" \
"## Description
Ensure app meets accessibility standards

## Acceptance Criteria
- [ ] Content descriptions added
- [ ] Touch targets sized properly
- [ ] Screen reader support
- [ ] High contrast support
- [ ] AA compliance verified

## Branch
\`feature/ISSUE-026-accessibility\`" \
"enhancement,P1,ui" \
"Phase 4: Testing & QA"

create_issue "Error Handling & Recovery" \
"## Description
Implement comprehensive error handling

## Acceptance Criteria
- [ ] Graceful error recovery
- [ ] User-friendly error messages
- [ ] Retry mechanisms
- [ ] Offline handling
- [ ] Crash prevention

## Branch
\`feature/ISSUE-027-error-handling\`" \
"enhancement,P1" \
"Phase 4: Testing & QA"

create_issue "ProGuard Configuration" \
"## Description
Configure ProGuard for release builds

## Acceptance Criteria
- [ ] ProGuard rules defined
- [ ] Critical classes kept
- [ ] Size optimization
- [ ] Obfuscation enabled
- [ ] Testing verified

## Branch
\`feature/ISSUE-028-proguard-setup\`" \
"feature,P1" \
"Phase 5: Release Preparation"

# Final Release Preparation
echo "Creating release preparation issues..."

create_issue "Complete Documentation" \
"## Description
Finalize all documentation

## Acceptance Criteria
- [ ] Code documentation complete
- [ ] README updated
- [ ] API documentation
- [ ] User guide created
- [ ] Architecture documented

## Branch
\`feature/ISSUE-029-documentation\`" \
"documentation,P1" \
"Phase 5: Release Preparation"

create_issue "Prepare Release Build" \
"## Description
Prepare app for release

## Acceptance Criteria
- [ ] Version number updated
- [ ] Release notes written
- [ ] APK signing configured
- [ ] Play Store assets ready
- [ ] Privacy policy updated

## Branch
\`feature/ISSUE-030-release-prep\`" \
"feature,P0" \
"Phase 5: Release Preparation"

echo ""
echo "✅ All 30 issues have been created successfully!"
echo ""
echo "View your issues at: https://github.com/Psheikomaniac/countjoy/issues"
echo ""
echo "Next steps:"
echo "1. Review the created issues on GitHub"
echo "2. Assign issues to team members"
echo "3. Create feature branches: bash create-branches.sh"
echo "4. Start development with the highest priority (P0) issues"
echo ""
echo "Happy coding! 🚀"