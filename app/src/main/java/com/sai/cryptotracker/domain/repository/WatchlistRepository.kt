package com.sai.cryptotracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    fun isInWatchlist(coinId: String): Flow<Boolean>
    fun getWatchlistIds(): Flow<List<String>>
    suspend fun addToWatchlist(coinId: String)
    suspend fun removeFromWatchlist(coinId: String)
}
