package com.plantcare.plantcareassistant.repository;
import com.plantcare.plantcareassistant.entities.PlantWateringHistory;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WateringHistoryRepository extends JpaRepository<PlantWateringHistory, Long>{
}
