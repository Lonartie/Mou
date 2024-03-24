package com.team.app.data.network.interceptors

import com.team.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class StockApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val url = originalUrl.newBuilder()
            .addQueryParameter("apikey", BuildConfig.STOCK_API_KEY)
            .build()

        println("StockApiKeyInterceptor: ${url.toUrl()}")

        val request = originalRequest.newBuilder().url(url).build()
        return chain.proceed(request)
    }

}