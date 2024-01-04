package com.example.plantcare.data.model

import java.time.LocalTime

data class UserPlantDto(
    val apiPlantId: String,
    val scientificName: String?,
    val customName: String?,
    val pictureUrl: String?,
    val notificationsEnabled: Boolean?,
    val notificationTime: LocalTime?,
    val wateringFrequency: Int, // Frequency in days
    val frequencyUnit: String // "days", "weeks", "months"
)

