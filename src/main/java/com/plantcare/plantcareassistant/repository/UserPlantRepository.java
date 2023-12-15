package com.plantcare.plantcareassistant.repository;

import com.plantcare.plantcareassistant.entities.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPlantRepository extends JpaRepository<UserPlant, Long>{
    List<UserPlant> findByUserId(Long userId);
}
