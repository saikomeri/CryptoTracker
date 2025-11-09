package com.sai.cryptotracker.domain.model

data class Coin(
    val id: String,
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
    val sparklineData: List<Double> = emptyList()
)
