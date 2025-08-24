package com.countjoy.presentation.analytics

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.domain.model.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsDashboardScreen(
    onBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val statistics by viewModel.statistics.collectAsState()
    val categoryDistribution by viewModel.categoryDistribution.collectAsState()
    val milestoneStats by viewModel.milestoneStats.collectAsState()
    val insights by viewModel.insights.collectAsState()
    val selectedTimeRange by viewModel.selectedTimeRange.collectAsState()
    
    var showExportDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(Icons.Default.Share, "Export")
                    }
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Time Range Selector
            item {
                TimeRangeSelector(
                    selectedRange = selectedTimeRange,
                    onRangeSelected = { viewModel.setTimeRange(it) }
                )
            }
            
            // Overview Cards
            item {
                statistics?.let { stats ->
                    OverviewCards(stats)
                }
            }
            
            // Category Distribution Chart
            item {
                categoryDistribution?.let { distribution ->
                    if (distribution.isNotEmpty()) {
                        CategoryDistributionChart(distribution)
                    }
                }
            }
            
            // Milestone Statistics
            item {
                milestoneStats?.let { stats ->
                    MilestoneStatsCard(stats)
                }
            }
            
            // Activity Timeline
            item {
                statistics?.let { stats ->
                    if (stats.creationTrend.isNotEmpty()) {
                        ActivityTimelineChart(stats.creationTrend)
                    }
                }
            }
            
            // Productivity Insights
            item {
                insights?.let { productivityInsights ->
                    ProductivityInsightsCard(productivityInsights)
                }
            }
            
            // Recent Events
            item {
                statistics?.let { stats ->
                    RecentEventsSection(
                        upcomingEvents = stats.upcomingEvents,
                        completedEvents = stats.recentlyCompleted
                    )
                }
            }
        }
    }
    
    if (showExportDialog) {
        ExportDialog(
            onDismiss = { showExportDialog = false },
            onExport = { options ->
                viewModel.exportReport(options)
                showExportDialog = false
            }
        )
    }
}

@Composable
fun TimeRangeSelector(
    selectedRange: TimeRange,
    onRangeSelected: (TimeRange) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(TimeRange.values().toList()) { range ->
            FilterChip(
                selected = selectedRange == range,
                onClick = { onRangeSelected(range) },
                label = {
                    Text(
                        when (range) {
                            TimeRange.TODAY -> "Today"
                            TimeRange.THIS_WEEK -> "This Week"
                            TimeRange.THIS_MONTH -> "This Month"
                            TimeRange.LAST_30_DAYS -> "Last 30 Days"
                            TimeRange.THIS_YEAR -> "This Year"
                            TimeRange.ALL_TIME -> "All Time"
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun OverviewCards(statistics: EventStatistics) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Total",
            value = statistics.totalEvents.toString(),
            icon = Icons.Default.DateRange,
            color = MaterialTheme.colorScheme.primary
        )
        
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Active",
            value = statistics.activeEvents.toString(),
            icon = Icons.Default.PlayArrow,
            color = MaterialTheme.colorScheme.secondary
        )
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Completed",
            value = statistics.completedEvents.toString(),
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF4CAF50)
        )
        
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Success Rate",
            value = "${statistics.successRate.toInt()}%",
            icon = Icons.Default.Done,
            color = Color(0xFFFFA726)
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CategoryDistributionChart(distribution: List<CategoryDistribution>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Category Distribution",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Simple bar chart representation
            distribution.forEach { category ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.category,
                        modifier = Modifier.width(100.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(category.percentage / 100f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(category.color))
                        )
                    }
                    
                    Text(
                        text = "${category.percentage.toInt()}%",
                        modifier = Modifier.width(40.dp),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun MilestoneStatsCard(stats: MilestoneStats) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Milestone Achievements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${stats.achievedMilestones}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Achieved",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${stats.achievementRate.toInt()}%",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        "Success Rate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stats.mostAchievedType ?: "N/A",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Top Type",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityTimelineChart(trend: List<DateCount>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Activity Timeline",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Simple line chart visualization
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                // Draw timeline chart
                // This is a simplified representation
                // In production, use a proper charting library
            }
        }
    }
}

@Composable
fun ProductivityInsightsCard(insights: ProductivityInsights) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Productivity Insights",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            InsightRow(
                icon = Icons.Default.DateRange,
                label = "Most Productive Day",
                value = insights.mostProductiveDay
            )
            
            InsightRow(
                icon = Icons.Default.Schedule,
                label = "Peak Hour",
                value = "${insights.mostProductiveHour}:00"
            )
            
            InsightRow(
                icon = Icons.Default.Done,
                label = "Weekly Average",
                value = "%.1f events".format(insights.averageEventsPerWeek)
            )
            
            InsightRow(
                icon = Icons.Default.Star,
                label = "Current Streak",
                value = "${insights.streakDays} days"
            )
            
            if (insights.suggestions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    "Suggestions",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                insights.suggestions.forEach { suggestion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFFFA726)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            suggestion,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InsightRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun RecentEventsSection(
    upcomingEvents: List<CountdownEvent>,
    completedEvents: List<CountdownEvent>
) {
    Column {
        if (upcomingEvents.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Upcoming Events",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    upcomingEvents.forEach { event ->
                        EventListItem(event)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (completedEvents.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Recently Completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    completedEvents.forEach { event ->
                        EventListItem(event)
                    }
                }
            }
        }
    }
}

@Composable
fun EventListItem(event: CountdownEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    if (event.isActive) MaterialTheme.colorScheme.primary
                    else Color(0xFF4CAF50)
                )
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                event.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                    .format(event.targetDateTime.atZone(java.time.ZoneId.systemDefault())),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportDialog(
    onDismiss: () -> Unit,
    onExport: (ExportOptions) -> Unit
) {
    var selectedFormat by remember { mutableStateOf(ExportFormat.PDF) }
    var includeCharts by remember { mutableStateOf(true) }
    var includeRawData by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Export Analytics Report") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Select export format:")
                
                ExportFormat.values().forEach { format ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedFormat == format,
                            onClick = { selectedFormat = format }
                        )
                        Text(
                            format.name,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                
                Divider()
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includeCharts,
                        onCheckedChange = { includeCharts = it }
                    )
                    Text(
                        "Include charts",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includeRawData,
                        onCheckedChange = { includeRawData = it }
                    )
                    Text(
                        "Include raw data",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onExport(
                        ExportOptions(
                            format = selectedFormat,
                            includeCharts = includeCharts,
                            includeRawData = includeRawData,
                            timeRange = TimeRange.ALL_TIME,
                            categories = null
                        )
                    )
                }
            ) {
                Text("Export")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}