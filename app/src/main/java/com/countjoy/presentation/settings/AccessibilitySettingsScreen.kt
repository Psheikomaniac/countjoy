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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.R
import com.countjoy.core.accessibility.AccessibilityManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilitySettingsScreen(
    onNavigateBack: () -> Unit,
    onRestartRequired: () -> Unit,
    viewModel: AccessibilitySettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.accessibility),
                        modifier = Modifier.semantics {
                            contentDescription = "Accessibility Settings"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.semantics {
                            contentDescription = "Navigate back"
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
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
                .padding(16.dp)
                .semantics {
                    contentDescription = "Accessibility settings list"
                },
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Visual Settings Section
            SettingsSection(
                title = "Visual",
                content = {
                    // Font Size
                    FontSizeSetting(
                        currentSize = settings.fontSize,
                        onSizeChanged = { size ->
                            viewModel.setFontSize(size)
                            onRestartRequired()
                        }
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // High Contrast
                    SwitchSetting(
                        title = stringResource(R.string.high_contrast),
                        description = "Increase color contrast for better visibility",
                        checked = settings.isHighContrastEnabled,
                        onCheckedChange = { 
                            viewModel.setHighContrastEnabled(it)
                            onRestartRequired()
                        }
                    )
                    
                    // Bold Text
                    SwitchSetting(
                        title = "Bold Text",
                        description = "Make all text bold for better readability",
                        checked = settings.isBoldTextEnabled,
                        onCheckedChange = { 
                            viewModel.setBoldTextEnabled(it)
                            onRestartRequired()
                        }
                    )
                    
                    // Color Blind Mode
                    ColorBlindModeSetting(
                        currentMode = settings.colorBlindMode,
                        onModeChanged = { mode ->
                            viewModel.setColorBlindMode(mode)
                            onRestartRequired()
                        }
                    )
                }
            )
            
            // Motor Settings Section
            SettingsSection(
                title = "Motor",
                content = {
                    // Touch Target Size
                    TouchTargetSizeSetting(
                        currentSize = settings.touchTargetSize,
                        onSizeChanged = { size ->
                            viewModel.setTouchTargetSize(size)
                        }
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Touch Hold Delay
                    TouchHoldDelaySetting(
                        currentDelay = settings.touchHoldDelay,
                        onDelayChanged = { delay ->
                            viewModel.setTouchHoldDelay(delay)
                        }
                    )
                }
            )
            
            // Audio & Voice Section
            SettingsSection(
                title = "Audio & Voice",
                content = {
                    // Screen Reader Status
                    InfoSetting(
                        title = "Screen Reader",
                        value = if (settings.isScreenReaderEnabled) "Active" else "Inactive",
                        description = "TalkBack status (system setting)"
                    )
                    
                    // Voice Control
                    SwitchSetting(
                        title = "Voice Control",
                        description = "Control the app using voice commands",
                        checked = settings.isVoiceControlEnabled,
                        onCheckedChange = { 
                            viewModel.setVoiceControlEnabled(it)
                        }
                    )
                }
            )
            
            // Interaction Section
            SettingsSection(
                title = "Interaction",
                content = {
                    // Reduce Motion
                    SwitchSetting(
                        title = stringResource(R.string.reduce_motion),
                        description = "Minimize animations and transitions",
                        checked = settings.isReduceMotionEnabled,
                        onCheckedChange = { 
                            viewModel.setReduceMotionEnabled(it)
                        }
                    )
                    
                    // Simple Mode
                    SwitchSetting(
                        title = "Simple Mode",
                        description = "Show only essential features",
                        checked = settings.isSimpleModeEnabled,
                        onCheckedChange = { 
                            viewModel.setSimpleModeEnabled(it)
                            onRestartRequired()
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "$title settings section"
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FontSizeSetting(
    currentSize: AccessibilityManager.Companion.FontSize,
    onSizeChanged: (AccessibilityManager.Companion.FontSize) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.font_size),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AccessibilityManager.Companion.FontSize.values().forEach { size ->
                FilterChip(
                    selected = currentSize == size,
                    onClick = { onSizeChanged(size) },
                    label = { 
                        Text(
                            when(size) {
                                AccessibilityManager.Companion.FontSize.EXTRA_SMALL -> "XS"
                                AccessibilityManager.Companion.FontSize.SMALL -> "S"
                                AccessibilityManager.Companion.FontSize.MEDIUM -> "M"
                                AccessibilityManager.Companion.FontSize.LARGE -> "L"
                                AccessibilityManager.Companion.FontSize.EXTRA_LARGE -> "XL"
                            }
                        )
                    },
                    modifier = Modifier.semantics {
                        contentDescription = when(size) {
                            AccessibilityManager.Companion.FontSize.EXTRA_SMALL -> "Extra small font size"
                            AccessibilityManager.Companion.FontSize.SMALL -> "Small font size"
                            AccessibilityManager.Companion.FontSize.MEDIUM -> "Medium font size"
                            AccessibilityManager.Companion.FontSize.LARGE -> "Large font size"
                            AccessibilityManager.Companion.FontSize.EXTRA_LARGE -> "Extra large font size"
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorBlindModeSetting(
    currentMode: AccessibilityManager.Companion.ColorBlindMode,
    onModeChanged: (AccessibilityManager.Companion.ColorBlindMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(
            text = "Color Blind Mode",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = when(currentMode) {
                    AccessibilityManager.Companion.ColorBlindMode.NONE -> "None"
                    AccessibilityManager.Companion.ColorBlindMode.PROTANOPIA -> "Protanopia (Red-blind)"
                    AccessibilityManager.Companion.ColorBlindMode.DEUTERANOPIA -> "Deuteranopia (Green-blind)"
                    AccessibilityManager.Companion.ColorBlindMode.TRITANOPIA -> "Tritanopia (Blue-blind)"
                    AccessibilityManager.Companion.ColorBlindMode.ACHROMATOPSIA -> "Achromatopsia (Total)"
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .semantics {
                        contentDescription = "Color blind mode selector, current mode: $currentMode"
                    }
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                AccessibilityManager.Companion.ColorBlindMode.values().forEach { mode ->
                    DropdownMenuItem(
                        text = { 
                            Text(
                                when(mode) {
                                    AccessibilityManager.Companion.ColorBlindMode.NONE -> "None"
                                    AccessibilityManager.Companion.ColorBlindMode.PROTANOPIA -> "Protanopia (Red-blind)"
                                    AccessibilityManager.Companion.ColorBlindMode.DEUTERANOPIA -> "Deuteranopia (Green-blind)"
                                    AccessibilityManager.Companion.ColorBlindMode.TRITANOPIA -> "Tritanopia (Blue-blind)"
                                    AccessibilityManager.Companion.ColorBlindMode.ACHROMATOPSIA -> "Achromatopsia (Total)"
                                }
                            )
                        },
                        onClick = {
                            onModeChanged(mode)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TouchTargetSizeSetting(
    currentSize: AccessibilityManager.Companion.TouchTargetSize,
    onSizeChanged: (AccessibilityManager.Companion.TouchTargetSize) -> Unit
) {
    Column {
        Text(
            text = "Touch Target Size",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AccessibilityManager.Companion.TouchTargetSize.values().forEach { size ->
                FilterChip(
                    selected = currentSize == size,
                    onClick = { onSizeChanged(size) },
                    label = { 
                        Text(
                            when(size) {
                                AccessibilityManager.Companion.TouchTargetSize.SMALL -> "Small"
                                AccessibilityManager.Companion.TouchTargetSize.MEDIUM -> "Medium"
                                AccessibilityManager.Companion.TouchTargetSize.LARGE -> "Large"
                            }
                        )
                    },
                    modifier = Modifier.semantics {
                        contentDescription = when(size) {
                            AccessibilityManager.Companion.TouchTargetSize.SMALL -> "Small touch targets"
                            AccessibilityManager.Companion.TouchTargetSize.MEDIUM -> "Medium touch targets"
                            AccessibilityManager.Companion.TouchTargetSize.LARGE -> "Large touch targets"
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TouchHoldDelaySetting(
    currentDelay: AccessibilityManager.Companion.TouchHoldDelay,
    onDelayChanged: (AccessibilityManager.Companion.TouchHoldDelay) -> Unit
) {
    Column {
        Text(
            text = "Touch & Hold Delay",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AccessibilityManager.Companion.TouchHoldDelay.values().forEach { delay ->
                FilterChip(
                    selected = currentDelay == delay,
                    onClick = { onDelayChanged(delay) },
                    label = { 
                        Text(
                            when(delay) {
                                AccessibilityManager.Companion.TouchHoldDelay.SHORT -> "Short"
                                AccessibilityManager.Companion.TouchHoldDelay.MEDIUM -> "Medium"
                                AccessibilityManager.Companion.TouchHoldDelay.LONG -> "Long"
                            }
                        )
                    },
                    modifier = Modifier.semantics {
                        contentDescription = when(delay) {
                            AccessibilityManager.Companion.TouchHoldDelay.SHORT -> "Short touch hold delay"
                            AccessibilityManager.Companion.TouchHoldDelay.MEDIUM -> "Medium touch hold delay"
                            AccessibilityManager.Companion.TouchHoldDelay.LONG -> "Long touch hold delay"
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SwitchSetting(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .semantics {
                contentDescription = "$title. $description. Currently ${if (checked) "enabled" else "disabled"}"
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun InfoSetting(
    title: String,
    value: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .semantics {
                contentDescription = "$title. $description. Current value: $value"
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}