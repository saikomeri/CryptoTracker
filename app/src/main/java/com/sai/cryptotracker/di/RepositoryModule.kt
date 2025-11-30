package com.sai.cryptotracker.di

import com.sai.cryptotracker.data.repository.AlertRepositoryImpl
import com.sai.cryptotracker.data.repository.CoinRepositoryImpl
import com.sai.cryptotracker.data.repository.WatchlistRepositoryImpl
import com.sai.cryptotracker.domain.repository.AlertRepository
import com.sai.cryptotracker.domain.repository.CoinRepository
import com.sai.cryptotracker.domain.repository.WatchlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCoinRepository(impl: CoinRepositoryImpl): CoinRepository

    @Binds
    @Singleton
    abstract fun bindWatchlistRepository(impl: WatchlistRepositoryImpl): WatchlistRepository

    @Binds
    @Singleton
    abstract fun bindAlertRepository(impl: AlertRepositoryImpl): AlertRepository
}
