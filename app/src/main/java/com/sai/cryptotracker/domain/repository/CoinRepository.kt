package com.sai.cryptotracker.domain.repository

import com.sai.cryptotracker.domain.model.ChartData
import com.sai.cryptotracker.domain.model.Coin
import com.sai.cryptotracker.domain.model.CoinDetail
import com.sai.cryptotracker.domain.model.SearchResult
import com.sai.cryptotracker.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getMarketData(forceRefresh: Boolean = false): Flow<NetworkResult<List<Coin>>>
    suspend fun getCoinDetail(coinId: String): NetworkResult<CoinDetail>
    suspend fun getChartData(coinId: String, days: String): NetworkResult<ChartData>
    suspend fun searchCoins(query: String): NetworkResult<List<SearchResult>>
    fun getCachedCoins(): Flow<List<Coin>>
    fun getWatchlistCoins(): Flow<List<Coin>>
}
