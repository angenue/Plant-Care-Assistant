package com.example.plantcare.data.model

data class UserDto(
    val email: String,
    val passwordHash: String,
    val confirmPasswordHash: String
)
