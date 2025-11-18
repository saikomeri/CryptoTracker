package com.sai.cryptotracker.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinMarketDto(
    @Json(name = "id") val id: String,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "name") val name: String,
    @Json(name = "image") val image: String,
    @Json(name = "current_price") val currentPrice: Double?,
    @Json(name = "market_cap") val marketCap: Long?,
    @Json(name = "market_cap_rank") val marketCapRank: Int?,
    @Json(name = "price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @Json(name = "high_24h") val high24h: Double?,
    @Json(name = "low_24h") val low24h: Double?,
    @Json(name = "total_volume") val totalVolume: Double?,
    @Json(name = "circulating_supply") val circulatingSupply: Double?,
    @Json(name = "sparkline_in_7d") val sparklineIn7d: SparklineDto?
)

@JsonClass(generateAdapter = true)
data class SparklineDto(
    @Json(name = "price") val price: List<Double>?
)
