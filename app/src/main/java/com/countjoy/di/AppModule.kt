package com.countjoy.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.countjoy.core.accessibility.AccessibilityManager
import com.countjoy.data.local.preferences.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("countjoy_prefs", Context.MODE_PRIVATE)
    }
    
    @Provides
    @Singleton
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }
    
    @Provides
    @Singleton
    fun provideAccessibilityManager(
        @ApplicationContext context: Context,
        preferencesManager: SharedPreferencesManager
    ): AccessibilityManager {
        return AccessibilityManager(context, preferencesManager)
    }
}