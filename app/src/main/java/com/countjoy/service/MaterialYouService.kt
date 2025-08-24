package com.countjoy.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.dynamiccolor.DynamicColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.countjoy.domain.model.CustomTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialYouService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    fun isMaterialYouSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
    
    @RequiresApi(Build.VERSION_CODES.S)
    fun extractDynamicColors(): CustomTheme? {
        if (!isMaterialYouSupported()) return null
        
        return try {
            // Extract colors from the system wallpaper
            val dynamicColors = android.R.color.system_accent1_500
            val primaryColor = context.getColor(dynamicColors)
            
            CustomTheme(
                id = "material_you_dynamic",
                name = "Material You",
                primaryColor = Color(primaryColor),
                secondaryColor = Color(context.getColor(android.R.color.system_accent2_500)),
                tertiaryColor = Color(context.getColor(android.R.color.system_accent3_500)),
                backgroundColor = Color(context.getColor(android.R.color.system_neutral1_50)),
                surfaceColor = Color(context.getColor(android.R.color.system_neutral1_100)),
                errorColor = Color(context.getColor(android.R.color.system_error_500)),
                onPrimaryColor = Color(context.getColor(android.R.color.system_accent1_0)),
                onSecondaryColor = Color(context.getColor(android.R.color.system_accent2_0)),
                onBackgroundColor = Color(context.getColor(android.R.color.system_neutral1_900)),
                onSurfaceColor = Color(context.getColor(android.R.color.system_neutral1_900)),
                onErrorColor = Color(context.getColor(android.R.color.system_error_0)),
                isDark = false,
                isMaterialYou = true
            )
        } catch (e: Exception) {
            null
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.S)
    fun extractDarkDynamicColors(): CustomTheme? {
        if (!isMaterialYouSupported()) return null
        
        return try {
            CustomTheme(
                id = "material_you_dynamic_dark",
                name = "Material You Dark",
                primaryColor = Color(context.getColor(android.R.color.system_accent1_200)),
                secondaryColor = Color(context.getColor(android.R.color.system_accent2_200)),
                tertiaryColor = Color(context.getColor(android.R.color.system_accent3_200)),
                backgroundColor = Color(context.getColor(android.R.color.system_neutral1_900)),
                surfaceColor = Color(context.getColor(android.R.color.system_neutral1_800)),
                errorColor = Color(context.getColor(android.R.color.system_error_200)),
                onPrimaryColor = Color(context.getColor(android.R.color.system_accent1_800)),
                onSecondaryColor = Color(context.getColor(android.R.color.system_accent2_800)),
                onBackgroundColor = Color(context.getColor(android.R.color.system_neutral1_50)),
                onSurfaceColor = Color(context.getColor(android.R.color.system_neutral1_50)),
                onErrorColor = Color(context.getColor(android.R.color.system_error_800)),
                isDark = true,
                isMaterialYou = true
            )
        } catch (e: Exception) {
            null
        }
    }
}