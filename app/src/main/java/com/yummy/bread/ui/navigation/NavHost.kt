package com.yummy.bread.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yummy.bread.BreadViewModel
import com.yummy.bread.ui.screens.*

@Composable
fun BreadNavHost(
    navController: NavHostController,
    viewModel: BreadViewModel
) {
    MainScreen(navController = navController, viewModel = viewModel) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen {
                    val uiState = viewModel.uiState.value
                    if (uiState.lastActiveProfileId != null && uiState.profiles.any { it.id == uiState.lastActiveProfileId }) {
                        navController.navigate(Screen.Lock.createRoute(uiState.lastActiveProfileId)) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else if (uiState.profiles.isNotEmpty()) {
                        navController.navigate(Screen.ProfileSelector.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.ProfileSetup.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            }
            
            composable(Screen.ProfileSelector.route) {
                ProfileSelectorScreen(
                    viewModel = viewModel,
                    onProfileSelected = { profileId ->
                        navController.navigate(Screen.Lock.createRoute(profileId))
                    },
                    onAddAccount = {
                        viewModel.logout() // Ensure we are in "Create" mode
                        navController.navigate(Screen.ProfileSetup.route)
                    }
                )
            }
            
            composable(
                route = Screen.Lock.route,
                arguments = listOf(navArgument("profileId") { type = NavType.StringType })
            ) { backStackEntry ->
                val profileId = backStackEntry.arguments?.getString("profileId") ?: ""
                SecurityLockScreen(
                    profileId = profileId,
                    viewModel = viewModel,
                    onSuccess = {
                        viewModel.login(profileId)
                        navController.navigate(Screen.Dashboard.route) {
                            // Clear everything up to the splash screen to make Dashboard the new root
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardContent(viewModel, navController)
            }
            composable(Screen.History.route) {
                TransactionHistoryScreen(viewModel, navController)
            }
            composable(Screen.Budget.route) {
                BudgetPlannerScreen(viewModel)
            }
            composable(Screen.Insights.route) {
                AnalyticsScreen(viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateToProfileSetup = { navController.navigate(Screen.ProfileSetup.route) },
                    onNavigateToSecurity = { navController.navigate(Screen.SecurityPrivacy.route) },
                    onNavigateToAbout = { navController.navigate(Screen.About.route) },
                    onLogout = {
                        // Just navigate to the selector, allowing "Back" to work
                        navController.navigate(Screen.ProfileSelector.route)
                    }
                )
            }
            composable(Screen.SecurityPrivacy.route) {
                SecurityPrivacyScreen { navController.popBackStack() }
            }
            composable(Screen.About.route) {
                AboutScreen { navController.popBackStack() }
            }
            composable(Screen.AddTransaction.route) {
                AddTransactionScreen(viewModel) {
                    navController.popBackStack()
                }
            }
            composable(Screen.ProfileSetup.route) {
                ProfileSetupScreen(viewModel) {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        }
    }
}
