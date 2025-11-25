package com.sai.cryptotracker.data.mapper

import com.sai.cryptotracker.data.remote.dto.MarketChartDto
import com.sai.cryptotracker.domain.model.ChartData
import com.sai.cryptotracker.domain.model.ChartPoint

fun MarketChartDto.toDomain(): ChartData {
    val points = prices?.map { point ->
        ChartPoint(
            timestamp = point[0].toLong(),
            price = point[1]
        )
    } ?: emptyList()
    return ChartData(prices = points)
}
