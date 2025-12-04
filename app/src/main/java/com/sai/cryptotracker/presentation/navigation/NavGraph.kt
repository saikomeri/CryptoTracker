package com.sai.cryptotracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sai.cryptotracker.presentation.detail.CoinDetailScreen
import com.sai.cryptotracker.presentation.market.MarketScreen
import com.sai.cryptotracker.presentation.search.SearchScreen
import com.sai.cryptotracker.presentation.settings.SettingsScreen
import com.sai.cryptotracker.presentation.watchlist.WatchlistScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Market.route
    ) {
        composable(Screen.Market.route) {
            MarketScreen(
                onCoinClick = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onCoinClick = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                }
            )
        }

        composable(Screen.Watchlist.route) {
            WatchlistScreen(
                onCoinClick = { coinId ->
                    navController.navigate(Screen.CoinDetail.createRoute(coinId))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }

        composable(
            route = Screen.CoinDetail.route,
            arguments = listOf(
                navArgument("coinId") { type = NavType.StringType }
            )
        ) {
            CoinDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
