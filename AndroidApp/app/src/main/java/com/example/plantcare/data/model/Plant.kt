package com.example.plantcare.data.model

import com.google.gson.annotations.SerializedName

import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import java.lang.reflect.Type

data class Plant(
    val id: String,
    @SerializedName("common_name") val commonName: String,
    @SerializedName("scientific_name") val scientificName: List<String>,
    val type: String,
    val indoor: Boolean,
    val watering: String,
    @SerializedName("default_image") val defaultImage: DefaultImage,
    @SerializedName("care_level") val careLevel: String,
    val description: String,
    val sunlight: Set<String>,
    @JsonAdapter(DepthWaterRequirementDeserializer::class)
    @SerializedName("depth_water_requirement") val depthWaterRequirement: List<WaterRequirement>?,
    @SerializedName("watering_general_benchmark") val wateringTime: WateringTime?,
    val imageUrl: String
)

data class WaterRequirement(
    val unit: String?,
    val value: Int
)

data class WateringTime(
    val value: String,
    val unit: String
)

data class DefaultImage(
    @SerializedName("medium_url") val mediumUrl: String
)

class DepthWaterRequirementDeserializer : JsonDeserializer<List<WaterRequirement>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<WaterRequirement> {
        val result = mutableListOf<WaterRequirement>()
        if (json.isJsonArray) {
            val array = json.asJsonArray
            for (element in array) {
                result.add(context.deserialize(element, WaterRequirement::class.java))
            }
        } else if (json.isJsonObject) {
            result.add(context.deserialize(json, WaterRequirement::class.java))
        }
        return result
    }
}
