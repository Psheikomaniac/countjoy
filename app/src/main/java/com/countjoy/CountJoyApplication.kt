package com.countjoy

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.countjoy.core.locale.LocaleManager
import com.countjoy.data.local.preferences.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CountJoyApplication : Application() {
    
    @Inject
    lateinit var localeManager: LocaleManager
    
    override fun attachBaseContext(base: Context) {
        // Apply the saved locale before the application is created
        val preferencesManager = SharedPreferencesManager(base)
        val localeManager = LocaleManager(base, preferencesManager)
        super.attachBaseContext(localeManager.applyLocaleToContext(base))
    }
    
    override fun onCreate() {
        super.onCreate()
    }
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle configuration changes including locale changes
        if (::localeManager.isInitialized) {
            localeManager.applyLocaleToContext(this)
        }
    }
}