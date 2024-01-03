package com.example.plantcare.util

import com.example.plantcare.data.network.PlantService
import com.example.plantcare.data.network.UserApiService
import com.example.plantcare.data.network.UserPlantService
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService(sessionManager: SessionManager) {

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(sessionManager))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userApi: UserApiService = retrofit.create(UserApiService::class.java)
    val userPlantApi: UserPlantService by lazy { retrofit.create(UserPlantService::class.java) }
    val plantApi: PlantService by lazy {
        retrofit.create(PlantService::class.java)
    }
}