package com.plantcare.plantcareassistant.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Plant {
    private String id;

    @JsonProperty("common_name")
    private String name;

    private String type;
    private boolean indoor;

    @JsonProperty("care_level")
    private String careLevel;
    private String description;
    private Set<String> sunlight;

    @JsonProperty("depth_water_requirement")
    private WaterRequirement depthWaterRequirement;
    @JsonProperty("watering_general_benchmark")
    private WateringTime wateringTime;

    private String imageUrl;

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
