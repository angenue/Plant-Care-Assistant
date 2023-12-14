package com.plantcare.plantcareassistant.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "plant_watering_history")
public class PlantWateringHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_plant_id", nullable = false)
    private UserPlant userPlant;

    private LocalDateTime wateringDate;
    private Double waterAmount;
}

