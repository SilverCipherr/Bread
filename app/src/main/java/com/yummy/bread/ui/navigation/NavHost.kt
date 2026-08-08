package com.yummy.bread.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                    val startRoute = if (viewModel.uiState.value.userName.isNotBlank()) Screen.Dashboard.route else Screen.ProfileSetup.route
                    navController.navigate(startRoute) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
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
            composable(Screen.AddTransaction.route) {
                AddTransactionScreen(viewModel) {
                    navController.popBackStack()
                }
            }
            composable(Screen.ProfileSetup.route) {
                ProfileSetupScreen(viewModel) {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                    }
                }
            }
        }
    }
}
