package com.example.plantcare.data.model

import java.time.LocalTime

data class UserPlant(
    val id: Long,
    val apiPlantId: String,
    val scientificName: String?,
    val customName: String?,
    val pictureUrl: String?,
    val notificationsEnabled: Boolean?,
    val notificationTime: LocalTime?,
    val wateringFrequency: Int, // Frequency in days
    val frequencyUnit: String // "days", "weeks", "months"
)

