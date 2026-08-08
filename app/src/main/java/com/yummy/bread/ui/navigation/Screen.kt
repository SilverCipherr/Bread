package com.yummy.bread.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object ProfileSetup : Screen("profile_setup?isNew={isNew}") {
        fun createRoute(isNew: Boolean = false) = "profile_setup?isNew=$isNew"
    }
    object Dashboard : Screen("home")
    object History : Screen("history")
    object Budget : Screen("budget")
    object Insights : Screen("insights")
    object AddTransaction : Screen("add_transaction")
    object Settings : Screen("settings")
    object SecurityPrivacy : Screen("security_privacy")
    object About : Screen("about")
    object Lock : Screen("lock/{profileId}") {
        fun createRoute(profileId: String) = "lock/$profileId"
    }
    object ProfileSelector : Screen("profile_selector")
}
