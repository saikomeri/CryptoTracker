package com.sai.cryptotracker.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sai.cryptotracker.CryptoTrackerApplication
import com.sai.cryptotracker.R
import com.sai.cryptotracker.data.remote.CoinGeckoApi
import com.sai.cryptotracker.domain.repository.AlertRepository
import com.sai.cryptotracker.presentation.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PriceAlertWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val api: CoinGeckoApi,
    private val alertRepository: AlertRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val activeAlerts = alertRepository.getActiveAlerts()
            if (activeAlerts.isEmpty()) return Result.success()

            // Get unique coin IDs
            val coinIds = activeAlerts.map { it.coinId }.distinct()

            // Fetch current prices
            val marketData = api.getMarketData(perPage = 250)
            val priceMap = marketData.associate { it.id to (it.currentPrice ?: 0.0) }

            // Check each alert
            activeAlerts.forEach { alert ->
                val currentPrice = priceMap[alert.coinId] ?: return@forEach
                val triggered = if (alert.isAbove) {
                    currentPrice >= alert.targetPrice
                } else {
                    currentPrice <= alert.targetPrice
                }

                if (triggered) {
                    sendNotification(
                        title = "${alert.coinName} Price Alert",
                        message = "${alert.coinName} is now $${String.format("%.2f", currentPrice)} " +
                                "(target: $${String.format("%.2f", alert.targetPrice)})"
                    )
                    alertRepository.deactivateAlert(alert.id)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun sendNotification(title: String, message: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            CryptoTrackerApplication.ALERT_CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val WORK_NAME = "price_alert_checker"
    }
}
