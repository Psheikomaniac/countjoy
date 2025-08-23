package com.countjoy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.countjoy.core.accessibility.AccessibilityManager
import com.countjoy.data.local.preferences.SharedPreferencesManager
import kotlinx.coroutines.flow.Flow
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.text.font.FontWeight

private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim
)

private val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim
)

// High Contrast Color Schemes
private val HighContrastDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFFE0E0E0),
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFFCCCCCC),
    onSecondary = Color(0xFF000000),
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFFFFFFF),
    error = Color(0xFFFF0000),
    onError = Color(0xFFFFFFFF)
)

private val HighContrastLightColorScheme = lightColorScheme(
    primary = Color(0xFF000000),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF303030),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFF404040),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFF5F5F5),
    onSurface = Color(0xFF000000),
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF)
)

val LocalAccessibilityManager = compositionLocalOf<AccessibilityManager?> { null }

@Composable
fun CountJoyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    preferencesManager: SharedPreferencesManager? = null,
    accessibilityManager: AccessibilityManager? = null,
    content: @Composable () -> Unit
) {
    // Determine the actual theme based on preferences
    val actualDarkTheme = if (preferencesManager != null) {
        val themeMode = remember { preferencesManager.observeInt(SharedPreferencesManager.KEY_THEME_MODE, SharedPreferencesManager.THEME_MODE_SYSTEM) }
            .collectAsState(initial = preferencesManager.getThemeMode())
        
        when (themeMode.value) {
            SharedPreferencesManager.THEME_MODE_LIGHT -> false
            SharedPreferencesManager.THEME_MODE_DARK -> true
            else -> darkTheme // THEME_MODE_SYSTEM
        }
    } else {
        darkTheme
    }
    
    // Check accessibility settings
    val isHighContrast = accessibilityManager?.isHighContrastEnabled() ?: false
    val fontScale = accessibilityManager?.getFontSize()?.scale ?: 1.0f
    val isBoldText = accessibilityManager?.isBoldTextEnabled() ?: false
    
    val colorScheme = when {
        isHighContrast -> {
            if (actualDarkTheme) HighContrastDarkColorScheme else HighContrastLightColorScheme
        }
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (actualDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        actualDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !actualDarkTheme
        }
    }

    // Create typography with accessibility adjustments
    val accessibleTypography = if (fontScale != 1.0f || isBoldText) {
        Typography.copy(
            displayLarge = Typography.displayLarge.copy(
                fontSize = Typography.displayLarge.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.displayLarge.fontWeight
            ),
            displayMedium = Typography.displayMedium.copy(
                fontSize = Typography.displayMedium.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.displayMedium.fontWeight
            ),
            displaySmall = Typography.displaySmall.copy(
                fontSize = Typography.displaySmall.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.displaySmall.fontWeight
            ),
            headlineLarge = Typography.headlineLarge.copy(
                fontSize = Typography.headlineLarge.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.headlineLarge.fontWeight
            ),
            headlineMedium = Typography.headlineMedium.copy(
                fontSize = Typography.headlineMedium.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.headlineMedium.fontWeight
            ),
            headlineSmall = Typography.headlineSmall.copy(
                fontSize = Typography.headlineSmall.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.headlineSmall.fontWeight
            ),
            titleLarge = Typography.titleLarge.copy(
                fontSize = Typography.titleLarge.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.titleLarge.fontWeight
            ),
            titleMedium = Typography.titleMedium.copy(
                fontSize = Typography.titleMedium.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.titleMedium.fontWeight
            ),
            titleSmall = Typography.titleSmall.copy(
                fontSize = Typography.titleSmall.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.titleSmall.fontWeight
            ),
            bodyLarge = Typography.bodyLarge.copy(
                fontSize = Typography.bodyLarge.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.bodyLarge.fontWeight
            ),
            bodyMedium = Typography.bodyMedium.copy(
                fontSize = Typography.bodyMedium.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.bodyMedium.fontWeight
            ),
            bodySmall = Typography.bodySmall.copy(
                fontSize = Typography.bodySmall.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.bodySmall.fontWeight
            ),
            labelLarge = Typography.labelLarge.copy(
                fontSize = Typography.labelLarge.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.labelLarge.fontWeight
            ),
            labelMedium = Typography.labelMedium.copy(
                fontSize = Typography.labelMedium.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.labelMedium.fontWeight
            ),
            labelSmall = Typography.labelSmall.copy(
                fontSize = Typography.labelSmall.fontSize * fontScale,
                fontWeight = if (isBoldText) FontWeight.Bold else Typography.labelSmall.fontWeight
            )
        )
    } else {
        Typography
    }
    
    CompositionLocalProvider(LocalAccessibilityManager provides accessibilityManager) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = accessibleTypography,
            shapes = Shapes,
            content = content
        )
    }
}