package com.sai.cryptotracker.domain.model

data class SearchResult(
    val id: String,
    val name: String,
    val symbol: String,
    val marketCapRank: Int?,
    val thumb: String?
)
