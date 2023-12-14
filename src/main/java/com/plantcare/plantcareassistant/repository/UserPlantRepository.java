package com.plantcare.plantcareassistant.repository;

import com.plantcare.plantcareassistant.entities.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserPlantRepository extends JpaRepository<UserPlant, Long>{
}
