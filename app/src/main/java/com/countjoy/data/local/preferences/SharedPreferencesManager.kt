package com.countjoy.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for handling SharedPreferences with type-safe methods and encryption support
 */
@Singleton
class SharedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "countjoy_prefs"
        private const val ENCRYPTED_PREFS_NAME = "countjoy_encrypted_prefs"
        
        // Preference keys
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_NOTIFICATION_ENABLED = "notifications_enabled"
        const val KEY_SOUND_ENABLED = "sound_enabled"
        const val KEY_SOUND_VOLUME = "sound_volume"
        const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        const val KEY_HAPTIC_INTENSITY = "haptic_intensity"
        const val KEY_MILESTONE_NOTIFICATIONS = "milestone_notifications"
        const val KEY_COMPLETION_CELEBRATION = "completion_celebration"
        const val KEY_BUTTON_CLICK_FEEDBACK = "button_click_feedback"
        const val KEY_FIRST_LAUNCH = "first_launch"
        const val KEY_DEFAULT_COUNTDOWN_TIME = "default_countdown_time"
        const val KEY_TIME_FORMAT_24H = "time_format_24h"
        const val KEY_DATE_FORMAT = "date_format"
        const val KEY_AUTO_DELETE_EXPIRED = "auto_delete_expired"
        const val KEY_COUNTDOWN_UPDATE_INTERVAL = "countdown_update_interval"
        const val KEY_USER_ID = "user_id" // Encrypted
        const val KEY_USER_TOKEN = "user_token" // Encrypted
        
        // Theme modes
        const val THEME_MODE_SYSTEM = 0
        const val THEME_MODE_LIGHT = 1
        const val THEME_MODE_DARK = 2
        
        // Date formats
        const val DATE_FORMAT_DEFAULT = "MMM dd, yyyy"
        const val DATE_FORMAT_US = "MM/dd/yyyy"
        const val DATE_FORMAT_EU = "dd/MM/yyyy"
        const val DATE_FORMAT_ISO = "yyyy-MM-dd"
    }
    
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    private val encryptedPrefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    // Theme settings
    fun getThemeMode(): Int = prefs.getInt(KEY_THEME_MODE, THEME_MODE_SYSTEM)
    fun setThemeMode(mode: Int) = prefs.edit().putInt(KEY_THEME_MODE, mode).apply()
    
    // Notification settings
    fun isNotificationEnabled(): Boolean = prefs.getBoolean(KEY_NOTIFICATION_ENABLED, true)
    fun setNotificationEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply()
    
    fun isSoundEnabled(): Boolean = prefs.getBoolean(KEY_SOUND_ENABLED, true)
    fun setSoundEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
    
    fun getSoundEnabled(): Boolean = isSoundEnabled()
    
    fun getSoundVolume(): Float = prefs.getFloat(KEY_SOUND_VOLUME, 0.7f)
    fun setSoundVolume(volume: Float) = prefs.edit().putFloat(KEY_SOUND_VOLUME, volume).apply()
    
    fun isVibrationEnabled(): Boolean = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
    fun setVibrationEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply()
    
    fun getHapticEnabled(): Boolean = isVibrationEnabled()
    fun setHapticEnabled(enabled: Boolean) = setVibrationEnabled(enabled)
    
    fun getHapticIntensity(): Int = prefs.getInt(KEY_HAPTIC_INTENSITY, 128)
    fun setHapticIntensity(intensity: Int) = prefs.edit().putInt(KEY_HAPTIC_INTENSITY, intensity).apply()
    
    fun getMilestoneNotifications(): Boolean = prefs.getBoolean(KEY_MILESTONE_NOTIFICATIONS, true)
    fun setMilestoneNotifications(enabled: Boolean) = prefs.edit().putBoolean(KEY_MILESTONE_NOTIFICATIONS, enabled).apply()
    
    fun getCompletionCelebration(): Boolean = prefs.getBoolean(KEY_COMPLETION_CELEBRATION, true)
    fun setCompletionCelebration(enabled: Boolean) = prefs.edit().putBoolean(KEY_COMPLETION_CELEBRATION, enabled).apply()
    
    fun getButtonClickFeedback(): Boolean = prefs.getBoolean(KEY_BUTTON_CLICK_FEEDBACK, false)
    fun setButtonClickFeedback(enabled: Boolean) = prefs.edit().putBoolean(KEY_BUTTON_CLICK_FEEDBACK, enabled).apply()
    
    // App settings
    fun isFirstLaunch(): Boolean = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    fun setFirstLaunch(isFirst: Boolean) = prefs.edit().putBoolean(KEY_FIRST_LAUNCH, isFirst).apply()
    
    fun getDefaultCountdownTime(): Long = prefs.getLong(KEY_DEFAULT_COUNTDOWN_TIME, 86400L) // Default 1 day
    fun setDefaultCountdownTime(seconds: Long) = prefs.edit().putLong(KEY_DEFAULT_COUNTDOWN_TIME, seconds).apply()
    
    fun is24HourFormat(): Boolean = prefs.getBoolean(KEY_TIME_FORMAT_24H, false)
    fun set24HourFormat(is24Hour: Boolean) = prefs.edit().putBoolean(KEY_TIME_FORMAT_24H, is24Hour).apply()
    
    fun getDateFormat(): String = prefs.getString(KEY_DATE_FORMAT, DATE_FORMAT_DEFAULT) ?: DATE_FORMAT_DEFAULT
    fun setDateFormat(format: String) = prefs.edit().putString(KEY_DATE_FORMAT, format).apply()
    
    fun isAutoDeleteExpired(): Boolean = prefs.getBoolean(KEY_AUTO_DELETE_EXPIRED, false)
    fun setAutoDeleteExpired(autoDelete: Boolean) = prefs.edit().putBoolean(KEY_AUTO_DELETE_EXPIRED, autoDelete).apply()
    
    fun getCountdownUpdateInterval(): Long = prefs.getLong(KEY_COUNTDOWN_UPDATE_INTERVAL, 1000L) // Default 1 second
    fun setCountdownUpdateInterval(millis: Long) = prefs.edit().putLong(KEY_COUNTDOWN_UPDATE_INTERVAL, millis).apply()
    
    // Encrypted preferences for sensitive data
    fun getUserId(): String? = encryptedPrefs.getString(KEY_USER_ID, null)
    fun setUserId(userId: String?) = encryptedPrefs.edit().putString(KEY_USER_ID, userId).apply()
    
    fun getUserToken(): String? = encryptedPrefs.getString(KEY_USER_TOKEN, null)
    fun setUserToken(token: String?) = encryptedPrefs.edit().putString(KEY_USER_TOKEN, token).apply()
    
    // Generic type-safe methods
    fun getString(key: String, defaultValue: String? = null): String? {
        return prefs.getString(key, defaultValue)
    }
    
    fun putString(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }
    
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return prefs.getInt(key, defaultValue)
    }
    
    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
    
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }
    
    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
    
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return prefs.getLong(key, defaultValue)
    }
    
    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }
    
    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return prefs.getFloat(key, defaultValue)
    }
    
    fun putFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }
    
    // Flow support for reactive updates
    fun observeBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) {
                trySend(getBoolean(key, defaultValue))
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(getBoolean(key, defaultValue))
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }
    
    fun observeString(key: String, defaultValue: String? = null): Flow<String?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) {
                trySend(getString(key, defaultValue))
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(getString(key, defaultValue))
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }
    
    fun observeInt(key: String, defaultValue: Int = 0): Flow<Int> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) {
                trySend(getInt(key, defaultValue))
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(getInt(key, defaultValue))
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }
    
    // Clear all preferences
    fun clearAll() {
        prefs.edit().clear().apply()
        encryptedPrefs.edit().clear().apply()
    }
    
    fun clearNonEncrypted() {
        prefs.edit().clear().apply()
    }
    
    fun clearEncrypted() {
        encryptedPrefs.edit().clear().apply()
    }
    
    // Migration support
    fun migrate(oldKey: String, newKey: String) {
        if (prefs.contains(oldKey)) {
            val value = prefs.all[oldKey]
            val editor = prefs.edit()
            
            when (value) {
                is String -> editor.putString(newKey, value)
                is Int -> editor.putInt(newKey, value)
                is Boolean -> editor.putBoolean(newKey, value)
                is Long -> editor.putLong(newKey, value)
                is Float -> editor.putFloat(newKey, value)
            }
            
            editor.remove(oldKey).apply()
        }
    }
}