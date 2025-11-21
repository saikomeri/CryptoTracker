package com.sai.cryptotracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val marketCap: Long,
    val marketCapRank: Int,
    val priceChangePercent24h: Double,
    val high24h: Double,
    val low24h: Double,
    val totalVolume: Double,
    val circulatingSupply: Double,
    val sparklineData: String = "", // JSON array of prices
    val lastUpdated: Long = System.currentTimeMillis()
)
