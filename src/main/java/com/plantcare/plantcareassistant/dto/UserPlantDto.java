package com.plantcare.plantcareassistant.dto;

import com.plantcare.plantcareassistant.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlantDto {

    private Long userId;
    private String apiPlantId;
    private String customName;
    private String pictureUrl;
}
