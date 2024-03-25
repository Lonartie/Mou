package com.team.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.team.app.data.model.ItemType
import com.team.app.ui.home.HomePage
import com.team.app.ui.inventory.InventoryScreen
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
            route = Screen.Home.route
        ) {
            HomePage(
                openShop = { navController.navigate(Screen.Shop.route) },
                selectFood = { navController.navigate(Screen.Food.route) },
                selectToy = { navController.navigate(Screen.Toys.route) },
                selectItem = { navController.navigate(Screen.Items.route) }
            )
        }
        composable(route = Screen.Shop.route) {
            ShopScreen(
                onBackClick = { navController.navigateUp() }
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
    }
}