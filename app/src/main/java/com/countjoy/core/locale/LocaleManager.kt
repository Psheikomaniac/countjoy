package com.countjoy.core.locale

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.countjoy.data.local.preferences.SharedPreferencesManager
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages application locale and language settings.
 * Handles runtime language switching and locale persistence.
 */
@Singleton
class LocaleManager @Inject constructor(
    private val context: Context,
    private val preferencesManager: SharedPreferencesManager
) {
    companion object {
        const val PREFERENCE_KEY_LANGUAGE = "app_language"
        const val DEFAULT_LANGUAGE = "en"
        
        // Supported languages with their native names
        val SUPPORTED_LOCALES = mapOf(
            "en" to Pair("English", "🇺🇸"),
            "es" to Pair("Español", "🇪🇸"),
            "fr" to Pair("Français", "🇫🇷"),
            "de" to Pair("Deutsch", "🇩🇪"),
            "it" to Pair("Italiano", "🇮🇹"),
            "pt" to Pair("Português", "🇵🇹"),
            "nl" to Pair("Nederlands", "🇳🇱"),
            "ru" to Pair("Русский", "🇷🇺"),
            "ja" to Pair("日本語", "🇯🇵"),
            "ko" to Pair("한국어", "🇰🇷"),
            "zh" to Pair("中文", "🇨🇳"),
            "ar" to Pair("العربية", "🇸🇦"),
            "hi" to Pair("हिन्दी", "🇮🇳"),
            "tr" to Pair("Türkçe", "🇹🇷")
        )
    }
    
    /**
     * Gets the currently active locale.
     */
    fun getCurrentLocale(): Locale {
        val languageCode = preferencesManager.getString(PREFERENCE_KEY_LANGUAGE, DEFAULT_LANGUAGE)
        return Locale(languageCode)
    }
    
    /**
     * Sets the application locale and persists the preference.
     * @param languageCode The ISO 639-1 language code (e.g., "en", "es", "fr")
     */
    fun setAppLocale(languageCode: String) {
        if (languageCode !in SUPPORTED_LOCALES.keys) {
            throw IllegalArgumentException("Unsupported language code: $languageCode")
        }
        
        // Save preference
        preferencesManager.putString(PREFERENCE_KEY_LANGUAGE, languageCode)
        
        // Apply locale change
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ per-app language preference
            val appLocale = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
        } else {
            // Legacy approach for older Android versions
            val locale = Locale(languageCode)
            updateResources(context, locale)
        }
    }
    
    /**
     * Gets the list of supported locales with their display names.
     */
    fun getSupportedLocales(): List<LocaleInfo> {
        return SUPPORTED_LOCALES.map { (code, info) ->
            LocaleInfo(
                code = code,
                displayName = info.first,
                flag = info.second,
                isRtl = isRtlLocale(code),
                isCurrentLocale = code == getCurrentLocale().language
            )
        }
    }
    
    /**
     * Applies the saved locale to a context.
     * This should be called in Application.attachBaseContext() and Activity.attachBaseContext()
     */
    fun applyLocaleToContext(baseContext: Context): Context {
        val locale = getCurrentLocale()
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesApi24AndAbove(baseContext, locale)
        } else {
            updateResourcesLegacy(baseContext, locale)
        }
    }
    
    /**
     * Checks if a locale is RTL (Right-to-Left).
     */
    fun isRtlLocale(languageCode: String): Boolean {
        return languageCode in listOf("ar", "he", "fa", "ur")
    }
    
    /**
     * Gets the layout direction for the current locale.
     */
    fun getLayoutDirection(): Int {
        val currentLanguage = getCurrentLocale().language
        return if (isRtlLocale(currentLanguage)) {
            Configuration.SCREENLAYOUT_LAYOUTDIR_RTL
        } else {
            Configuration.SCREENLAYOUT_LAYOUTDIR_LTR
        }
    }
    
    /**
     * Updates resources for API 24 and above.
     */
    private fun updateResourcesApi24AndAbove(context: Context, locale: Locale): Context {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        
        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        
        return context.createConfigurationContext(configuration)
    }
    
    /**
     * Updates resources for older Android versions.
     */
    private fun updateResourcesLegacy(context: Context, locale: Locale): Context {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        return context
    }
    
    /**
     * Updates the app's resources with the new locale.
     * Used for immediate locale change without restart.
     */
    private fun updateResources(context: Context, locale: Locale) {
        Locale.setDefault(locale)
        
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.setLocale(locale)
        }
        
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
    
    /**
     * Data class representing locale information.
     */
    data class LocaleInfo(
        val code: String,
        val displayName: String,
        val flag: String,
        val isRtl: Boolean,
        val isCurrentLocale: Boolean
    )
}