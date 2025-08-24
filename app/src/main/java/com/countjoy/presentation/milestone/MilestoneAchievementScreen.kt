package com.countjoy.presentation.milestone

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.domain.model.CelebrationEffect
import com.countjoy.domain.model.Milestone
import com.countjoy.domain.model.MilestoneType
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.LocalDate
import android.content.Intent
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneAchievementScreen(
    onNavigateBack: () -> Unit,
    viewModel: MilestoneViewModel = hiltViewModel()
) {
    val achievementHistory by viewModel.achievementHistory.collectAsState()
    val groupedAchievements = achievementHistory.groupBy { milestone ->
        milestone.achievedAt?.atZone(java.time.ZoneId.systemDefault())?.toLocalDate()
    }.toSortedMap(compareByDescending<LocalDate?> { it })
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Achievement History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (achievementHistory.isNotEmpty()) {
                        IconButton(onClick = { /* Export achievements */ }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (achievementHistory.isEmpty()) {
            EmptyAchievementsView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Statistics Card
                item {
                    AchievementStatisticsCard(achievementHistory)
                }
                
                // Grouped achievements by date
                groupedAchievements.forEach { entry ->
                    val date = entry.key
                    val milestones = entry.value
                    item {
                        if (date != null) {
                            Text(
                                text = date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                    
                    items(milestones) { milestone: Milestone ->
                        AchievementCard(
                            milestone = milestone,
                            onShare = { shareMilestone(milestone) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyAchievementsView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No Achievements Yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Complete milestones to see them here!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AchievementStatisticsCard(achievements: List<Milestone>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Your Achievements",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    value = achievements.size.toString(),
                    label = "Total",
                    icon = Icons.Default.Star
                )
                
                StatisticItem(
                    value = achievements.count { it.type == MilestoneType.PERCENTAGE_BASED }.toString(),
                    label = "Progress",
                    icon = Icons.Default.Check
                )
                
                StatisticItem(
                    value = achievements.count { it.type == MilestoneType.TIME_BASED }.toString(),
                    label = "Time",
                    icon = Icons.Default.Schedule
                )
                
                StatisticItem(
                    value = achievements.count { it.type == MilestoneType.CUSTOM }.toString(),
                    label = "Custom",
                    icon = Icons.Default.Star
                )
            }
        }
    }
}

@Composable
private fun StatisticItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(48.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AchievementCard(
    milestone: Milestone,
    onShare: () -> Unit
) {
    var showCelebration by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showCelebration = true }
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Achievement Icon
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (milestone.type) {
                        MilestoneType.PERCENTAGE_BASED -> MaterialTheme.colorScheme.primaryContainer
                        MilestoneType.TIME_BASED -> MaterialTheme.colorScheme.secondaryContainer
                        MilestoneType.CUSTOM -> MaterialTheme.colorScheme.tertiaryContainer
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = when (milestone.type) {
                                MilestoneType.PERCENTAGE_BASED -> Icons.Default.Check
                                MilestoneType.TIME_BASED -> Icons.Default.Schedule
                                MilestoneType.CUSTOM -> Icons.Default.Star
                            },
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Achievement Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = milestone.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = milestone.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    milestone.achievedAt?.let { achievedAt ->
                        Text(
                            text = achievedAt.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
                
                // Share Button
                IconButton(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "🎉 I just achieved: ${milestone.title}! ${milestone.message}")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Achievement"))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // Celebration Animation Overlay
            if (showCelebration) {
                CelebrationAnimation(
                    effect = milestone.celebrationEffect,
                    isPlaying = showCelebration,
                    onAnimationComplete = { showCelebration = false }
                )
            }
        }
    }
}

private fun shareMilestone(milestone: Milestone) {
    // Implementation handled in AchievementCard
}