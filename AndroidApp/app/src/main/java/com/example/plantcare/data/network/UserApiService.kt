package com.example.plantcare.data.network

import com.example.plantcare.data.model.User
import com.example.plantcare.data.model.UserDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("/api/users/register")
    fun registerUser(@Body userDto: UserDto): Call<User>
}