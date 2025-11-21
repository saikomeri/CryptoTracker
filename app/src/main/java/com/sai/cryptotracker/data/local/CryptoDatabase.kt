package com.sai.cryptotracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sai.cryptotracker.data.local.dao.AlertDao
import com.sai.cryptotracker.data.local.dao.CoinDao
import com.sai.cryptotracker.data.local.dao.WatchlistDao
import com.sai.cryptotracker.data.local.entity.AlertEntity
import com.sai.cryptotracker.data.local.entity.CoinEntity
import com.sai.cryptotracker.data.local.entity.SearchHistoryEntity
import com.sai.cryptotracker.data.local.entity.WatchlistEntity

@Database(
    entities = [
        CoinEntity::class,
        WatchlistEntity::class,
        AlertEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun alertDao(): AlertDao
}
