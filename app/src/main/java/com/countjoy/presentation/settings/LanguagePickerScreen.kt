package com.countjoy.presentation.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.R
import com.countjoy.core.locale.LocaleManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePickerScreen(
    onNavigateBack: () -> Unit,
    onLanguageChanged: () -> Unit,
    viewModel: LanguagePickerViewModel = hiltViewModel()
) {
    val supportedLocales by viewModel.supportedLocales.collectAsState()
    val currentLocale by viewModel.currentLocale.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf<LocaleManager.LocaleInfo?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.language)) },
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
        ) {
            // Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = stringResource(id = R.string.language_selection_info),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            // Language List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(supportedLocales) { locale ->
                    LanguageItem(
                        locale = locale,
                        isSelected = locale.isCurrentLocale,
                        onClick = {
                            if (!locale.isCurrentLocale) {
                                selectedLanguage = locale
                                showConfirmDialog = true
                            }
                        }
                    )
                }
            }
        }
    }
    
    // Confirmation Dialog
    if (showConfirmDialog && selectedLanguage != null) {
        AlertDialog(
            onDismissRequest = { 
                showConfirmDialog = false
                selectedLanguage = null
            },
            title = {
                Text(stringResource(id = R.string.change_language))
            },
            text = {
                Column {
                    Text(stringResource(id = R.string.language_change_confirm, selectedLanguage!!.displayName))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedLanguage!!.flag,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = selectedLanguage!!.displayName,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (selectedLanguage!!.isRtl) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.language_rtl_warning),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedLanguage?.let {
                            viewModel.changeLanguage(it.code)
                            onLanguageChanged()
                        }
                        showConfirmDialog = false
                        selectedLanguage = null
                    }
                ) {
                    Text(stringResource(id = R.string.save))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        selectedLanguage = null
                    }
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun LanguageItem(
    locale: LocaleManager.LocaleInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "background"
    )
    
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !isSelected) { onClick() },
        color = backgroundColor,
        tonalElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag
            Text(
                text = locale.flag,
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            
            // Language Name
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = locale.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = contentColor
                )
                
                if (locale.isRtl) {
                    Text(
                        text = "RTL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            
            // Selected Indicator
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}