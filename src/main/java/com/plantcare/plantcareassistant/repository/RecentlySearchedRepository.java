package com.plantcare.plantcareassistant.repository;
import com.plantcare.plantcareassistant.entities.RecentlySearchedPlants;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RecentlySearchedRepository extends JpaRepository<RecentlySearchedPlants, Long>{
}
