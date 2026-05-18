package com.turkcell.data.network

import com.turkcell.data.util.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenStore.getAccessToken()

        val requestBuilder = originalRequest.newBuilder()

        // Eğer cihazda kayıtlı bir token varsa header'a ekle
        if (!accessToken.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(requestBuilder.build())
    }
}