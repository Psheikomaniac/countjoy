package com.countjoy.core.accessibility

import android.content.Context
import android.view.accessibility.AccessibilityManager as SystemAccessibilityManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.countjoy.data.local.preferences.SharedPreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages accessibility settings and provides accessibility-related utilities.
 */
@Singleton
class AccessibilityManager @Inject constructor(
    private val context: Context,
    private val preferencesManager: SharedPreferencesManager
) {
    companion object {
        // Preference Keys
        const val PREF_FONT_SIZE = "accessibility_font_size"
        const val PREF_HIGH_CONTRAST = "accessibility_high_contrast"
        const val PREF_COLOR_BLIND_MODE = "accessibility_color_blind_mode"
        const val PREF_REDUCE_MOTION = "accessibility_reduce_motion"
        const val PREF_VOICE_CONTROL = "accessibility_voice_control"
        const val PREF_TOUCH_TARGET_SIZE = "accessibility_touch_target_size"
        const val PREF_TOUCH_HOLD_DELAY = "accessibility_touch_hold_delay"
        const val PREF_BOLD_TEXT = "accessibility_bold_text"
        const val PREF_SIMPLE_MODE = "accessibility_simple_mode"
        
        // Font Size Levels
        enum class FontSize(val scale: Float) {
            EXTRA_SMALL(0.85f),
            SMALL(0.9f),
            MEDIUM(1.0f),
            LARGE(1.15f),
            EXTRA_LARGE(1.3f)
        }
        
        // Color Blind Modes
        enum class ColorBlindMode {
            NONE,
            PROTANOPIA,      // Red-blind
            DEUTERANOPIA,    // Green-blind
            TRITANOPIA,      // Blue-blind
            ACHROMATOPSIA    // Complete color blindness
        }
        
        // Touch Target Sizes (in dp)
        enum class TouchTargetSize(val sizeDp: Int) {
            SMALL(44),
            MEDIUM(48),
            LARGE(56)
        }
        
        // Touch Hold Delays (in ms)
        enum class TouchHoldDelay(val delayMs: Long) {
            SHORT(300),
            MEDIUM(500),
            LONG(800)
        }
    }
    
    private val systemAccessibilityManager = 
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as SystemAccessibilityManager
    
    // Font Size
    fun getFontSize(): FontSize {
        val scale = preferencesManager.getFloat(PREF_FONT_SIZE, FontSize.MEDIUM.scale)
        return FontSize.values().minByOrNull { kotlin.math.abs(it.scale - scale) } ?: FontSize.MEDIUM
    }
    
    fun setFontSize(fontSize: FontSize) {
        preferencesManager.putFloat(PREF_FONT_SIZE, fontSize.scale)
    }
    
    // High Contrast
    fun isHighContrastEnabled(): Boolean {
        return preferencesManager.getBoolean(PREF_HIGH_CONTRAST, false)
    }
    
    fun setHighContrastEnabled(enabled: Boolean) {
        preferencesManager.putBoolean(PREF_HIGH_CONTRAST, enabled)
    }
    
    // Color Blind Mode
    fun getColorBlindMode(): ColorBlindMode {
        val modeName = preferencesManager.getString(PREF_COLOR_BLIND_MODE, ColorBlindMode.NONE.name)
        return try {
            ColorBlindMode.valueOf(modeName ?: ColorBlindMode.NONE.name)
        } catch (e: IllegalArgumentException) {
            ColorBlindMode.NONE
        }
    }
    
    fun setColorBlindMode(mode: ColorBlindMode) {
        preferencesManager.putString(PREF_COLOR_BLIND_MODE, mode.name)
    }
    
    // Reduce Motion
    fun isReduceMotionEnabled(): Boolean {
        return preferencesManager.getBoolean(PREF_REDUCE_MOTION, false)
    }
    
    fun setReduceMotionEnabled(enabled: Boolean) {
        preferencesManager.putBoolean(PREF_REDUCE_MOTION, enabled)
    }
    
    // Voice Control
    fun isVoiceControlEnabled(): Boolean {
        return preferencesManager.getBoolean(PREF_VOICE_CONTROL, false)
    }
    
    fun setVoiceControlEnabled(enabled: Boolean) {
        preferencesManager.putBoolean(PREF_VOICE_CONTROL, enabled)
    }
    
    // Touch Target Size
    fun getTouchTargetSize(): TouchTargetSize {
        val sizeDp = preferencesManager.getInt(PREF_TOUCH_TARGET_SIZE, TouchTargetSize.MEDIUM.sizeDp)
        return TouchTargetSize.values().minByOrNull { kotlin.math.abs(it.sizeDp - sizeDp) } 
            ?: TouchTargetSize.MEDIUM
    }
    
    fun setTouchTargetSize(size: TouchTargetSize) {
        preferencesManager.putInt(PREF_TOUCH_TARGET_SIZE, size.sizeDp)
    }
    
    // Touch Hold Delay
    fun getTouchHoldDelay(): TouchHoldDelay {
        val delayMs = preferencesManager.getLong(PREF_TOUCH_HOLD_DELAY, TouchHoldDelay.MEDIUM.delayMs)
        return TouchHoldDelay.values().minByOrNull { kotlin.math.abs(it.delayMs - delayMs) }
            ?: TouchHoldDelay.MEDIUM
    }
    
    fun setTouchHoldDelay(delay: TouchHoldDelay) {
        preferencesManager.putLong(PREF_TOUCH_HOLD_DELAY, delay.delayMs)
    }
    
    // Bold Text
    fun isBoldTextEnabled(): Boolean {
        return preferencesManager.getBoolean(PREF_BOLD_TEXT, false)
    }
    
    fun setBoldTextEnabled(enabled: Boolean) {
        preferencesManager.putBoolean(PREF_BOLD_TEXT, enabled)
    }
    
    // Simple Mode
    fun isSimpleModeEnabled(): Boolean {
        return preferencesManager.getBoolean(PREF_SIMPLE_MODE, false)
    }
    
    fun setSimpleModeEnabled(enabled: Boolean) {
        preferencesManager.putBoolean(PREF_SIMPLE_MODE, enabled)
    }
    
    // System Accessibility
    fun isScreenReaderEnabled(): Boolean {
        return systemAccessibilityManager.isEnabled && 
               systemAccessibilityManager.isTouchExplorationEnabled
    }
    
    fun getRecommendedContentDescription(text: String, context: String): String {
        return if (isScreenReaderEnabled()) {
            // Add context for screen readers
            "$text. $context"
        } else {
            text
        }
    }
    
    // Color Transformation Matrices for Color Blind Modes
    fun getColorTransformMatrix(mode: ColorBlindMode): FloatArray? {
        return when (mode) {
            ColorBlindMode.PROTANOPIA -> floatArrayOf(
                0.567f, 0.433f, 0f, 0f, 0f,
                0.558f, 0.442f, 0f, 0f, 0f,
                0f, 0.242f, 0.758f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
            ColorBlindMode.DEUTERANOPIA -> floatArrayOf(
                0.625f, 0.375f, 0f, 0f, 0f,
                0.7f, 0.3f, 0f, 0f, 0f,
                0f, 0.3f, 0.7f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
            ColorBlindMode.TRITANOPIA -> floatArrayOf(
                0.95f, 0.05f, 0f, 0f, 0f,
                0f, 0.433f, 0.567f, 0f, 0f,
                0f, 0.475f, 0.525f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
            ColorBlindMode.ACHROMATOPSIA -> floatArrayOf(
                0.299f, 0.587f, 0.114f, 0f, 0f,
                0.299f, 0.587f, 0.114f, 0f, 0f,
                0.299f, 0.587f, 0.114f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
            ColorBlindMode.NONE -> null
        }
    }
}

/**
 * Composable helper to get accessibility settings
 */
@Composable
fun rememberAccessibilitySettings(): AccessibilitySettings {
    val context = LocalContext.current
    val preferencesManager = remember { SharedPreferencesManager(context) }
    val accessibilityManager = remember { AccessibilityManager(context, preferencesManager) }
    
    return remember {
        AccessibilitySettings(
            fontSize = mutableStateOf(accessibilityManager.getFontSize()),
            isHighContrastEnabled = mutableStateOf(accessibilityManager.isHighContrastEnabled()),
            colorBlindMode = mutableStateOf(accessibilityManager.getColorBlindMode()),
            isReduceMotionEnabled = mutableStateOf(accessibilityManager.isReduceMotionEnabled()),
            isVoiceControlEnabled = mutableStateOf(accessibilityManager.isVoiceControlEnabled()),
            isBoldTextEnabled = mutableStateOf(accessibilityManager.isBoldTextEnabled()),
            touchTargetSize = mutableStateOf(accessibilityManager.getTouchTargetSize()),
            isScreenReaderEnabled = mutableStateOf(accessibilityManager.isScreenReaderEnabled()),
            isSimpleModeEnabled = mutableStateOf(accessibilityManager.isSimpleModeEnabled())
        )
    }
}

/**
 * Data class to hold accessibility settings state
 */
data class AccessibilitySettings(
    val fontSize: State<AccessibilityManager.Companion.FontSize>,
    val isHighContrastEnabled: State<Boolean>,
    val colorBlindMode: State<AccessibilityManager.Companion.ColorBlindMode>,
    val isReduceMotionEnabled: State<Boolean>,
    val isVoiceControlEnabled: State<Boolean>,
    val isBoldTextEnabled: State<Boolean>,
    val touchTargetSize: State<AccessibilityManager.Companion.TouchTargetSize>,
    val isScreenReaderEnabled: State<Boolean>,
    val isSimpleModeEnabled: State<Boolean>
)