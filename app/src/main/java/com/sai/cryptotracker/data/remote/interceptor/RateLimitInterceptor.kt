package com.sai.cryptotracker.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicLong

class RateLimitInterceptor : Interceptor {

    private val lastRequestTime = AtomicLong(0)
    private val minIntervalMs = 1500L // ~40 requests per minute (well within free tier limits)

    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val now = System.currentTimeMillis()
            val timeSinceLastRequest = now - lastRequestTime.get()
            if (timeSinceLastRequest < minIntervalMs) {
                Thread.sleep(minIntervalMs - timeSinceLastRequest)
            }
            lastRequestTime.set(System.currentTimeMillis())
        }
        return chain.proceed(chain.request())
    }
}
