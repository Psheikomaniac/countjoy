package com.countjoy.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.countjoy.domain.model.EventCategory
import com.countjoy.domain.model.EventPriority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFilterChips(
    selectedCategory: EventCategory?,
    selectedPriority: EventPriority?,
    showPastEvents: Boolean,
    onCategorySelected: (EventCategory?) -> Unit,
    onPrioritySelected: (EventPriority?) -> Unit,
    onShowPastEventsToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category filter chip
        FilterChip(
            selected = selectedCategory != null,
            onClick = { 
                if (selectedCategory != null) {
                    onCategorySelected(null)
                }
            },
            label = { 
                Text(selectedCategory?.displayName ?: "All Categories") 
            },
            leadingIcon = if (selectedCategory != null) {
                {
                    Icon(
                        imageVector = getCategoryFilterIcon(selectedCategory),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else null,
            trailingIcon = if (selectedCategory != null) {
                {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear category filter",
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else null
        )
        
        // Priority filter chip
        FilterChip(
            selected = selectedPriority != null,
            onClick = { 
                if (selectedPriority != null) {
                    onPrioritySelected(null)
                }
            },
            label = { 
                Text(selectedPriority?.displayName ?: "All Priorities") 
            },
            leadingIcon = if (selectedPriority != null) {
                {
                    Icon(
                        imageVector = getPriorityFilterIcon(selectedPriority),
                        contentDescription = null,
                        tint = Color(selectedPriority.color),
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else null,
            trailingIcon = if (selectedPriority != null) {
                {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear priority filter",
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else null
        )
        
        // Past events filter chip
        FilterChip(
            selected = showPastEvents,
            onClick = onShowPastEventsToggle,
            label = { Text("Past Events") },
            leadingIcon = {
                Icon(
                    imageVector = if (showPastEvents) Icons.Default.CheckCircle else Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterDropdown(
    selectedCategory: EventCategory?,
    onCategorySelected: (EventCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCategory?.displayName ?: "All Categories",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("All Categories") },
                onClick = {
                    onCategorySelected(null)
                    expanded = false
                }
            )
            
            Divider()
            
            EventCategory.values().forEach { category ->
                DropdownMenuItem(
                    text = { 
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = getCategoryFilterIcon(category),
                                contentDescription = null,
                                tint = Color(category.defaultColor),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(category.displayName)
                        }
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityFilterDropdown(
    selectedPriority: EventPriority?,
    onPrioritySelected: (EventPriority?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedPriority?.displayName ?: "All Priorities",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("All Priorities") },
                onClick = {
                    onPrioritySelected(null)
                    expanded = false
                }
            )
            
            Divider()
            
            EventPriority.values().forEach { priority ->
                DropdownMenuItem(
                    text = { 
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = getPriorityFilterIcon(priority),
                                contentDescription = null,
                                tint = Color(priority.color),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(priority.displayName)
                        }
                    },
                    onClick = {
                        onPrioritySelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Helper functions for icons - using only available Material Icons
private fun getCategoryFilterIcon(category: EventCategory): androidx.compose.ui.graphics.vector.ImageVector {
    return when (category.icon) {
        "cake" -> Icons.Default.Favorite
        "favorite" -> Icons.Default.Favorite
        "beach_access" -> Icons.Default.Place
        "schedule" -> Icons.Default.DateRange
        "groups", "people" -> Icons.Default.Person
        "flight" -> Icons.Default.Send
        "school" -> Icons.Default.Edit
        "health_and_safety", "local_hospital" -> Icons.Default.Favorite
        "sports", "sports_soccer" -> Icons.Default.Star
        "movie", "movie_filter" -> Icons.Default.PlayArrow
        "work" -> Icons.Default.Home
        "person" -> Icons.Default.Person
        "attach_money" -> Icons.Default.ShoppingCart
        "star" -> Icons.Default.Star
        "create" -> Icons.Default.Create
        else -> Icons.Default.DateRange
    }
}

private fun getPriorityFilterIcon(priority: EventPriority): androidx.compose.ui.graphics.vector.ImageVector {
    return when (priority) {
        EventPriority.LOW -> Icons.Default.KeyboardArrowDown
        EventPriority.MEDIUM -> Icons.Default.Menu
        EventPriority.HIGH -> Icons.Default.Warning
    }
}