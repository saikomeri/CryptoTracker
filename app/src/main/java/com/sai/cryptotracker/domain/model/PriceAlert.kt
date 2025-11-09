package com.sai.cryptotracker.domain.model

data class PriceAlert(
    val id: Long = 0,
    val coinId: String,
    val coinName: String,
    val targetPrice: Double,
    val isAbove: Boolean,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
