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
        composable(route = Screen.Home.route) {
            HomePage(
                onShopClick = { navController.navigate(Screen.Shop.route) }
            )
        }
        composable(route = Screen.Shop.route) {
            ShopScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}