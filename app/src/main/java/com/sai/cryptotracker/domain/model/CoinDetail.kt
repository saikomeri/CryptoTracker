package com.sai.cryptotracker.domain.model

data class CoinDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val description: String,
    val currentPrice: Double,
    val marketCap: Long,
    val totalVolume: Double,
    val high24h: Double,
    val low24h: Double,
    val priceChangePercent24h: Double,
    val circulatingSupply: Double,
    val totalSupply: Double,
    val ath: Double,
    val atl: Double
)
