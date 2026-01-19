package com.sai.cryptotracker.data.repository

import com.sai.cryptotracker.data.local.dao.AlertDao
import com.sai.cryptotracker.data.mapper.toDomain
import com.sai.cryptotracker.data.mapper.toEntity
import com.sai.cryptotracker.domain.model.PriceAlert
import com.sai.cryptotracker.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertRepositoryImpl @Inject constructor(
    private val alertDao: AlertDao
) : AlertRepository {

    override fun getAllAlerts(): Flow<List<PriceAlert>> {
        return alertDao.getAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getActiveAlerts(): List<PriceAlert> {
        return alertDao.getActiveAlerts().map { it.toDomain() }
    }

    override suspend fun addAlert(alert: PriceAlert): Long {
        return alertDao.insert(alert.toEntity())
    }

    override suspend fun deactivateAlert(id: Long) {
        alertDao.deactivate(id)
    }

    override suspend fun deleteAlert(id: Long) {
        alertDao.delete(id)
    }
}
