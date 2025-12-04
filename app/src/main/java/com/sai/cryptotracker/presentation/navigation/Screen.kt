package com.sai.cryptotracker.presentation.navigation

sealed class Screen(val route: String) {
    data object Market : Screen("market")
    data object Search : Screen("search")
    data object Watchlist : Screen("watchlist")
    data object Settings : Screen("settings")
    data object CoinDetail : Screen("coin_detail/{coinId}") {
        fun createRoute(coinId: String) = "coin_detail/$coinId"
    }
}
