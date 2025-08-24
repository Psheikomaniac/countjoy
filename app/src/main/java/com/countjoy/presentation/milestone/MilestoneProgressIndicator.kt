package com.countjoy.presentation.milestone

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.Milestone
import com.countjoy.domain.model.MilestoneType
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.Instant
import kotlin.math.*

@Composable
fun MilestoneProgressIndicator(
    event: CountdownEvent,
    milestones: List<Milestone>,
    modifier: Modifier = Modifier,
    onMilestoneClick: (Milestone) -> Unit = {}
) {
    val now = remember { Instant.now() }
    val totalDuration = remember { Duration.between(event.createdAt, event.targetDateTime) }
    val elapsedDuration = remember { Duration.between(event.createdAt, now) }
    val progressPercentage = remember {
        if (totalDuration.isZero) 100f
        else (elapsedDuration.toMillis().toFloat() / totalDuration.toMillis().toFloat()) * 100f
    }
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Progress Bar with Milestones
        MilestoneProgressBar(
            progress = progressPercentage,
            milestones = milestones.filter { it.type == MilestoneType.PERCENTAGE_BASED },
            onMilestoneClick = onMilestoneClick
        )
        
        // Upcoming Milestones
        val upcomingMilestones = milestones.filter { !it.isAchieved }.take(3)
        if (upcomingMilestones.isNotEmpty()) {
            UpcomingMilestonesRow(
                milestones = upcomingMilestones,
                event = event,
                onMilestoneClick = onMilestoneClick
            )
        }
    }
}

@Composable
private fun MilestoneProgressBar(
    progress: Float,
    milestones: List<Milestone>,
    onMilestoneClick: (Milestone) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        // Background track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        
        // Progress fill
        Box(
            modifier = Modifier
                .fillMaxWidth(progress / 100f)
                .height(8.dp)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
        )
        
        // Milestone markers
        milestones.forEach { milestone ->
            val position = milestone.value / 100f
            
            MilestoneMarker(
                milestone = milestone,
                position = position,
                isAchieved = progress >= milestone.value,
                onClick = { onMilestoneClick(milestone) },
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        
        // Current progress indicator
        CurrentProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MilestoneMarker(
    milestone: Milestone,
    position: Float,
    isAchieved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val animatedScale by animateFloatAsState(
        targetValue = if (isAchieved) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    Box(
        modifier = modifier
            .offset(x = with(density) { (position * 350).dp })
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = if (isAchieved) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            modifier = Modifier
                .size(24.dp * animatedScale)
                .align(Alignment.Center),
            shadowElevation = if (isAchieved) 4.dp else 0.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (isAchieved) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                } else {
                    Text(
                        text = "${milestone.value.toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // Tooltip
        AnimatedVisibility(
            visible = false, // Show on hover/long press
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-32).dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.inverseSurface,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = milestone.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CurrentProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    
    val pulseAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = modifier
            .offset(x = with(LocalDensity.current) { (animatedProgress * 350).dp })
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .size(16.dp * pulseAnimation)
                .align(Alignment.Center),
            shadowElevation = 8.dp
        ) {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun UpcomingMilestonesRow(
    milestones: List<Milestone>,
    event: CountdownEvent,
    onMilestoneClick: (Milestone) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(milestones) { milestone ->
            UpcomingMilestoneCard(
                milestone = milestone,
                event = event,
                onClick = { onMilestoneClick(milestone) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpcomingMilestoneCard(
    milestone: Milestone,
    event: CountdownEvent,
    onClick: () -> Unit
) {
    val timeUntilMilestone = calculateTimeUntilMilestone(milestone, event)
    
    Card(
        onClick = onClick,
        modifier = Modifier.width(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (milestone.type) {
                MilestoneType.PERCENTAGE_BASED -> MaterialTheme.colorScheme.primaryContainer
                MilestoneType.TIME_BASED -> MaterialTheme.colorScheme.secondaryContainer
                MilestoneType.CUSTOM -> MaterialTheme.colorScheme.tertiaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = when (milestone.type) {
                    MilestoneType.PERCENTAGE_BASED -> Icons.Default.CheckCircle
                    MilestoneType.TIME_BASED -> Icons.Default.DateRange
                    MilestoneType.CUSTOM -> Icons.Default.Star
                },
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = milestone.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = timeUntilMilestone,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MilestoneCircularProgress(
    event: CountdownEvent,
    milestones: List<Milestone>,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 200.dp
) {
    val now = remember { Instant.now() }
    val totalDuration = remember { Duration.between(event.createdAt, event.targetDateTime) }
    val elapsedDuration = remember { Duration.between(event.createdAt, now) }
    val progressPercentage = remember {
        if (totalDuration.isZero) 100f
        else (elapsedDuration.toMillis().toFloat() / totalDuration.toMillis().toFloat()) * 100f
    }
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            val radius = (size.toPx() - strokeWidth) / 2
            val center = Offset(size.toPx() / 2, size.toPx() / 2)
            
            // Background circle
            drawCircle(
                color = Color.Gray.copy(alpha = 0.2f),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )
            
            // Progress arc
            drawArc(
                color = Color(0xFF4CAF50),
                startAngle = -90f,
                sweepAngle = progressPercentage * 3.6f,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            
            // Milestone markers
            milestones.filter { it.type == MilestoneType.PERCENTAGE_BASED }.forEach { milestone ->
                val angle = -90f + (milestone.value * 3.6f)
                val markerRadius = if (progressPercentage >= milestone.value) radius + 8.dp.toPx() else radius
                val x = center.x + markerRadius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val y = center.y + markerRadius * sin(Math.toRadians(angle.toDouble())).toFloat()
                
                drawCircle(
                    color = if (progressPercentage >= milestone.value) {
                        Color(0xFF4CAF50)
                    } else {
                        Color.Gray.copy(alpha = 0.5f)
                    },
                    radius = if (progressPercentage >= milestone.value) 8.dp.toPx() else 4.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
        
        // Center content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${progressPercentage.toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            val nextMilestone = milestones
                .filter { !it.isAchieved && it.type == MilestoneType.PERCENTAGE_BASED }
                .minByOrNull { it.value }
            
            nextMilestone?.let {
                Text(
                    text = "Next: ${it.title}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun calculateTimeUntilMilestone(milestone: Milestone, event: CountdownEvent): String {
    val now = Instant.now()
    val totalDuration = Duration.between(event.createdAt, event.targetDateTime)
    
    return when (milestone.type) {
        MilestoneType.PERCENTAGE_BASED -> {
            val milestoneTime = event.createdAt.plus(
                Duration.ofMillis((totalDuration.toMillis() * milestone.value / 100).toLong())
            )
            val duration = Duration.between(now, milestoneTime)
            formatDuration(duration)
        }
        MilestoneType.TIME_BASED -> {
            val targetTime = event.targetDateTime.minus(Duration.ofDays(milestone.value.toLong()))
            val duration = Duration.between(now, targetTime)
            formatDuration(duration)
        }
        MilestoneType.CUSTOM -> "Custom"
    }
}

private fun formatDuration(duration: Duration): String {
    return when {
        duration.isNegative -> "Achieved!"
        duration.toDays() > 0 -> "in ${duration.toDays()} days"
        duration.toHours() > 0 -> "in ${duration.toHours()} hours"
        duration.toMinutes() > 0 -> "in ${duration.toMinutes()} minutes"
        else -> "Soon!"
    }
}