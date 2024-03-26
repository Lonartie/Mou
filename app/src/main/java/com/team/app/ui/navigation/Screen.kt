package com.team.app.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen(route = "home")

    data object Shop : Screen(route = "shop")

    data object Food : Screen(route = "food")

    data object Toys : Screen(route = "toys")

    data object Items : Screen(route = "items")

    data object Coins : Screen(route = "coins")

    data object Investments : Screen(route = "investments")

    data object StepsOverview : Screen(route = "steps-overview")
    data object Investment : Screen(
        route = "investment/{symbol}",
        navArguments = listOf(
            navArgument("symbol") { type = NavType.StringType }
        )
    ) {
        fun createRoute(symbol: String) = "investment/${symbol.replace("/", "&")}"
    }
}