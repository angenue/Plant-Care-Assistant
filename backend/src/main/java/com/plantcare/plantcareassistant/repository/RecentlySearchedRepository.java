package com.plantcare.plantcareassistant.repository;
import com.plantcare.plantcareassistant.entities.RecentlySearchedPlants;
import com.plantcare.plantcareassistant.entities.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecentlySearchedRepository extends JpaRepository<RecentlySearchedPlants, Long>{
    List<RecentlySearchedPlants> findByUserIdOrderByTimestampDesc(Long userId);
    void deleteAllByIdIn(List<Long> ids); //allows user to delete multiple entries

}
