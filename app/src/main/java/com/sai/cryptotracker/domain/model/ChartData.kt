package com.sai.cryptotracker.domain.model

data class ChartData(
    val prices: List<ChartPoint>
)

data class ChartPoint(
    val timestamp: Long,
    val price: Double
)
