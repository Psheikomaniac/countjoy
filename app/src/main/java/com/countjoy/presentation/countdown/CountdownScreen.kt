package com.countjoy.presentation.countdown

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.R
import com.countjoy.domain.model.CountdownEvent
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountdownScreen(
    onNavigateToEventInput: (Long?) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: CountdownViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.settings),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToEventInput(null) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_event))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.events.isEmpty() -> {
                    EmptyState(
                        onAddEvent = { onNavigateToEventInput(null) }
                    )
                }
                else -> {
                    EventList(
                        events = uiState.events.map { it.event },
                        onEventClick = { event ->
                            onNavigateToEventInput(event.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(
    onAddEvent: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.no_events_yet),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.create_first_countdown),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddEvent) {
            Text(stringResource(id = R.string.create_event))
        }
    }
}

@Composable
fun EventList(
    events: List<CountdownEvent>,
    onEventClick: (CountdownEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(events) { event ->
            EventCard(
                event = event,
                onClick = { onEventClick(event) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(
    event: CountdownEvent,
    onClick: () -> Unit
) {
    val detailedTime = event.getDetailedTimeRemaining()
    val isExpired = event.hasExpired()
    
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = if (isExpired) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall
            )
            
            event.description?.let { desc ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Detailed countdown display
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Main countdown display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isExpired) {
                        Text(
                            text = stringResource(id = R.string.expired),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        // Check if countdown has reached zero (all values are 0)
                        val hasReachedZero = detailedTime.days == 0L && 
                                           detailedTime.hours == 0 && 
                                           detailedTime.minutes == 0 && 
                                           detailedTime.seconds == 0
                        
                        if (hasReachedZero) {
                            // Hide counter when it reaches 0, show completion message instead
                            Text(
                                text = stringResource(id = R.string.event_completed),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                TimeUnit(value = detailedTime.days.toInt(), label = stringResource(id = R.string.days))
                                TimeUnit(value = detailedTime.hours, label = stringResource(id = R.string.hours))
                                TimeUnit(value = detailedTime.minutes, label = stringResource(id = R.string.minutes))
                                TimeUnit(value = detailedTime.seconds, label = "Sec")
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Date and time info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.targetDateTime.format(
                            DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Quick summary text
                    Text(
                        text = when {
                            isExpired -> "Event passed"
                            detailedTime.days == 0L && detailedTime.hours < 1 -> "Less than an hour!"
                            detailedTime.days == 0L -> "Today!"
                            detailedTime.days == 1L -> "Tomorrow"
                            detailedTime.days <= 7 -> "This week"
                            detailedTime.days <= 30 -> "This month"
                            else -> "In ${detailedTime.days} days"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = when {
                            isExpired -> MaterialTheme.colorScheme.error
                            detailedTime.days <= 1 -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TimeUnit(
    value: Int,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.toString().padStart(2, '0'),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}