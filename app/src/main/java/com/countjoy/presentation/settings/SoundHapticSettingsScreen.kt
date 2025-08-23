package com.countjoy.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.R
import com.countjoy.service.SoundHapticService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundHapticSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SoundHapticSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sound & Haptics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Sound Settings Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sound Settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Sound Enable Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Enable Sounds")
                            Text(
                                text = "Play sounds for notifications and interactions",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.soundEnabled,
                            onCheckedChange = { viewModel.setSoundEnabled(it) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Sound Volume Slider
                    Text("Sound Volume")
                    Slider(
                        value = uiState.soundVolume,
                        onValueChange = { viewModel.setSoundVolume(it) },
                        enabled = uiState.soundEnabled,
                        valueRange = 0f..1f
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Test Sound Button
                    Button(
                        onClick = { viewModel.testSound() },
                        enabled = uiState.soundEnabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Test Notification Sound")
                    }
                }
            }
            
            // Haptic Settings Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Haptic Feedback",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Haptic Enable Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Enable Haptic Feedback")
                            Text(
                                text = "Vibrate for notifications and interactions",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.hapticEnabled,
                            onCheckedChange = { viewModel.setHapticEnabled(it) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Haptic Intensity Slider
                    Text("Haptic Intensity")
                    Slider(
                        value = uiState.hapticIntensity.toFloat(),
                        onValueChange = { viewModel.setHapticIntensity(it.toInt()) },
                        enabled = uiState.hapticEnabled,
                        valueRange = 1f..255f,
                        steps = 4
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Test Haptic Patterns
                    Text("Test Haptic Patterns", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.testHaptic(SoundHapticService.HapticPattern.LIGHT_TAP) },
                            enabled = uiState.hapticEnabled,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Light", style = MaterialTheme.typography.labelSmall)
                        }
                        OutlinedButton(
                            onClick = { viewModel.testHaptic(SoundHapticService.HapticPattern.DOUBLE_TAP) },
                            enabled = uiState.hapticEnabled,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Double", style = MaterialTheme.typography.labelSmall)
                        }
                        OutlinedButton(
                            onClick = { viewModel.testHaptic(SoundHapticService.HapticPattern.SUCCESS_PATTERN) },
                            enabled = uiState.hapticEnabled,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Success", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
            
            // Event Feedback Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Event Feedback",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Milestone Notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Milestone Notifications")
                            Text(
                                text = "Alert at 75%, 50%, 25% completion",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.milestoneNotifications,
                            onCheckedChange = { viewModel.setMilestoneNotifications(it) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Completion Celebration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Completion Celebration")
                            Text(
                                text = "Special effects when countdown completes",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.completionCelebration,
                            onCheckedChange = { viewModel.setCompletionCelebration(it) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Button Click Feedback
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Button Click Feedback")
                            Text(
                                text = "Subtle feedback for button presses",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.buttonClickFeedback,
                            onCheckedChange = { viewModel.setButtonClickFeedback(it) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}