package com.team.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.app.ui.coins.CoinsPage
import com.team.app.ui.home.HomePage
import com.team.app.ui.investment.InvestmentPage
import com.team.app.ui.investments.InvestmentsPage
import com.team.app.ui.shop.ShopPage

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
                openShop = { navController.navigate(Screen.Shop.route) },
                openCoins = { navController.navigate(Screen.Coins.route) }
            )
        }
        composable(route = Screen.Shop.route) {
            ShopPage(
                goBack = { navController.navigateUp() }
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
        composable(route = Screen.Coins.route) {
            CoinsPage(
                goBack = { navController.navigateUp() },
                openInvestments = { navController.navigate(Screen.Investments.route) }
            )
        }
        composable(route = Screen.Investments.route) {
            InvestmentsPage(
                goBack = { navController.navigateUp() },
                openInvestment = {
                    symbol ->
                    val route = Screen.Investment.createRoute(symbol)
                    navController.navigate(route)
                }
            )
        }
        composable(
            route = Screen.Investment.route,
            arguments = Screen.Investment.navArguments
        ) {
            val symbol: String = it.arguments?.getString("symbol") ?: ""
            InvestmentPage(
                goBack = { navController.navigateUp() },
                symbol = symbol.replace("&", "/")
            )
        }
    }
}