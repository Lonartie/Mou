package com.team.app.data.network.interceptors

import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class RateLimitInterceptor : Interceptor {
    companion object {
        private const val RATE_LIMIT_PER_MINUTE = 8
        private var lastRequests = mutableListOf<Long>()
    }


    private fun clearOldRequests() {
        val currentTime = System.currentTimeMillis()
        synchronized(lastRequests) {
            lastRequests = lastRequests.filter { currentTime - it < 60_000 }.toMutableList()
        }
    }

    private fun getDelay(): Long {
        clearOldRequests()
        synchronized(lastRequests) {
            if (lastRequests.size < RATE_LIMIT_PER_MINUTE) {
                return 0
            }
            return 60_000 - (System.currentTimeMillis() - lastRequests.first()) + 1_000
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val delay = getDelay()

        if (delay > 0) {
            Thread.sleep(delay)
        }

        var response: Response? = null
        var responseCode: Int? = null
        try {
            response = chain.proceed(chain.request())
            synchronized(lastRequests) {
                lastRequests.add(System.currentTimeMillis())
            }
            val body = response.body?.string()
            response = response.newBuilder()
                .body(body?.toResponseBody("application/json; charset=utf-8".toMediaType())).build()
            Moshi.Builder().build().adapter(Map::class.java)
                .fromJson(body ?: "")?.let {
                    responseCode = (it["code"] as Double?)?.toInt()
                }
        } catch (_: Exception) {
        }

        while (response == null || response.code == 429 || responseCode == 429) {
            response?.close()
            println("Rate limit exceeded, waiting 30 seconds")
            Thread.sleep(30_000)
            try {
                response = chain.proceed(chain.request())
                synchronized(lastRequests) {
                    lastRequests.add(System.currentTimeMillis())
                }
                val body = response.body?.string()
                response = response.newBuilder()
                    .body(body?.toResponseBody("application/json; charset=utf-8".toMediaType()))
                    .build()
                Moshi.Builder().build().adapter(Map::class.java)
                    .fromJson(body ?: "")?.let {
                        responseCode = (it["code"] as Double?)?.toInt()
                    }
            } catch (_: Exception) {
            }
        }

        return response
    }
}