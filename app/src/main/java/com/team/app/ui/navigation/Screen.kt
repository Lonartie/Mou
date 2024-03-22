package com.team.app.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String
) {
    data object Home : Screen("home")

    data object Shop : Screen(route = "shop")
}