package com.example.plantcare.data.model

// Plant.kt

data class Plant(
    val name: String,
    val imageUrl: String,
    val careLevel: String,
    val sunlightAmount: String,
    val indoor: Boolean,
    val description: String
)