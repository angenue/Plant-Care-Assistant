package com.plantcare.plantcareassistant.entities;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

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
    private String customName;
    private String pictureUrl;
    private Boolean notificationsEnabled;

    @Column(nullable = true)
    private LocalDateTime lastWatered;
}

