package com.plantcare.plantcareassistant.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimplePlant {
    private String id;
    @JsonProperty("common_name")
    private String name;

    @JsonProperty("scientific_name")
    private String scientificName;
    private String imageUrl;

    public void setImageFromDefault(Plant.DefaultImage defaultImage) {
        this.imageUrl = defaultImage.getMediumUrl();
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
