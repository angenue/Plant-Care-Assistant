package com.plantcare.plantcareassistant.entities;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_plants")
public class UserPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String apiPlantId;
    private String scientificName;
    private String customName;
    private String pictureUrl;

    private Boolean notificationsEnabled;
    private LocalTime notificationTime;
    private int wateringFrequency; // Frequency in days
    private String frequencyUnit; // "days", "weeks", "months"
}

