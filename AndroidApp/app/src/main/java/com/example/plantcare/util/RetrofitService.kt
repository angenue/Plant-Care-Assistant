package com.example.plantcare.util

import com.example.plantcare.data.network.UserApiService
import com.example.plantcare.data.network.UserPlantService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApi: UserApiService = retrofit.create(UserApiService::class.java)
    val userPlantApi: UserPlantService = retrofit.create(UserPlantService::class.java)
}