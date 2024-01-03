package com.example.plantcare.data.model

import java.time.LocalDateTime

data class RecentlySearchedPlants (
    val id: Long?,
    val userId: Long,
    val apiPlantId: String,
    val plantName: String,
    val pictureUrl: String,
    val timestamp: LocalDateTime
)