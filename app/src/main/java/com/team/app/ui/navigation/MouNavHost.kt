package com.team.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.app.data.model.ItemType
import com.team.app.ui.coins.CoinsPage
import com.team.app.ui.home.HomePage
import com.team.app.ui.inventory.InventoryScreen
import com.team.app.ui.investment.InvestmentPage
import com.team.app.ui.investments.InvestmentsPage
import com.team.app.ui.shop.ShopPage
import com.team.app.ui.tic_tac_toe.TicTacToeScreen

@Composable
fun MouNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomePage(
                openShop = { navController.navigate(Screen.Shop.route) },
                openCoins = { navController.navigate(Screen.Coins.route) },
                selectFood = { navController.navigate(Screen.Food.route) },
                selectToy = { navController.navigate(Screen.Toys.route) },
                selectItem = { navController.navigate(Screen.Items.route) }
            )
        }
        composable(route = Screen.Shop.route) {
            ShopPage(
                goBack = { navController.navigateUp() }
            )
        }
        composable(route = Screen.Food.route) {
            InventoryScreen(
                itemTypes = listOf(ItemType.FOOD),
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(route = Screen.Toys.route) {
            InventoryScreen(
                itemTypes = listOf(ItemType.TOY),
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(route = Screen.Items.route) {
            InventoryScreen(
                itemTypes = listOf(ItemType.MEDICINE, ItemType.MISC),
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(route = Screen.Coins.route) {
            CoinsPage(
                goBack = { navController.navigateUp() },
                openInvestments = { navController.navigate(Screen.Investments.route) },
                openTicTacToe = { navController.navigate(Screen.TicTacToe.route) }
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
        composable(route = Screen.TicTacToe.route) {
            TicTacToeScreen(
                goBack = { navController.navigateUp() }
            )
        }
    }
}