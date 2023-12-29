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
@Table(name = "recently_searched_plants")
public class RecentlySearchedPlants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String apiPlantId;
    private String plantName;
    private String pictureUrl;
    private LocalDateTime timestamp;

    public RecentlySearchedPlants(Long userId, String apiPlantId, LocalDateTime now) {
    }
}

