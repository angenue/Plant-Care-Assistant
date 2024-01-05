package com.example.plantcare.data.model

import java.time.LocalDateTime

data class PlantWateringHistory(
    val id: Long,
    val userPlantId: Long,
    val wateringDate: LocalDateTime,
    val waterAmount: Double
)

data class WateringEventDto(
    val wateringDate: LocalDateTime,
    val waterAmount: Double
)

data class UserPlantWateringHistoryResponse(
    val userPlant: UserPlant,
    val wateringHistory: List<PlantWateringHistory>
)

