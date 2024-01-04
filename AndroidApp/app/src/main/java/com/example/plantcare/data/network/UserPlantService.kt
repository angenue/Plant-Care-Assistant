package com.example.plantcare.data.network

import com.example.plantcare.data.model.CombinedPlantDto
import com.example.plantcare.data.model.UserPlant
import com.example.plantcare.data.model.UserPlantDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

interface UserPlantService {
    @POST("/api/userplants")
    suspend fun addUserPlant(@Body userPlantDto: UserPlantDto): Response<UserPlant>

    // Endpoint to get all saved UserPlants for the current user
    @GET("/api/userplants/yourPlants")
    suspend fun getAllSavedPlants(): Response<List<UserPlant>>

    // Endpoint to get a specific UserPlant by ID
    @GET("/api/userplants/{userPlantId}")
    suspend fun getUserPlant(@Path("userPlantId") userPlantId: Long): Response<CombinedPlantDto>

    // Endpoint to add a watering event to a specific UserPlant
    /*@POST("/api/userplants/{userPlantId}/watering")
    suspend fun addWateringEvent(@Path("userPlantId") userPlantId: Long, @Body wateringEventDto: WateringEventDto): Response<PlantWateringHistory>
*/

    // Endpoint to get the watering log for a specific UserPlant
    @GET("/api/userplants/{userPlantId}/watering-log")
    suspend fun getWateringLog(@Path("userPlantId") userPlantId: Long): Response<Map<String, Any>>

    // Endpoint to update a specific watering log entry
    /*@PUT("/api/userplants/watering-logs/{logId}")
    suspend fun updateWateringLog(@Path("logId") logId: Long, @Body newWateringDateTime: LocalDateTime): Response<PlantWateringHistory>
*/

    // Endpoint to delete a specific watering history entry
    @DELETE("/api/userplants/watering-logs/{logId}")
    suspend fun deleteWateringHistory(@Path("logId") logId: Long): Response<Unit>

    // Endpoint to toggle notifications for a specific UserPlant
    @POST("/api/userplants/{userPlantId}/notifications")
    suspend fun toggleNotifications(@Path("userPlantId") userPlantId: Long, @Query("enable") enable: Boolean): Response<UserPlant>

    // Endpoint to update notification settings for a specific UserPlant
    @PUT("/api/userplants/{userPlantId}/notification-settings")
    suspend fun updateNotificationSettings(@Path("userPlantId") userPlantId: Long, @Body userPlantDto: UserPlantDto): Response<UserPlant>

    // Endpoint to update the picture of a specific UserPlant
    @PUT("/api/userplants/{userPlantId}/picture")
    suspend fun updateUserPlantPicture(@Path("userPlantId") userPlantId: Long, @Body newPicture: String): Response<UserPlant>

    // Endpoint to update the name of a specific UserPlant
    @PUT("/api/userplants/{userPlantId}/name")
    suspend fun updatePlantName(@Path("userPlantId") userPlantId: Long, @Body newName: String): Response<UserPlant>

    // Endpoint to delete a specific UserPlant
    @DELETE("/api/userplants/{userPlantId}")
    suspend fun deleteUserPlant(@Path("userPlantId") userPlantId: Long): Response<Unit>

}