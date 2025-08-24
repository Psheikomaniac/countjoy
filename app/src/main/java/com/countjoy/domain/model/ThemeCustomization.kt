package com.countjoy.domain.model

import androidx.compose.ui.graphics.Color

data class CustomTheme(
    val id: String,
    val name: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val tertiaryColor: Color,
    val backgroundColor: Color,
    val surfaceColor: Color,
    val errorColor: Color,
    val onPrimaryColor: Color,
    val onSecondaryColor: Color,
    val onBackgroundColor: Color,
    val onSurfaceColor: Color,
    val onErrorColor: Color,
    val isDark: Boolean,
    val isSystemTheme: Boolean = false,
    val isMaterialYou: Boolean = false
)

data class ThemeSettings(
    val currentTheme: CustomTheme,
    val fontSize: FontSize = FontSize.MEDIUM,
    val displayDensity: DisplayDensity = DisplayDensity.COMFORTABLE,
    val enableHighContrast: Boolean = false,
    val useMaterialYou: Boolean = false,
    val perEventThemes: Map<String, CustomTheme> = emptyMap()
)

enum class FontSize(val scale: Float) {
    EXTRA_SMALL(0.85f),
    SMALL(0.9f),
    MEDIUM(1.0f),
    LARGE(1.1f),
    EXTRA_LARGE(1.2f)
}

enum class DisplayDensity(val padding: Float, val spacing: Float) {
    COMPACT(0.75f, 0.8f),
    COMFORTABLE(1.0f, 1.0f),
    SPACIOUS(1.25f, 1.2f)
}

object PresetThemes {
    val themes = listOf(
        CustomTheme(
            id = "default_light",
            name = "Default Light",
            primaryColor = Color(0xFF6200EE),
            secondaryColor = Color(0xFF03DAC6),
            tertiaryColor = Color(0xFF3700B3),
            backgroundColor = Color(0xFFF5F5F5),
            surfaceColor = Color.White,
            errorColor = Color(0xFFB00020),
            onPrimaryColor = Color.White,
            onSecondaryColor = Color.Black,
            onBackgroundColor = Color.Black,
            onSurfaceColor = Color.Black,
            onErrorColor = Color.White,
            isDark = false,
            isSystemTheme = true
        ),
        CustomTheme(
            id = "default_dark",
            name = "Default Dark",
            primaryColor = Color(0xFFBB86FC),
            secondaryColor = Color(0xFF03DAC6),
            tertiaryColor = Color(0xFF3700B3),
            backgroundColor = Color(0xFF121212),
            surfaceColor = Color(0xFF1E1E1E),
            errorColor = Color(0xFFCF6679),
            onPrimaryColor = Color.Black,
            onSecondaryColor = Color.Black,
            onBackgroundColor = Color.White,
            onSurfaceColor = Color.White,
            onErrorColor = Color.Black,
            isDark = true,
            isSystemTheme = true
        ),
        CustomTheme(
            id = "ocean_breeze",
            name = "Ocean Breeze",
            primaryColor = Color(0xFF006064),
            secondaryColor = Color(0xFF00ACC1),
            tertiaryColor = Color(0xFF004D40),
            backgroundColor = Color(0xFFE0F7FA),
            surfaceColor = Color(0xFFB2EBF2),
            errorColor = Color(0xFFD32F2F),
            onPrimaryColor = Color.White,
            onSecondaryColor = Color.White,
            onBackgroundColor = Color.Black,
            onSurfaceColor = Color.Black,
            onErrorColor = Color.White,
            isDark = false
        ),
        CustomTheme(
            id = "sunset_glow",
            name = "Sunset Glow",
            primaryColor = Color(0xFFFF6B35),
            secondaryColor = Color(0xFFF77737),
            tertiaryColor = Color(0xFFC44536),
            backgroundColor = Color(0xFFFFF3E0),
            surfaceColor = Color(0xFFFFE0B2),
            errorColor = Color(0xFF8B0000),
            onPrimaryColor = Color.White,
            onSecondaryColor = Color.White,
            onBackgroundColor = Color.Black,
            onSurfaceColor = Color.Black,
            onErrorColor = Color.White,
            isDark = false
        ),
        CustomTheme(
            id = "midnight_purple",
            name = "Midnight Purple",
            primaryColor = Color(0xFF9C27B0),
            secondaryColor = Color(0xFFE91E63),
            tertiaryColor = Color(0xFF673AB7),
            backgroundColor = Color(0xFF1A0033),
            surfaceColor = Color(0xFF2E1A47),
            errorColor = Color(0xFFFF5252),
            onPrimaryColor = Color.White,
            onSecondaryColor = Color.White,
            onBackgroundColor = Color.White,
            onSurfaceColor = Color.White,
            onErrorColor = Color.Black,
            isDark = true
        ),
        CustomTheme(
            id = "forest_green",
            name = "Forest Green",
            primaryColor = Color(0xFF2E7D32),
            secondaryColor = Color(0xFF66BB6A),
            tertiaryColor = Color(0xFF1B5E20),
            backgroundColor = Color(0xFFE8F5E9),
            surfaceColor = Color(0xFFC8E6C9),
            errorColor = Color(0xFFB71C1C),
            onPrimaryColor = Color.White,
            onSecondaryColor = Color.Black,
            onBackgroundColor = Color.Black,
            onSurfaceColor = Color.Black,
            onErrorColor = Color.White,
            isDark = false
        )
    )
}

data class ColorHarmony(
    val baseColor: Color,
    val complementary: Color,
    val analogous1: Color,
    val analogous2: Color,
    val triadic1: Color,
    val triadic2: Color
)