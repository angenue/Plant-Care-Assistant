package com.example.plantcare.util

import com.example.plantcare.data.network.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private const val BASE_URL = "http://localhost:8080/" // Replace with your API base URL

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApi: UserApiService = retrofit.create(UserApiService::class.java)
}