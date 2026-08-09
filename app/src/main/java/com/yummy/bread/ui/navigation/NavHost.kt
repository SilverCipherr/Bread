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
                        navController.navigate(Screen.ProfileSetup.createRoute(true)) {
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
                        navController.navigate(Screen.ProfileSetup.createRoute(true))
                    },
                    onEmptyProfiles = {
                        navController.navigate(Screen.ProfileSetup.createRoute(true)) {
                            popUpTo(Screen.ProfileSelector.route) { inclusive = true }
                        }
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
                    onNavigateToProfileSetup = { navController.navigate(Screen.ProfileSetup.createRoute(false)) },
                    onNavigateToAddAccount = { navController.navigate(Screen.ProfileSetup.createRoute(true)) },
                    onNavigateToSecurity = { navController.navigate(Screen.SecurityPrivacy.route) },
                    onNavigateToAbout = { navController.navigate(Screen.About.route) },
                    onLogout = {
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
            composable(
                route = Screen.ProfileSetup.route,
                arguments = listOf(navArgument("isNew") { type = NavType.BoolType; defaultValue = false })
            ) { backStackEntry ->
                val isNew = backStackEntry.arguments?.getBoolean("isNew") ?: false
                ProfileSetupScreen(viewModel, isNew) {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        }
    }
}
