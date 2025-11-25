package com.sai.cryptotracker.data.mapper

import com.sai.cryptotracker.data.local.entity.CoinEntity
import com.sai.cryptotracker.data.remote.dto.CoinDetailDto
import com.sai.cryptotracker.data.remote.dto.CoinMarketDto
import com.sai.cryptotracker.data.remote.dto.SearchCoinDto
import com.sai.cryptotracker.domain.model.Coin
import com.sai.cryptotracker.domain.model.CoinDetail
import com.sai.cryptotracker.domain.model.SearchResult

fun CoinMarketDto.toEntity(): CoinEntity {
    return CoinEntity(
        id = id,
        symbol = symbol,
        name = name,
        image = image,
        currentPrice = currentPrice ?: 0.0,
        marketCap = marketCap ?: 0L,
        marketCapRank = marketCapRank ?: 0,
        priceChangePercent24h = priceChangePercentage24h ?: 0.0,
        high24h = high24h ?: 0.0,
        low24h = low24h ?: 0.0,
        totalVolume = totalVolume ?: 0.0,
        circulatingSupply = circulatingSupply ?: 0.0,
        sparklineData = sparklineIn7d?.price?.joinToString(",") ?: "",
        lastUpdated = System.currentTimeMillis()
    )
}

fun CoinEntity.toDomain(): Coin {
    return Coin(
        id = id,
        symbol = symbol,
        name = name,
        image = image,
        currentPrice = currentPrice,
        marketCap = marketCap,
        marketCapRank = marketCapRank,
        priceChangePercent24h = priceChangePercent24h,
        high24h = high24h,
        low24h = low24h,
        totalVolume = totalVolume,
        circulatingSupply = circulatingSupply,
        sparklineData = if (sparklineData.isNotBlank()) {
            sparklineData.split(",").mapNotNull { it.toDoubleOrNull() }
        } else emptyList()
    )
}

fun CoinMarketDto.toDomain(): Coin {
    return Coin(
        id = id,
        symbol = symbol,
        name = name,
        image = image,
        currentPrice = currentPrice ?: 0.0,
        marketCap = marketCap ?: 0L,
        marketCapRank = marketCapRank ?: 0,
        priceChangePercent24h = priceChangePercentage24h ?: 0.0,
        high24h = high24h ?: 0.0,
        low24h = low24h ?: 0.0,
        totalVolume = totalVolume ?: 0.0,
        circulatingSupply = circulatingSupply ?: 0.0,
        sparklineData = sparklineIn7d?.price ?: emptyList()
    )
}

fun CoinDetailDto.toDomain(): CoinDetail {
    return CoinDetail(
        id = id,
        symbol = symbol,
        name = name,
        image = image?.large ?: "",
        description = description?.en ?: "",
        currentPrice = marketData?.currentPrice?.get("usd") ?: 0.0,
        marketCap = marketData?.marketCap?.get("usd") ?: 0L,
        totalVolume = marketData?.totalVolume?.get("usd") ?: 0.0,
        high24h = marketData?.high24h?.get("usd") ?: 0.0,
        low24h = marketData?.low24h?.get("usd") ?: 0.0,
        priceChangePercent24h = marketData?.priceChangePercentage24h ?: 0.0,
        circulatingSupply = marketData?.circulatingSupply ?: 0.0,
        totalSupply = marketData?.totalSupply ?: 0.0,
        ath = marketData?.ath?.get("usd") ?: 0.0,
        atl = marketData?.atl?.get("usd") ?: 0.0
    )
}

fun SearchCoinDto.toDomain(): SearchResult {
    return SearchResult(
        id = id,
        name = name,
        symbol = symbol,
        marketCapRank = marketCapRank,
        thumb = thumb
    )
}

fun List<CoinEntity>.toDomainList(): List<Coin> = map { it.toDomain() }
