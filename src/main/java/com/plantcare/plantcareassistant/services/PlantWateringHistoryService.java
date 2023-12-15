package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.entities.PlantWateringHistory;
import com.plantcare.plantcareassistant.repository.WateringHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantWateringHistoryService {

    private final WateringHistoryRepository wateringHistoryRepository;

@Autowired
    public PlantWateringHistoryService(WateringHistoryRepository wateringHistoryRepository) {
        this.wateringHistoryRepository = wateringHistoryRepository;
    }

    public PlantWateringHistory addWateringHistory(PlantWateringHistory wateringHistory) {
        return wateringHistoryRepository.save(wateringHistory);
    }

    public List<PlantWateringHistory> getAllWateringHistoryByPlantId(Long plantId) {
    return wateringHistoryRepository.findByUserPlantId(plantId);
    }

    public void deleteWateringHistory(Long id) {
    wateringHistoryRepository.deleteById(id);
    }
}
