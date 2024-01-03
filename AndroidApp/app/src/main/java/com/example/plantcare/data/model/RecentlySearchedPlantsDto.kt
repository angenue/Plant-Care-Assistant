package com.example.plantcare.data.model

import java.time.LocalDateTime

data class RecentlySearchedPlantsDto (
    val apiPlantId: String,
    val plantName: String,
    val plantImageUrl: String,
    val timestamp: String
)