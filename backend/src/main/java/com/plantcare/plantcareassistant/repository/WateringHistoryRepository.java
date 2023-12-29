package com.plantcare.plantcareassistant.repository;
import com.plantcare.plantcareassistant.entities.PlantWateringHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WateringHistoryRepository extends JpaRepository<PlantWateringHistory, Long>{
    List<PlantWateringHistory> findByUserPlantIdOrderByWateringDateDesc(long  id);
}
