package com.sai.cryptotracker.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarketChartDto(
    @Json(name = "prices") val prices: List<List<Double>>?
)
