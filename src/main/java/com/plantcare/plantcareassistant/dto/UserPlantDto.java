package com.plantcare.plantcareassistant.dto;

import com.plantcare.plantcareassistant.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlantDto {

    private Long userId;
    private String apiPlantId;
    private String customName;
    private String pictureUrl;

    private Boolean notificationsEnabled;
    private LocalTime notificationTime;
    private int wateringFrequency; // Frequency in days
    private String frequencyUnit; // "days", "weeks", "months"

}
