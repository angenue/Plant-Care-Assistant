package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.dto.WateringEventDto;
import com.plantcare.plantcareassistant.entities.PlantWateringHistory;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.repository.UserPlantRepository;
import com.plantcare.plantcareassistant.repository.WateringHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantWateringHistoryService {

    private final WateringHistoryRepository wateringHistoryRepository;
    private final UserPlantRepository userPlantRepository;

@Autowired
    public PlantWateringHistoryService(WateringHistoryRepository wateringHistoryRepository, UserPlantRepository userPlantRepository) {
        this.wateringHistoryRepository = wateringHistoryRepository;
        this.userPlantRepository = userPlantRepository;
    }

    public PlantWateringHistory addWateringHistory(Long userPlantId, WateringEventDto wateringEventDto) {
        // First, retrieve the UserPlant by userPlantId
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new EntityNotFoundException("UserPlant not found with id: " + userPlantId));

        // Create a new PlantWateringHistory entity from the DTO
        PlantWateringHistory newEvent = new PlantWateringHistory();
        newEvent.setUserPlant(userPlant);
        newEvent.setWateringDate(wateringEventDto.getWateringDate());
        newEvent.setWaterAmount(wateringEventDto.getWaterAmount());

        // Save the new watering event
        return wateringHistoryRepository.save(newEvent);
    }


    public List<PlantWateringHistory> getAllWateringHistoryByPlantId(Long plantId) {
    return wateringHistoryRepository.findByUserPlantId(plantId);
    }

    public PlantWateringHistory toggleWateringNotifications(Long plantWateringHistoryId, boolean enable) {
        return wateringHistoryRepository.findById(plantWateringHistoryId)
                .map(plantWateringHistory -> {
                    plantWateringHistory.setNotificationsEnabled(enable);
                    return wateringHistoryRepository.save(plantWateringHistory);
                }).orElseThrow(() -> new EntityNotFoundException("PlantWateringHistory not found with id " + plantWateringHistoryId));
    }



    public void deleteWateringHistory(Long id) {
    wateringHistoryRepository.deleteById(id);
    }
}
