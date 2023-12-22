package com.plantcare.plantcareassistant.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Plant {
    private String id;

    @JsonProperty("common_name")
    private String commonName;

    @JsonProperty("scientific_name")
    private String scientificName;


    private String type;
    private boolean indoor;

    private String watering;

    @JsonProperty("default_image")
    private DefaultImage defaultImage;

    @JsonProperty("care_level")
    private String careLevel;
    private String description;
    private Set<String> sunlight;

    //json node can handle both arrays and objects
    //need this because data is stored inconsistently in the api
    @JsonProperty("depth_water_requirement")
    private JsonNode depthWaterRequirement;
    @JsonProperty("watering_general_benchmark")
    private WateringTime wateringTime;

    private String imageUrl;


    public List<WaterRequirement> getDepthWaterRequirements() {
        List<WaterRequirement> requirements = new ArrayList<>();
        if (depthWaterRequirement.isArray()) {
            // Process as an array
            for (JsonNode node : depthWaterRequirement) {
                WaterRequirement req =jsonNodeToWaterRequirement(node);
                        requirements.add(req);
            }
        } else {
            WaterRequirement req = jsonNodeToWaterRequirement(depthWaterRequirement);
                    requirements.add(req);
        }
        return requirements;
    }

    private WaterRequirement jsonNodeToWaterRequirement(JsonNode jsonNode) {
        String unit = jsonNode.has("unit") ? jsonNode.get("unit").asText() : null;
        int value = jsonNode.has("value") ? jsonNode.get("value").asInt() : 0;
        return new WaterRequirement(unit, value);
    }


    public String getImageUrl() {
        if (defaultImage != null) {
            return defaultImage.getMediumUrl();
        }
        return null;
    }

    public void setImageFromDefault(DefaultImage defaultImage) {
        this.imageUrl = defaultImage.getMediumUrl();
    }



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaterRequirement { //how much water
        private String unit;
        private int value;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WateringTime { //when to water
        private String value;  // num of days
        private String unit; //hours, weeks, days, etc

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultImage {
        @JsonProperty("medium_url")
        private String mediumUrl;

        public String getMediumUrl() {
            return mediumUrl;
        }

        public void setMediumUrl(String mediumUrl) {
            this.mediumUrl = mediumUrl;
        }
    }


}
