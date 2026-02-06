// App-wide constants
package com.sai.cryptotracker.util

object Constants {
    const val BASE_URL = "https://api.coingecko.com/api/v3/"
    const val DATABASE_NAME = "crypto_tracker_db"
    const val CACHE_DURATION_MARKET = 5 * 60 * 1000L // 5 minutes
    const val CACHE_DURATION_DETAIL = 15 * 60 * 1000L // 15 minutes
    const val SEARCH_DEBOUNCE_MS = 300L
    const val ALERT_CHECK_INTERVAL_MIN = 15L
}
