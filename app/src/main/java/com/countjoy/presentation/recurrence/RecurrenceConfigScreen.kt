package com.countjoy.presentation.recurrence

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.countjoy.domain.model.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceConfigScreen(
    eventId: String,
    onSave: (RecurrenceRule?) -> Unit,
    onBack: () -> Unit
) {
    var isRecurring by remember { mutableStateOf(false) }
    var selectedPattern by remember { mutableStateOf(RecurrencePattern.DAILY) }
    var interval by remember { mutableStateOf("1") }
    var selectedDaysOfWeek by remember { mutableStateOf(setOf<DayOfWeek>()) }
    var endType by remember { mutableStateOf(RecurrenceEndType.NEVER) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var occurrenceCount by remember { mutableStateOf("10") }
    var skipWeekends by remember { mutableStateOf(false) }
    var skipHolidays by remember { mutableStateOf(false) }
    var selectedTemplate by remember { mutableStateOf<RecurrenceTemplate?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recurrence Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (isRecurring) {
                                val rule = RecurrenceRule(
                                    eventId = eventId,
                                    pattern = selectedPattern,
                                    interval = interval.toIntOrNull() ?: 1,
                                    daysOfWeek = if (selectedPattern == RecurrencePattern.WEEKLY) selectedDaysOfWeek else emptySet(),
                                    endType = endType,
                                    endDate = if (endType == RecurrenceEndType.BY_DATE) endDate else null,
                                    occurrenceCount = if (endType == RecurrenceEndType.AFTER_OCCURRENCES) occurrenceCount.toIntOrNull() else null,
                                    skipWeekends = skipWeekends,
                                    skipHolidays = skipHolidays
                                )
                                onSave(rule)
                            } else {
                                onSave(null)
                            }
                        }
                    ) {
                        Text("Save")
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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Enable Recurrence",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = isRecurring,
                            onCheckedChange = { isRecurring = it }
                        )
                    }
                }
            }
            
            if (isRecurring) {
                item {
                    Text(
                        "Quick Templates",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                items(RecurrenceTemplates.templates) { template ->
                    RecurrenceTemplateCard(
                        template = template,
                        isSelected = selectedTemplate == template,
                        onClick = {
                            selectedTemplate = template
                            selectedPattern = template.rule.pattern
                            interval = template.rule.interval.toString()
                            selectedDaysOfWeek = template.rule.daysOfWeek
                        }
                    )
                }
                
                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
                
                item {
                    Text(
                        "Custom Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                item {
                    RecurrencePatternSelector(
                        selectedPattern = selectedPattern,
                        onPatternSelected = { selectedPattern = it }
                    )
                }
                
                item {
                    OutlinedTextField(
                        value = interval,
                        onValueChange = { interval = it },
                        label = {
                            Text(
                                when (selectedPattern) {
                                    RecurrencePattern.DAILY -> "Every X days"
                                    RecurrencePattern.WEEKLY -> "Every X weeks"
                                    RecurrencePattern.MONTHLY_BY_DATE,
                                    RecurrencePattern.MONTHLY_BY_DAY -> "Every X months"
                                    RecurrencePattern.YEARLY -> "Every X years"
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                if (selectedPattern == RecurrencePattern.WEEKLY) {
                    item {
                        DayOfWeekSelector(
                            selectedDays = selectedDaysOfWeek,
                            onDaysSelected = { selectedDaysOfWeek = it }
                        )
                    }
                }
                
                item {
                    RecurrenceEndTypeSelector(
                        endType = endType,
                        endDate = endDate,
                        occurrenceCount = occurrenceCount,
                        onEndTypeChanged = { endType = it },
                        onEndDateChanged = { endDate = it },
                        onOccurrenceCountChanged = { occurrenceCount = it }
                    )
                }
                
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Advanced Options",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Skip weekends")
                                Switch(
                                    checked = skipWeekends,
                                    onCheckedChange = { skipWeekends = it }
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Skip holidays")
                                Switch(
                                    checked = skipHolidays,
                                    onCheckedChange = { skipHolidays = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecurrenceTemplateCard(
    template: RecurrenceTemplate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    template.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    template.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrencePatternSelector(
    selectedPattern: RecurrencePattern,
    onPatternSelected: (RecurrencePattern) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = when (selectedPattern) {
                RecurrencePattern.DAILY -> "Daily"
                RecurrencePattern.WEEKLY -> "Weekly"
                RecurrencePattern.MONTHLY_BY_DATE -> "Monthly (by date)"
                RecurrencePattern.MONTHLY_BY_DAY -> "Monthly (by day)"
                RecurrencePattern.YEARLY -> "Yearly"
            },
            onValueChange = { },
            readOnly = true,
            label = { Text("Recurrence Pattern") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            RecurrencePattern.values().forEach { pattern ->
                DropdownMenuItem(
                    text = {
                        Text(
                            when (pattern) {
                                RecurrencePattern.DAILY -> "Daily"
                                RecurrencePattern.WEEKLY -> "Weekly"
                                RecurrencePattern.MONTHLY_BY_DATE -> "Monthly (by date)"
                                RecurrencePattern.MONTHLY_BY_DAY -> "Monthly (by day)"
                                RecurrencePattern.YEARLY -> "Yearly"
                            }
                        )
                    },
                    onClick = {
                        onPatternSelected(pattern)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DayOfWeekSelector(
    selectedDays: Set<DayOfWeek>,
    onDaysSelected: (Set<DayOfWeek>) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Select Days",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DayOfWeek.values().forEach { day ->
                    FilterChip(
                        selected = day in selectedDays,
                        onClick = {
                            onDaysSelected(
                                if (day in selectedDays) {
                                    selectedDays - day
                                } else {
                                    selectedDays + day
                                }
                            )
                        },
                        label = {
                            Text(
                                day.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceEndTypeSelector(
    endType: RecurrenceEndType,
    endDate: LocalDate?,
    occurrenceCount: String,
    onEndTypeChanged: (RecurrenceEndType) -> Unit,
    onEndDateChanged: (LocalDate?) -> Unit,
    onOccurrenceCountChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "End Condition",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            
            RecurrenceEndType.values().forEach { type ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = endType == type,
                        onClick = { onEndTypeChanged(type) }
                    )
                    Text(
                        when (type) {
                            RecurrenceEndType.NEVER -> "Never ends"
                            RecurrenceEndType.BY_DATE -> "End by date"
                            RecurrenceEndType.AFTER_OCCURRENCES -> "End after occurrences"
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            
            when (endType) {
                RecurrenceEndType.BY_DATE -> {
                    OutlinedTextField(
                        value = endDate?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
                        onValueChange = { /* Date picker would go here */ },
                        label = { Text("End Date") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                RecurrenceEndType.AFTER_OCCURRENCES -> {
                    OutlinedTextField(
                        value = occurrenceCount,
                        onValueChange = onOccurrenceCountChanged,
                        label = { Text("Number of Occurrences") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                else -> { /* No additional input needed */ }
            }
        }
    }
}