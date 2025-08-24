package com.countjoy.presentation.eventlist

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import com.countjoy.R
import com.countjoy.domain.model.CountdownEvent
import com.countjoy.domain.model.EventCategory
import com.countjoy.domain.model.EventPriority
import com.countjoy.presentation.components.SearchBar
import com.countjoy.presentation.components.EventFilterChips
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    onNavigateToEventDetail: (Long) -> Unit,
    onNavigateToCreateEvent: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: EventListViewModel = hiltViewModel()
) {
    val events by viewModel.filteredEvents.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedPriority by viewModel.selectedPriority.collectAsStateWithLifecycle()
    val sortBy by viewModel.sortBy.collectAsStateWithLifecycle()
    val showPastEvents by viewModel.showPastEvents.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Events") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Sort")
                    }
                    IconButton(onClick = { showFilterDialog = true }) {
                        BadgedBox(
                            badge = {
                                if (selectedCategory != null || selectedPriority != null || showPastEvents) {
                                    Badge { Text(text = "•") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.List, contentDescription = "Filter")
                        }
                    }
                    
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = { 
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(option.displayName)
                                        if (sortBy == option) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    viewModel.onSortOptionChanged(option)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCreateEvent,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Event") },
                text = { Text("New Event") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                placeholder = "Search events...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Event list
            if (events.isEmpty()) {
                EmptyStateView(
                    modifier = Modifier.fillMaxSize(),
                    message = if (searchQuery.isNotBlank() || selectedCategory != null || selectedPriority != null) {
                        "No events found matching your filters"
                    } else {
                        "No events yet. Create your first countdown!"
                    },
                    onCreateEvent = onNavigateToCreateEvent
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = events,
                        key = { event -> event.id }
                    ) { event ->
                        EventListItem(
                            event = event,
                            onClick = { onNavigateToEventDetail(event.id) },
                            onDelete = { viewModel.deleteEvent(event) },
                            onDuplicate = { viewModel.duplicateEvent(event) },
                            onPriorityChange = { priority ->
                                viewModel.updateEventPriority(event, priority)
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            selectedCategory = selectedCategory,
            selectedPriority = selectedPriority,
            showPastEvents = showPastEvents,
            onCategorySelected = viewModel::onCategorySelected,
            onPrioritySelected = viewModel::onPrioritySelected,
            onShowPastEventsToggle = viewModel::toggleShowPastEvents,
            onDismiss = { showFilterDialog = false }
        )
    }
    
    // Error/Success Messages
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar or toast
            viewModel.clearMessages()
        }
    }
    
    uiState.successMessage?.let { message ->
        LaunchedEffect(message) {
            // Show snackbar or toast
            viewModel.clearMessages()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListItem(
    event: CountdownEvent,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    onPriorityChange: (EventPriority) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    val category = EventCategory.fromName(event.category)
    val priority = EventPriority.fromValue(event.priority)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (event.hasExpired()) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(category.defaultColor).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(category),
                        contentDescription = null,
                        tint = Color(category.defaultColor),
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        // Priority indicator
                        if (event.priority > 0) {
                            Icon(
                                imageVector = getPriorityIcon(priority),
                                contentDescription = null,
                                tint = Color(priority.color),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Text(
                        text = event.getFormattedCountdown(includeSeconds = false),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (event.hasExpired()) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                    
                    Text(
                        text = event.targetDateTime.format(
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onClick()
                        }
                    )
                    
                    DropdownMenuItem(
                        text = { Text("Duplicate") },
                        leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onDuplicate()
                        }
                    )
                    
                    Divider()
                    
                    // Priority submenu
                    EventPriority.values().forEach { priorityOption ->
                        DropdownMenuItem(
                            text = { 
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = getPriorityIcon(priorityOption),
                                        contentDescription = null,
                                        tint = Color(priorityOption.color),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text("${priorityOption.displayName} Priority")
                                    if (priority == priorityOption) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                showMenu = false
                                onPriorityChange(priorityOption)
                            }
                        )
                    }
                    
                    Divider()
                    
                    DropdownMenuItem(
                        text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { 
                            Icon(
                                Icons.Default.Delete, 
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            ) 
                        },
                        onClick = {
                            showMenu = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(
    message: String,
    onCreateEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onCreateEvent) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create Event")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    selectedCategory: EventCategory?,
    selectedPriority: EventPriority?,
    showPastEvents: Boolean,
    onCategorySelected: (EventCategory?) -> Unit,
    onPrioritySelected: (EventPriority?) -> Unit,
    onShowPastEventsToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Events") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category filter
                Column {
                    Text(
                        "Category",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { onCategorySelected(null) },
                            label = { Text("All") }
                        )
                        EventCategory.values().forEach { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { 
                                    onCategorySelected(
                                        if (selectedCategory == category) null else category
                                    )
                                },
                                label = { Text(category.displayName) }
                            )
                        }
                    }
                }
                
                // Priority filter
                Column {
                    Text(
                        "Priority",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedPriority == null,
                            onClick = { onPrioritySelected(null) },
                            label = { Text("All") }
                        )
                        EventPriority.values().forEach { priority ->
                            FilterChip(
                                selected = selectedPriority == priority,
                                onClick = { 
                                    onPrioritySelected(
                                        if (selectedPriority == priority) null else priority
                                    )
                                },
                                label = { Text(priority.displayName) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = getPriorityIcon(priority),
                                        contentDescription = null,
                                        tint = Color(priority.color),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                }
                
                // Show past events toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show past events")
                    Switch(
                        checked = showPastEvents,
                        onCheckedChange = { onShowPastEventsToggle() }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onCategorySelected(null)
                    onPrioritySelected(null)
                    if (showPastEvents) onShowPastEventsToggle()
                    onDismiss()
                }
            ) {
                Text("Clear All")
            }
        }
    )
}

// Helper functions for icons - using only available Material Icons
fun getCategoryIcon(category: EventCategory): ImageVector {
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

fun getPriorityIcon(priority: EventPriority): ImageVector {
    return when (priority) {
        EventPriority.LOW -> Icons.Default.KeyboardArrowDown
        EventPriority.MEDIUM -> Icons.Default.Menu  
        EventPriority.HIGH -> Icons.Default.Warning
    }
}

// Extension to create FlowRow-like layout
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val rows = mutableListOf<List<Placeable>>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0
        
        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints)
            if (currentRowWidth + placeable.width <= constraints.maxWidth) {
                currentRow.add(placeable)
                currentRowWidth += placeable.width
            } else {
                rows.add(currentRow)
                currentRow = mutableListOf(placeable)
                currentRowWidth = placeable.width
            }
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }
        
        val height = rows.sumOf { row -> row.maxOf { it.height } }
        
        layout(constraints.maxWidth, height) {
            var y = 0
            rows.forEach { row ->
                var x = 0
                row.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width
                }
                y += row.maxOf { it.height }
            }
        }
    }
}