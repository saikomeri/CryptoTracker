package com.sai.cryptotracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sai.cryptotracker.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Query("SELECT * FROM alerts ORDER BY createdAt DESC")
    fun getAll(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts WHERE isActive = 1")
    suspend fun getActiveAlerts(): List<AlertEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: AlertEntity): Long

    @Query("UPDATE alerts SET isActive = 0 WHERE id = :id")
    suspend fun deactivate(id: Long)

    @Query("DELETE FROM alerts WHERE id = :id")
    suspend fun delete(id: Long)
}
