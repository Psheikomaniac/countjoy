package com.countjoy.service

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RawRes
import com.countjoy.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for managing sound and haptic feedback throughout the app
 */
@Singleton
class SoundHapticService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    
    private var mediaPlayer: MediaPlayer? = null
    
    enum class SoundType {
        NOTIFICATION,
        CELEBRATION,
        BUTTON_CLICK,
        WARNING,
        SUCCESS,
        ERROR
    }
    
    enum class HapticPattern {
        LIGHT_TAP,        // Single light tap
        DOUBLE_TAP,       // Two quick taps
        LONG_VIBRATION,   // Single long vibration
        SUCCESS_PATTERN,  // Pattern for success feedback
        WARNING_PATTERN,  // Pattern for warnings
        CELEBRATION       // Special celebration pattern
    }
    
    /**
     * Play a sound effect
     */
    fun playSound(soundType: SoundType, volume: Float = 1.0f) {
        if (!isSoundEnabled()) return
        
        try {
            // Release previous player if exists
            mediaPlayer?.release()
            
            val soundUri = when (soundType) {
                SoundType.NOTIFICATION -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                SoundType.CELEBRATION -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                SoundType.BUTTON_CLICK -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                SoundType.WARNING -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                SoundType.SUCCESS -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                SoundType.ERROR -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }
            
            mediaPlayer = MediaPlayer.create(context, soundUri)?.apply {
                setVolume(volume, volume)
                setOnCompletionListener { release() }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Play a custom sound from raw resources
     */
    fun playCustomSound(@RawRes soundResId: Int, volume: Float = 1.0f) {
        if (!isSoundEnabled()) return
        
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, soundResId)?.apply {
                setVolume(volume, volume)
                setOnCompletionListener { release() }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Trigger haptic feedback
     */
    fun triggerHaptic(pattern: HapticPattern, intensity: Int = VibrationEffect.DEFAULT_AMPLITUDE) {
        if (!isHapticEnabled()) return
        if (!vibrator.hasVibrator()) return
        
        val vibrationPattern = when (pattern) {
            HapticPattern.LIGHT_TAP -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createOneShot(50, intensity)
                } else {
                    null
                }
            }
            HapticPattern.DOUBLE_TAP -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 50, 50, 50),
                        intArrayOf(0, intensity, 0, intensity),
                        -1
                    )
                } else {
                    null
                }
            }
            HapticPattern.LONG_VIBRATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createOneShot(500, intensity)
                } else {
                    null
                }
            }
            HapticPattern.SUCCESS_PATTERN -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 100, 100, 100),
                        intArrayOf(0, intensity, 0, intensity / 2),
                        -1
                    )
                } else {
                    null
                }
            }
            HapticPattern.WARNING_PATTERN -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 200, 100, 200),
                        intArrayOf(0, intensity, 0, intensity),
                        -1
                    )
                } else {
                    null
                }
            }
            HapticPattern.CELEBRATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 50, 50, 50, 50, 50, 50, 100),
                        intArrayOf(0, intensity/2, 0, intensity/2, 0, intensity, 0, intensity),
                        -1
                    )
                } else {
                    null
                }
            }
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrationPattern != null) {
            vibrator.vibrate(vibrationPattern)
        } else {
            // Fallback for older devices
            @Suppress("DEPRECATION")
            when (pattern) {
                HapticPattern.LIGHT_TAP -> vibrator.vibrate(50)
                HapticPattern.DOUBLE_TAP -> vibrator.vibrate(longArrayOf(0, 50, 50, 50), -1)
                HapticPattern.LONG_VIBRATION -> vibrator.vibrate(500)
                HapticPattern.SUCCESS_PATTERN -> vibrator.vibrate(longArrayOf(0, 100, 100, 100), -1)
                HapticPattern.WARNING_PATTERN -> vibrator.vibrate(longArrayOf(0, 200, 100, 200), -1)
                HapticPattern.CELEBRATION -> vibrator.vibrate(longArrayOf(0, 50, 50, 50, 50, 50, 50, 100), -1)
            }
        }
    }
    
    /**
     * Play countdown milestone reached effect
     */
    fun playMilestoneEffect(milestonePercentage: Int) {
        when (milestonePercentage) {
            100 -> {
                playSound(SoundType.CELEBRATION)
                triggerHaptic(HapticPattern.CELEBRATION)
            }
            75, 50, 25 -> {
                playSound(SoundType.SUCCESS)
                triggerHaptic(HapticPattern.SUCCESS_PATTERN)
            }
            10 -> {
                playSound(SoundType.WARNING)
                triggerHaptic(HapticPattern.WARNING_PATTERN)
            }
            else -> {
                playSound(SoundType.NOTIFICATION)
                triggerHaptic(HapticPattern.LIGHT_TAP)
            }
        }
    }
    
    /**
     * Play event completion celebration
     */
    fun playEventCompletionCelebration() {
        playSound(SoundType.CELEBRATION)
        triggerHaptic(HapticPattern.CELEBRATION)
    }
    
    /**
     * Play button click feedback
     */
    fun playButtonClickFeedback() {
        if (isSoundEnabled()) {
            // Play subtle click sound at lower volume
            playSound(SoundType.BUTTON_CLICK, volume = 0.3f)
        }
        if (isHapticEnabled()) {
            triggerHaptic(HapticPattern.LIGHT_TAP, intensity = 50)
        }
    }
    
    /**
     * Check if sound is enabled
     */
    private fun isSoundEnabled(): Boolean {
        // Check user preferences and system settings
        val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("sound_enabled", true) && 
               audioManager.ringerMode != AudioManager.RINGER_MODE_SILENT
    }
    
    /**
     * Check if haptic feedback is enabled
     */
    private fun isHapticEnabled(): Boolean {
        // Check user preferences
        val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("haptic_enabled", true)
    }
    
    /**
     * Update sound enabled preference
     */
    fun setSoundEnabled(enabled: Boolean) {
        val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("sound_enabled", enabled).apply()
    }
    
    /**
     * Update haptic enabled preference
     */
    fun setHapticEnabled(enabled: Boolean) {
        val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("haptic_enabled", enabled).apply()
    }
    
    /**
     * Clean up resources
     */
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}