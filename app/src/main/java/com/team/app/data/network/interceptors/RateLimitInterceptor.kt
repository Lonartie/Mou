package com.team.app.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import kotlin.math.max

class RateLimitInterceptor : Interceptor {
    companion object {
        private const val RATE_LIMIT_PER_MINUTE = 8
    }

    private var lastRequestTime = 0L

    private fun getDelay(): Long {
        val currentTime = System.currentTimeMillis()
        val elapsed = currentTime - lastRequestTime
        val delay = 60_000 / RATE_LIMIT_PER_MINUTE
        return max(delay - elapsed, 0)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val delay = getDelay()

//        if (delay > 0) {
//            Thread.sleep(delay)
//        }

        lastRequestTime = System.currentTimeMillis()
        return chain.proceed(chain.request())
    }
}