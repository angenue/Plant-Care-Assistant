package com.example.plantcare.data.model

import com.google.gson.annotations.SerializedName

data class SimplePlant(
    val id: String,
    @SerializedName("common_name")
    val name: String,
    @SerializedName("scientific_name")
    val scientificName: String?,
    //@SerializedName("default_image")
    val imageUrl: String
)
