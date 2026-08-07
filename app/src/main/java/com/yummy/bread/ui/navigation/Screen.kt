package com.yummy.bread.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object ProfileSetup : Screen("profile_setup")
    object Dashboard : Screen("home")
    object History : Screen("history")
    object Budget : Screen("budget")
    object Insights : Screen("insights")
    object AddTransaction : Screen("add_transaction")
}
