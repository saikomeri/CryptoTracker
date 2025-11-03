package com.sai.cryptotracker.util

import java.text.NumberFormat
import java.util.Locale

object NumberFormatter {

    fun formatPrice(price: Double): String {
        return when {
            price >= 1.0 -> "$${String.format(Locale.US, "%,.2f", price)}"
            price >= 0.01 -> "$${String.format(Locale.US, "%.4f", price)}"
            else -> "$${String.format(Locale.US, "%.8f", price)}"
        }
    }

    fun formatPercentage(value: Double): String {
        val sign = if (value >= 0) "+" else ""
        return "$sign${String.format(Locale.US, "%.2f", value)}%"
    }

    fun formatMarketCap(value: Long): String {
        return when {
            value >= 1_000_000_000_000 -> "$${String.format(Locale.US, "%.2fT", value / 1_000_000_000_000.0)}"
            value >= 1_000_000_000 -> "$${String.format(Locale.US, "%.2fB", value / 1_000_000_000.0)}"
            value >= 1_000_000 -> "$${String.format(Locale.US, "%.2fM", value / 1_000_000.0)}"
            value >= 1_000 -> "$${String.format(Locale.US, "%.2fK", value / 1_000.0)}"
            else -> "$$value"
        }
    }

    fun formatVolume(value: Double): String {
        return formatMarketCap(value.toLong())
    }

    fun formatSupply(value: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.maximumFractionDigits = 0
        return formatter.format(value)
    }
}
