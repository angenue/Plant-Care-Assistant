package com.example.plantcare.data.network

import com.example.plantcare.data.model.User
import com.example.plantcare.data.model.UserDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {
    @POST("/api/users/register")
    fun registerUser(@Body userDto: UserDto): Call<User>

    @GET("/api/auth/login")
    fun loginUser(@Header("Authorization") authHeader: String): Call<ResponseBody>

}