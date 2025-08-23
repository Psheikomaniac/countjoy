package com.countjoy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.countjoy.presentation.countdown.CountdownScreen
import com.countjoy.presentation.event.EventInputScreen
import com.countjoy.presentation.settings.LanguagePickerScreen
import com.countjoy.presentation.settings.SettingsScreen
import com.countjoy.presentation.settings.AccessibilitySettingsScreen

/**
 * Navigation destinations for the CountJoy app
 */
sealed class Screen(val route: String) {
    object Countdown : Screen("countdown")
    object EventInput : Screen("event_input/{eventId}") {
        fun createRoute(eventId: Long? = null): String {
            return if (eventId != null) {
                "event_input/$eventId"
            } else {
                "event_input/new"
            }
        }
    }
    object Settings : Screen("settings")
    object LanguagePicker : Screen("language_picker")
    object AccessibilitySettings : Screen("accessibility_settings")
}

/**
 * Main navigation host for the CountJoy app
 */
@Composable
fun CountJoyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Countdown.route,
    onLanguageChanged: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Countdown Screen - Main screen displaying countdown
        composable(Screen.Countdown.route) {
            CountdownScreen(
                onNavigateToEventInput = { eventId ->
                    navController.navigate(Screen.EventInput.createRoute(eventId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        // Event Input Screen - Create or edit events
        composable(
            route = Screen.EventInput.route,
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toLongOrNull()
            EventInputScreen(
                eventId = eventId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Settings Screen - App settings and preferences
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLanguageClick = {
                    navController.navigate(Screen.LanguagePicker.route)
                },
                onAccessibilityClick = {
                    navController.navigate(Screen.AccessibilitySettings.route)
                }
            )
        }
        
        // Language Picker Screen - Language selection
        composable(Screen.LanguagePicker.route) {
            LanguagePickerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLanguageChanged = onLanguageChanged
            )
        }
        
        // Accessibility Settings Screen
        composable(Screen.AccessibilitySettings.route) {
            AccessibilitySettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRestartRequired = onLanguageChanged // Reuse the same callback for restart
            )
        }
    }
}