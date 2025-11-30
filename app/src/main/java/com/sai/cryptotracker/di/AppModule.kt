package com.sai.cryptotracker.di

import android.content.Context
import androidx.room.Room
import com.sai.cryptotracker.data.local.CryptoDatabase
import com.sai.cryptotracker.data.local.dao.AlertDao
import com.sai.cryptotracker.data.local.dao.CoinDao
import com.sai.cryptotracker.data.local.dao.WatchlistDao
import com.sai.cryptotracker.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CryptoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CryptoDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCoinDao(db: CryptoDatabase): CoinDao = db.coinDao()

    @Provides
    fun provideWatchlistDao(db: CryptoDatabase): WatchlistDao = db.watchlistDao()

    @Provides
    fun provideAlertDao(db: CryptoDatabase): AlertDao = db.alertDao()
}
