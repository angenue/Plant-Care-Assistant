package com.example.plantcare.data.network

import com.example.plantcare.data.model.ImageRequest
import com.example.plantcare.data.model.Plant
import com.example.plantcare.data.model.RecentlySearchedPlantsDto
import com.example.plantcare.data.model.SimplePlant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlantService {

    @GET("api/plants/search")
    suspend fun searchPlants(@Query("query") query: String): Response<List<SimplePlant>>

    @GET("api/plants/details/{plantId}")
    suspend fun getPlantDetails(@Path("plantId") plantId: String): Response<Plant>

    @GET("api/plants/recently-searched")
    suspend fun getRecentlySearchedPlants(): Response<List<RecentlySearchedPlantsDto>>

    @POST("api/plants/identify-from-image")
    suspend fun identifyPlantFromImage(@Body imageRequest: ImageRequest): Response<Plant>
}
