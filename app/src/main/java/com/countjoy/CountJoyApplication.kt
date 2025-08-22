package com.countjoy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CountJoyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}