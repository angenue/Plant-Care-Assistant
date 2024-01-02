package com.example.plantcare.data.network

import com.example.plantcare.data.model.CombinedPlantDto
import com.example.plantcare.data.model.UserPlant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserPlantService {
    @GET("/api/userplants/yourPlants")
    suspend fun getAllSavedPlants(): Response<List<UserPlant>>

    @GET("/api/userplants/{userPlantId}")
    suspend fun getUserPlant(@Path("userPlantId") userPlantId: Long): Response<CombinedPlantDto>

    // ... other endpoints
}