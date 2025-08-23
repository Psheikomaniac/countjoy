package com.countjoy.presentation.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.countjoy.R
import com.countjoy.core.locale.LocaleManager
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLanguageClick: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
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
                .verticalScroll(scrollState)
        ) {
            // Theme Section
            SettingsSection(
                title = stringResource(id = R.string.appearance),
                icon = Icons.Default.Edit
            ) {
                ThemeSelector(
                    selectedTheme = uiState.themeMode,
                    onThemeSelected = viewModel::onThemeModeChanged
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Notification Section
            SettingsSection(
                title = stringResource(id = R.string.notifications),
                icon = Icons.Default.Notifications
            ) {
                SwitchSettingItem(
                    title = stringResource(id = R.string.enable_notifications),
                    subtitle = stringResource(id = R.string.notification_description),
                    isChecked = uiState.notificationsEnabled,
                    onCheckedChange = viewModel::onNotificationsChanged
                )
                
                AnimatedVisibility(visible = uiState.notificationsEnabled) {
                    Column {
                        SwitchSettingItem(
                            title = stringResource(id = R.string.notification_sound),
                            subtitle = stringResource(id = R.string.sound_description),
                            isChecked = uiState.soundEnabled,
                            onCheckedChange = viewModel::onSoundChanged,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        SwitchSettingItem(
                            title = stringResource(id = R.string.notification_vibration),
                            subtitle = stringResource(id = R.string.vibration_description),
                            isChecked = uiState.vibrationEnabled,
                            onCheckedChange = viewModel::onVibrationChanged,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Language Section
            SettingsSection(
                title = stringResource(id = R.string.language_and_region),
                icon = Icons.Default.Place
            ) {
                ClickableSettingItem(
                    title = stringResource(id = R.string.language),
                    subtitle = getCurrentLanguageName(viewModel.uiState.collectAsState().value.currentLanguageCode),
                    onClick = onLanguageClick
                )
                
                ClickableSettingItem(
                    title = stringResource(id = R.string.date_format),
                    subtitle = uiState.dateFormat,
                    onClick = { /* TODO: Show date format picker */ }
                )
                
                SwitchSettingItem(
                    title = stringResource(id = R.string.twenty_four_hour_format),
                    subtitle = stringResource(id = R.string.twenty_four_hour_description),
                    isChecked = uiState.use24HourFormat,
                    onCheckedChange = viewModel::on24HourFormatChanged
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Display Section
            SettingsSection(
                title = stringResource(id = R.string.display),
                icon = Icons.Default.Settings
            ) {
                // Moved to Language & Region section
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Data Management Section
            SettingsSection(
                title = stringResource(id = R.string.data_management),
                icon = Icons.Default.AccountBox
            ) {
                SwitchSettingItem(
                    title = stringResource(id = R.string.auto_delete_expired),
                    subtitle = stringResource(id = R.string.auto_delete_description),
                    isChecked = uiState.autoDeleteExpired,
                    onCheckedChange = viewModel::onAutoDeleteChanged
                )
                
                ClickableSettingItem(
                    title = stringResource(id = R.string.backup_data),
                    subtitle = stringResource(id = R.string.backup_description),
                    onClick = { /* TODO: Implement backup */ }
                )
                
                ClickableSettingItem(
                    title = stringResource(id = R.string.restore_data),
                    subtitle = stringResource(id = R.string.restore_description),
                    onClick = { /* TODO: Implement restore */ }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // About Section
            SettingsSection(
                title = stringResource(id = R.string.about),
                icon = Icons.Default.Info
            ) {
                ClickableSettingItem(
                    title = stringResource(id = R.string.version),
                    subtitle = "1.0.0",
                    onClick = { }
                )
                
                ClickableSettingItem(
                    title = stringResource(id = R.string.privacy_policy),
                    subtitle = stringResource(id = R.string.privacy_policy_description),
                    onClick = { /* TODO: Open privacy policy */ }
                )
                
                ClickableSettingItem(
                    title = stringResource(id = R.string.terms_of_service),
                    subtitle = stringResource(id = R.string.terms_of_service_description),
                    onClick = { /* TODO: Open terms */ }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun getCurrentLanguageName(languageCode: String?): String {
    val currentCode = languageCode ?: Locale.getDefault().language
    return LocaleManager.SUPPORTED_LOCALES[currentCode]?.first ?: "English"
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        content()
    }
}

@Composable
private fun ThemeSelector(
    selectedTheme: Int,
    onThemeSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .selectableGroup()
    ) {
        ThemeOption(
            title = "System Default",
            subtitle = "Follow system theme",
            icon = Icons.Default.Phone,
            isSelected = selectedTheme == 0,
            onClick = { onThemeSelected(0) }
        )
        ThemeOption(
            title = "Light Theme",
            subtitle = "Always use light theme",
            icon = Icons.Default.Star,
            isSelected = selectedTheme == 1,
            onClick = { onThemeSelected(1) }
        )
        ThemeOption(
            title = "Dark Theme",
            subtitle = "Always use dark theme",
            icon = Icons.Default.Lock,
            isSelected = selectedTheme == 2,
            onClick = { onThemeSelected(2) }
        )
    }
}

@Composable
private fun ThemeOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(300),
        label = "background"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun SwitchSettingItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ClickableSettingItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}