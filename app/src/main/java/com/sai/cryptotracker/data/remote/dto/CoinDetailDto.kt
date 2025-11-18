package com.sai.cryptotracker.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinDetailDto(
    @Json(name = "id") val id: String,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: DescriptionDto?,
    @Json(name = "image") val image: ImageDto?,
    @Json(name = "market_data") val marketData: MarketDataDto?
)

@JsonClass(generateAdapter = true)
data class DescriptionDto(
    @Json(name = "en") val en: String?
)

@JsonClass(generateAdapter = true)
data class ImageDto(
    @Json(name = "large") val large: String?,
    @Json(name = "small") val small: String?
)

@JsonClass(generateAdapter = true)
data class MarketDataDto(
    @Json(name = "current_price") val currentPrice: Map<String, Double>?,
    @Json(name = "market_cap") val marketCap: Map<String, Long>?,
    @Json(name = "total_volume") val totalVolume: Map<String, Double>?,
    @Json(name = "high_24h") val high24h: Map<String, Double>?,
    @Json(name = "low_24h") val low24h: Map<String, Double>?,
    @Json(name = "price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @Json(name = "circulating_supply") val circulatingSupply: Double?,
    @Json(name = "total_supply") val totalSupply: Double?,
    @Json(name = "ath") val ath: Map<String, Double>?,
    @Json(name = "atl") val atl: Map<String, Double>?
)
