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

# Continue for more issues...
echo ""
echo "Basic issues created! Continue this script for all 30 issues."
echo "You can also create issues manually through the GitHub web interface."
echo ""
echo "To create branches for each issue, run:"
echo "bash create-branches.sh"