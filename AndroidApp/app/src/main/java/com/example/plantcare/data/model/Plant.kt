package com.example.plantcare.data.model

import com.google.gson.annotations.SerializedName

data class Plant(
    val id: String,

    @SerializedName("common_name")
    val commonName: String,

    @SerializedName("scientific_name")
    val scientificName: List<String>,

    val type: String,
    val indoor: Boolean,
    val watering: String,

    @SerializedName("default_image")
    val defaultImage: DefaultImage,

    @SerializedName("care_level")
    val careLevel: String,
    val description: String,
    val sunlight: Set<String>,

    val depthWaterRequirement: List<WaterRequirement>?,
    val wateringTime: WateringTime?,

    val imageUrl: String
)

data class WaterRequirement(
    val unit: String?,
    val value: Int
)

data class WateringTime(
    val value: String,  // num of days
    val unit: String //hours, weeks, days, etc
)

data class DefaultImage(
    @SerializedName("medium_url")
    val mediumUrl: String
)

