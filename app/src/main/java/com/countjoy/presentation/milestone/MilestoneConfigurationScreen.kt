package com.countjoy.presentation.milestone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.R
import com.countjoy.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneConfigurationScreen(
    eventId: String,
    onNavigateBack: () -> Unit,
    viewModel: MilestoneViewModel = hiltViewModel()
) {
    var showPercentageTemplates by remember { mutableStateOf(true) }
    var showTimeTemplates by remember { mutableStateOf(false) }
    var selectedMilestones by remember { mutableStateOf<Set<MilestoneTemplate>>(emptySet()) }
    var showCustomMilestoneDialog by remember { mutableStateOf(false) }
    var customMilestones by remember { mutableStateOf<List<Milestone>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configure Milestones") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.createMilestones(
                                eventId = eventId,
                                usePercentageTemplates = showPercentageTemplates && selectedMilestones.any { it.type == MilestoneType.PERCENTAGE_BASED },
                                useTimeTemplates = showTimeTemplates && selectedMilestones.any { it.type == MilestoneType.TIME_BASED },
                                customMilestones = customMilestones
                            )
                            onNavigateBack()
                        }
                    ) {
                        Text("Save")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCustomMilestoneDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Custom") },
                text = { Text("Custom Milestone") }
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
            // Template Selection Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Milestone Templates",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = showPercentageTemplates,
                                onClick = { showPercentageTemplates = !showPercentageTemplates },
                                label = { Text("Percentage-based") },
                                leadingIcon = if (showPercentageTemplates) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                            
                            FilterChip(
                                selected = showTimeTemplates,
                                onClick = { showTimeTemplates = !showTimeTemplates },
                                label = { Text("Time-based") },
                                leadingIcon = if (showTimeTemplates) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null
                            )
                        }
                    }
                }
            }
            
            // Percentage Templates
            if (showPercentageTemplates) {
                item {
                    MilestoneCategoryHeader(
                        title = "Percentage Milestones",
                        icon = Icons.Default.Check
                    )
                }
                
                items(MilestoneTemplates.percentageTemplates) { template ->
                    MilestoneTemplateCard(
                        template = template,
                        isSelected = selectedMilestones.contains(template),
                        onClick = {
                            selectedMilestones = if (selectedMilestones.contains(template)) {
                                selectedMilestones - template
                            } else {
                                selectedMilestones + template
                            }
                        }
                    )
                }
            }
            
            // Time Templates
            if (showTimeTemplates) {
                item {
                    MilestoneCategoryHeader(
                        title = "Time-based Milestones",
                        icon = Icons.Default.DateRange
                    )
                }
                
                items(MilestoneTemplates.timeTemplates) { template ->
                    MilestoneTemplateCard(
                        template = template,
                        isSelected = selectedMilestones.contains(template),
                        onClick = {
                            selectedMilestones = if (selectedMilestones.contains(template)) {
                                selectedMilestones - template
                            } else {
                                selectedMilestones + template
                            }
                        }
                    )
                }
            }
            
            // Custom Milestones
            if (customMilestones.isNotEmpty()) {
                item {
                    MilestoneCategoryHeader(
                        title = "Custom Milestones",
                        icon = Icons.Default.Star
                    )
                }
                
                items(customMilestones) { milestone ->
                    CustomMilestoneCard(
                        milestone = milestone,
                        onDelete = {
                            customMilestones = customMilestones - milestone
                        }
                    )
                }
            }
        }
    }
    
    // Custom Milestone Dialog
    if (showCustomMilestoneDialog) {
        CustomMilestoneDialog(
            onDismiss = { showCustomMilestoneDialog = false },
            onConfirm = { milestone ->
                customMilestones = customMilestones + milestone
                showCustomMilestoneDialog = false
            },
            eventId = eventId
        )
    }
}

@Composable
private fun MilestoneCategoryHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MilestoneTemplateCard(
    template: MilestoneTemplate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = template.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = template.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = when (template.type) {
                            MilestoneType.PERCENTAGE_BASED -> "${template.value.toInt()}%"
                            MilestoneType.TIME_BASED -> "${template.value.toInt()} days"
                            else -> ""
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() }
                )
            }
        }
    }
}

@Composable
private fun CustomMilestoneCard(
    milestone: Milestone,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = milestone.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = milestone.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomMilestoneDialog(
    onDismiss: () -> Unit,
    onConfirm: (Milestone) -> Unit,
    eventId: String
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(MilestoneType.PERCENTAGE_BASED) }
    var value by remember { mutableStateOf("") }
    var enableNotification by remember { mutableStateOf(true) }
    var celebrationEffect by remember { mutableStateOf(CelebrationEffect.CONFETTI) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Custom Milestone") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = { },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = type.name.replace("_", " "),
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Type") },
                            modifier = Modifier.menuAnchor()
                        )
                    }
                    
                    OutlinedTextField(
                        value = value,
                        onValueChange = { value = it },
                        label = { Text("Value") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Notification", modifier = Modifier.weight(1f))
                    Switch(
                        checked = enableNotification,
                        onCheckedChange = { enableNotification = it }
                    )
                }
                
                ExposedDropdownMenuBox(
                    expanded = false,
                    onExpandedChange = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = celebrationEffect.name,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Celebration Effect") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val milestone = Milestone(
                        eventId = eventId,
                        type = type,
                        value = value.toFloatOrNull() ?: 0f,
                        title = title,
                        message = message,
                        isNotificationEnabled = enableNotification,
                        celebrationEffect = celebrationEffect
                    )
                    onConfirm(milestone)
                },
                enabled = title.isNotBlank() && message.isNotBlank() && value.toFloatOrNull() != null
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}