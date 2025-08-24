package com.countjoy.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.R
import com.countjoy.domain.model.NotificationChannelType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Notification settings screen for Issue #46
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: NotificationSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notification Channels Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Notification Channels",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    NotificationChannelItem(
                        title = "Urgent Events",
                        description = "High priority events that always notify",
                        icon = Icons.Default.Warning,
                        isEnabled = uiState.urgentChannelEnabled,
                        onToggle = { viewModel.toggleChannel(NotificationChannelType.URGENT, it) }
                    )
                    
                    NotificationChannelItem(
                        title = "Milestones",
                        description = "Standard milestone achievements",
                        icon = Icons.Default.Star,
                        isEnabled = uiState.milestonesChannelEnabled,
                        onToggle = { viewModel.toggleChannel(NotificationChannelType.MILESTONES, it) }
                    )
                    
                    NotificationChannelItem(
                        title = "Reminders",
                        description = "Custom user reminders",
                        icon = Icons.Default.Notifications,
                        isEnabled = uiState.remindersChannelEnabled,
                        onToggle = { viewModel.toggleChannel(NotificationChannelType.REMINDERS, it) }
                    )
                    
                    NotificationChannelItem(
                        title = "Daily Summary",
                        description = "Daily event summaries",
                        icon = Icons.Default.DateRange,
                        isEnabled = uiState.summaryChannelEnabled,
                        onToggle = { viewModel.toggleChannel(NotificationChannelType.SUMMARY, it) }
                    )
                }
            }
            
            // Smart Features Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Smart Features",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("AI-Suggested Reminders")
                            Text(
                                "Intelligent reminder scheduling",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.smartRemindersEnabled,
                            onCheckedChange = { viewModel.toggleSmartReminders(it) }
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Adaptive Scheduling")
                            Text(
                                "Learn from your interaction patterns",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.adaptiveSchedulingEnabled,
                            onCheckedChange = { viewModel.toggleAdaptiveScheduling(it) }
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Bundle Notifications")
                            Text(
                                "Group multiple notifications",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.bundleNotificationsEnabled,
                            onCheckedChange = { viewModel.toggleBundleNotifications(it) }
                        )
                    }
                }
            }
            
            // Quiet Hours Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quiet Hours",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Switch(
                            checked = uiState.quietHoursEnabled,
                            onCheckedChange = { viewModel.toggleQuietHours(it) }
                        )
                    }
                    
                    if (uiState.quietHoursEnabled) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedCard(
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.showStartTimePicker() }
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Start", style = MaterialTheme.typography.labelMedium)
                                    Text(
                                        uiState.quietHoursStart?.format(timeFormatter) ?: "22:00",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                            
                            OutlinedCard(
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.showEndTimePicker() }
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("End", style = MaterialTheme.typography.labelMedium)
                                    Text(
                                        uiState.quietHoursEnd?.format(timeFormatter) ?: "07:00",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Override for Urgent", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "Allow urgent notifications during quiet hours",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = uiState.overrideQuietHoursForUrgent,
                                onCheckedChange = { viewModel.toggleOverrideQuietHours(it) }
                            )
                        }
                    }
                }
            }
            
            // Notification Actions Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Enable Quick Actions")
                        Switch(
                            checked = uiState.quickActionsEnabled,
                            onCheckedChange = { viewModel.toggleQuickActions(it) }
                        )
                    }
                    
                    if (uiState.quickActionsEnabled) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.snoozeEnabled,
                                onClick = { viewModel.toggleSnooze(!uiState.snoozeEnabled) },
                                label = { Text("Snooze") },
                                leadingIcon = {
                                    if (uiState.snoozeEnabled) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                            
                            FilterChip(
                                selected = uiState.shareEnabled,
                                onClick = { viewModel.toggleShare(!uiState.shareEnabled) },
                                label = { Text("Share") },
                                leadingIcon = {
                                    if (uiState.shareEnabled) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                            
                            FilterChip(
                                selected = uiState.quickViewEnabled,
                                onClick = { viewModel.toggleQuickView(!uiState.quickViewEnabled) },
                                label = { Text("Quick View") },
                                leadingIcon = {
                                    if (uiState.quickViewEnabled) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                        }
                        
                        // Snooze Options
                        if (uiState.snoozeEnabled) {
                            Text(
                                "Snooze Options",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("1h", "3h", "1d").forEach { option ->
                                    FilterChip(
                                        selected = option in uiState.selectedSnoozeOptions,
                                        onClick = { viewModel.toggleSnoozeOption(option) },
                                        label = { Text(option) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Test Notification Button
            Button(
                onClick = { viewModel.sendTestNotification() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send Test Notification")
            }
        }
    }
}

@Composable
private fun NotificationChannelItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isEnabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle
        )
    }
}