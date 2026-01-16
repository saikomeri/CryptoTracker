package com.sai.cryptotracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val coinId: String,
    val coinName: String,
    val targetPrice: Double,
    val isAbove: Boolean,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
