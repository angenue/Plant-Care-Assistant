package com.plantcare.plantcareassistant.dto;

import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.UserPlant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CombinedPlantDto {
    private Plant plantDetails;
    private UserPlant userPlantDetails;
}
