package com.plantcare.plantcareassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WateringEventDto {
    private LocalDateTime wateringDate;
    private Double waterAmount;
}
