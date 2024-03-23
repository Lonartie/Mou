package com.team.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.app.ui.home.HomePage
import com.team.app.ui.shop.ShopScreen

@Composable
fun MouNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route,
            arguments = Screen.Home.navArguments
        ) {
            val foodId: Int = it.arguments?.getInt("foodId") ?: -1
            val toyId: Int = it.arguments?.getInt("toyId") ?: -1
            val itemId: Int = it.arguments?.getInt("itemId") ?: -1
            HomePage(
                openShop = { navController.navigate(Screen.Shop.route) }
            )
        }
        composable(route = Screen.Shop.route) {
            ShopScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(route = Screen.Food.route) {
            // TODO add food screen
        }
        composable(route = Screen.Toys.route) {
            // TODO add toys screen
        }
        composable(route = Screen.Items.route) {
            // TODO add items screen
        }
    }
}