package com.team.app.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen(
        route = "home/{foodId}/{toyId}/{itemId}",
        navArguments = listOf(
            navArgument("foodId") { type = NavType.IntType },
            navArgument("toyId") { type = NavType.IntType },
            navArgument("itemId") { type = NavType.IntType }
        )
    ) {
        fun createRoute(
            foodId: Int,
            toyId: Int,
            itemId: Int
        ) = "home/{foodId}/{toyId}/{itemId}"
    }

    data object Shop : Screen(route = "shop")

    data object Food : Screen(route = "food")

    data object Toys : Screen(route = "toys")

    data object Items : Screen(route = "items")

    data object Coins : Screen(route = "coins")

    data object Investments : Screen(route = "investments")

    data object Investment : Screen(
        route = "investment/{symbol}",
        navArguments = listOf(
            navArgument("symbol") { type = NavType.StringType }
        )
    ) {
        fun createRoute(symbol: String) = "investment/${symbol.replace("/", "&")}"
    }
}