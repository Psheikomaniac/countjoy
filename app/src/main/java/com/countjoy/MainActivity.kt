package com.countjoy

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.countjoy.core.locale.LocaleManager
import com.countjoy.core.accessibility.AccessibilityManager
import com.countjoy.data.local.preferences.SharedPreferencesManager
import com.countjoy.navigation.CountJoyNavHost
import com.countjoy.ui.theme.CountJoyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var preferencesManager: SharedPreferencesManager
    
    @Inject
    lateinit var localeManager: LocaleManager
    
    @Inject
    lateinit var accessibilityManager: AccessibilityManager
    
    override fun attachBaseContext(newBase: Context) {
        // Apply the saved locale before the activity is created
        val localeManager = LocaleManager(newBase, SharedPreferencesManager(newBase))
        super.attachBaseContext(localeManager.applyLocaleToContext(newBase))
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountJoyTheme(
                preferencesManager = preferencesManager,
                accessibilityManager = accessibilityManager
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CountJoyNavHost(onLanguageChanged = {
                        // Recreate activity to apply new locale
                        recreate()
                    })
                }
            }
        }
    }
    
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle configuration changes like locale changes
        localeManager.applyLocaleToContext(this)
    }
}