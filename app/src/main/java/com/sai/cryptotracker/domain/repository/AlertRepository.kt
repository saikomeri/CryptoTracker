package com.sai.cryptotracker.domain.repository

import com.sai.cryptotracker.domain.model.PriceAlert
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun getAllAlerts(): Flow<List<PriceAlert>>
    suspend fun getActiveAlerts(): List<PriceAlert>
    suspend fun addAlert(alert: PriceAlert): Long
    suspend fun deactivateAlert(id: Long)
    suspend fun deleteAlert(id: Long)
}
