package com.team.app.ui.navigation

sealed class Screen(
    val route: String
) {
    data object Home : Screen(route = "home")

    data object Shop : Screen(route = "shop")

    data object Food : Screen(route = "food")

    data object Toys : Screen(route = "toys")

    data object Items : Screen(route = "items")
}