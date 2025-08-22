package com.countjoy.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Window size classes for responsive design
 */
enum class WindowSizeClass {
    COMPACT,  // Phone in portrait
    MEDIUM,   // Large phone or small tablet
    EXPANDED  // Tablet or desktop
}

/**
 * Data class containing window size information
 */
data class WindowSizeInfo(
    val widthSizeClass: WindowSizeClass,
    val heightSizeClass: WindowSizeClass,
    val widthDp: Dp,
    val heightDp: Dp,
    val isTablet: Boolean,
    val isLandscape: Boolean
)

/**
 * Calculate window size class based on width
 */
fun calculateWindowSizeClass(widthDp: Dp): WindowSizeClass {
    return when {
        widthDp < 600.dp -> WindowSizeClass.COMPACT
        widthDp < 840.dp -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }
}

/**
 * Composable to get window size information
 */
@Composable
fun rememberWindowSizeInfo(): WindowSizeInfo {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        val widthDp = configuration.screenWidthDp.dp
        val heightDp = configuration.screenHeightDp.dp
        val widthSizeClass = calculateWindowSizeClass(widthDp)
        val heightSizeClass = calculateWindowSizeClass(heightDp)
        
        WindowSizeInfo(
            widthSizeClass = widthSizeClass,
            heightSizeClass = heightSizeClass,
            widthDp = widthDp,
            heightDp = heightDp,
            isTablet = widthSizeClass != WindowSizeClass.COMPACT,
            isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
        )
    }
}

/**
 * Adaptive layout configurations based on window size
 */
object AdaptiveLayoutConfig {
    @Composable
    fun getColumns(windowSizeInfo: WindowSizeInfo): Int {
        return when (windowSizeInfo.widthSizeClass) {
            WindowSizeClass.COMPACT -> 1
            WindowSizeClass.MEDIUM -> 2
            WindowSizeClass.EXPANDED -> 3
        }
    }
    
    @Composable
    fun getContentPadding(windowSizeInfo: WindowSizeInfo): Dp {
        return when (windowSizeInfo.widthSizeClass) {
            WindowSizeClass.COMPACT -> 16.dp
            WindowSizeClass.MEDIUM -> 24.dp
            WindowSizeClass.EXPANDED -> 32.dp
        }
    }
    
    @Composable
    fun getCardMaxWidth(windowSizeInfo: WindowSizeInfo): Dp {
        return when (windowSizeInfo.widthSizeClass) {
            WindowSizeClass.COMPACT -> Dp.Infinity
            WindowSizeClass.MEDIUM -> 400.dp
            WindowSizeClass.EXPANDED -> 500.dp
        }
    }
    
    @Composable
    fun shouldShowNavigationRail(windowSizeInfo: WindowSizeInfo): Boolean {
        return windowSizeInfo.widthSizeClass != WindowSizeClass.COMPACT
    }
    
    @Composable
    fun shouldShowDetailPane(windowSizeInfo: WindowSizeInfo): Boolean {
        return windowSizeInfo.widthSizeClass == WindowSizeClass.EXPANDED
    }
}