package com.sai.cryptotracker.data.repository

import com.sai.cryptotracker.data.local.dao.WatchlistDao
import com.sai.cryptotracker.data.local.entity.WatchlistEntity
import com.sai.cryptotracker.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepositoryImpl @Inject constructor(
    private val watchlistDao: WatchlistDao
) : WatchlistRepository {

    override fun isInWatchlist(coinId: String): Flow<Boolean> {
        return watchlistDao.isInWatchlist(coinId)
    }

    override fun getWatchlistIds(): Flow<List<String>> {
        return watchlistDao.getAllCoinIds()
    }

    override suspend fun addToWatchlist(coinId: String) {
        watchlistDao.insert(WatchlistEntity(coinId = coinId))
    }

    override suspend fun removeFromWatchlist(coinId: String) {
        watchlistDao.delete(coinId)
    }
}
