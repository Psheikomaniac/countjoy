#!/bin/bash

# Branch Creation Script for CountJoy
# This script creates feature branches for all defined issues

echo "Creating feature branches for CountJoy issues..."
echo ""

# Check if we're on dev branch
current_branch=$(git branch --show-current)
if [ "$current_branch" != "dev" ]; then
    echo "Switching to dev branch..."
    git checkout dev
fi

# Pull latest changes
echo "Pulling latest changes from dev..."
git pull origin dev

# Array of issue numbers and descriptions
issues=(
    "001:project-init"
    "002:hilt-setup"
    "003:room-database"
    "004:navigation-setup"
    "005:theme-setup"
    "006:domain-models"
    "007:repository-interface"
    "008:use-cases"
    "009:repository-impl"
    "010:data-mappers"
    "011:shared-preferences"
    "012:countdown-viewmodel"
    "013:event-input-viewmodel"
    "014:countdown-screen"
    "015:event-input-screen"
    "016:custom-components"
    "017:tablet-optimization"
    "018:countdown-service"
    "019:boot-receiver"
    "020:timezone-handling"
    "021:domain-tests"
    "022:viewmodel-tests"
    "023:repository-tests"
    "024:ui-tests"
    "025:performance-optimization"
    "026:accessibility"
    "027:error-handling"
    "028:proguard-setup"
    "029:documentation"
    "030:release-prep"
)

# Counter for created branches
created=0
skipped=0

# Create branches for each issue
for issue in "${issues[@]}"; do
    IFS=':' read -r num desc <<< "$issue"
    branch="feature/ISSUE-${num}-${desc}"
    
    # Check if branch already exists locally
    if git show-ref --verify --quiet "refs/heads/$branch"; then
        echo "Branch $branch already exists locally, skipping..."
        ((skipped++))
        continue
    fi
    
    # Check if branch exists on remote
    if git ls-remote --heads origin "$branch" | grep -q "$branch"; then
        echo "Branch $branch already exists on remote, skipping..."
        ((skipped++))
        continue
    fi
    
    # Create and push the branch
    echo "Creating branch: $branch"
    git checkout -b "$branch"
    
    # Create a placeholder file to initialize the branch
    echo "# Issue ${num}: ${desc}" > ".issue-${num}.md"
    echo "Branch created for development" >> ".issue-${num}.md"
    git add ".issue-${num}.md"
    git commit -m "Initialize branch for ISSUE-${num}: ${desc}"
    
    # Push the branch
    git push -u origin "$branch"
    
    # Switch back to dev
    git checkout dev
    
    ((created++))
    echo "✓ Branch created and pushed successfully"
    echo ""
done

echo "Summary:"
echo "  Branches created: $created"
echo "  Branches skipped: $skipped"
echo ""
echo "All branches have been created!"
echo ""
echo "To start working on an issue, use:"
echo "  git checkout feature/ISSUE-XXX-description"
echo ""
echo "To see all branches:"
echo "  git branch -a"