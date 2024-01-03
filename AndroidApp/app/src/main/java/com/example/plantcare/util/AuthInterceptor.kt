package com.example.plantcare.util

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authHeader = sessionManager.getAuthHeader()

        val newRequest = originalRequest.newBuilder()
            .apply {
                if (authHeader != null) {
                    addHeader("Authorization", authHeader)
                }
            }
            .build()

        return chain.proceed(newRequest)
    }
}
