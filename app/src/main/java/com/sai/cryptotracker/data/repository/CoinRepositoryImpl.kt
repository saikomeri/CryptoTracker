package com.sai.cryptotracker.data.repository

import com.sai.cryptotracker.data.local.dao.CoinDao
import com.sai.cryptotracker.data.local.dao.WatchlistDao
import com.sai.cryptotracker.data.mapper.toDomain
import com.sai.cryptotracker.data.mapper.toDomainList
import com.sai.cryptotracker.data.mapper.toEntity
import com.sai.cryptotracker.data.remote.CoinGeckoApi
import com.sai.cryptotracker.domain.model.ChartData
import com.sai.cryptotracker.domain.model.Coin
import com.sai.cryptotracker.domain.model.CoinDetail
import com.sai.cryptotracker.domain.model.SearchResult
import com.sai.cryptotracker.domain.repository.CoinRepository
import com.sai.cryptotracker.util.Constants
import com.sai.cryptotracker.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi,
    private val coinDao: CoinDao,
    private val watchlistDao: WatchlistDao
) : CoinRepository {

    override fun getMarketData(forceRefresh: Boolean): Flow<NetworkResult<List<Coin>>> = flow {
        emit(NetworkResult.Loading)

        val lastUpdate = coinDao.getLastUpdateTime()
        val isCacheValid = lastUpdate != null &&
                (System.currentTimeMillis() - lastUpdate) < Constants.CACHE_DURATION_MARKET

        if (!forceRefresh && isCacheValid) {
            // Return cached data
            val cached = coinDao.getAllCoins()
            cached.collect { entities ->
                emit(NetworkResult.Success(entities.toDomainList()))
            }
            return@flow
        }

        try {
            val response = api.getMarketData()
            val entities = response.map { it.toEntity() }
            coinDao.deleteAll()
            coinDao.insertAll(entities)
            val coins = response.map { it.toDomain() }
            emit(NetworkResult.Success(coins))
        } catch (e: Exception) {
            // Try returning cached data on error
            val cached = coinDao.getLastUpdateTime()
            if (cached != null) {
                coinDao.getAllCoins().collect { entities ->
                    if (entities.isNotEmpty()) {
                        emit(NetworkResult.Success(entities.toDomainList()))
                    } else {
                        emit(NetworkResult.Error(e.message ?: "Unknown error"))
                    }
                }
            } else {
                emit(NetworkResult.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun getCoinDetail(coinId: String): NetworkResult<CoinDetail> {
        return try {
            val response = api.getCoinDetail(coinId)
            NetworkResult.Success(response.toDomain())
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Failed to fetch coin details")
        }
    }

    override suspend fun getChartData(coinId: String, days: String): NetworkResult<ChartData> {
        return try {
            val response = api.getMarketChart(coinId, days = days)
            val chartData = com.sai.cryptotracker.data.mapper.toDomain(response)
            NetworkResult.Success(chartData)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Failed to fetch chart data")
        }
    }

    override suspend fun searchCoins(query: String): NetworkResult<List<SearchResult>> {
        return try {
            val response = api.searchCoins(query)
            val results = response.coins?.map { it.toDomain() } ?: emptyList()
            NetworkResult.Success(results)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Search failed")
        }
    }

    override fun getCachedCoins(): Flow<List<Coin>> {
        return coinDao.getAllCoins().map { it.toDomainList() }
    }

    override fun getWatchlistCoins(): Flow<List<Coin>> {
        return watchlistDao.getAllCoinIds().map { ids ->
            if (ids.isEmpty()) emptyList()
            else {
                // We need to collect the inner flow; use a simpler approach
                val coins = mutableListOf<Coin>()
                ids.forEach { id ->
                    coinDao.getCoinById(id)?.let { coins.add(it.toDomain()) }
                }
                coins
            }
        }
    }
}

private fun com.sai.cryptotracker.data.mapper.toDomain(dto: com.sai.cryptotracker.data.remote.dto.MarketChartDto): ChartData {
    val points = dto.prices?.map { point ->
        com.sai.cryptotracker.domain.model.ChartPoint(
            timestamp = point[0].toLong(),
            price = point[1]
        )
    } ?: emptyList()
    return ChartData(prices = points)
}
