package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.dto.UserPlantDto;
import com.plantcare.plantcareassistant.dto.WateringEventDto;
import com.plantcare.plantcareassistant.entities.PlantWateringHistory;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.repository.UserPlantRepository;
import com.plantcare.plantcareassistant.repository.WateringHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
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

        PlantWateringHistory savedEvent = wateringHistoryRepository.save(newEvent);

        // Check and maintain only the latest 20 entries
        List<PlantWateringHistory> history = wateringHistoryRepository.findByUserPlantIdOrderByWateringDateDesc(userPlant.getId());
        if (history.size() > 20) {
            wateringHistoryRepository.delete(history.get(history.size() - 1)); // Delete the oldest entry
        }

        scheduleNextWateringNotification(userPlant);

        return savedEvent;
    }


    public List<PlantWateringHistory> getAllWateringHistoryByPlantId(Long plantId) {
    return wateringHistoryRepository.findByUserPlantIdOrderByWateringDateDesc(plantId);
    }

    public UserPlant toggleNotifications(Long userPlantId, boolean enable) {
        return userPlantRepository.findById(userPlantId)
                .map(userPlant -> {
                    userPlant.setNotificationsEnabled(enable);
                    return userPlantRepository.save(userPlant);
                }).orElseThrow(() -> new EntityNotFoundException("User plant not found with id " + userPlantId));
    }

    //for notification and timer purposes
    private PlantWateringHistory fetchMostRecentWateringLog(Long userPlantId) {
        return wateringHistoryRepository.findByUserPlantIdOrderByWateringDateDesc(userPlantId)
                .stream()
                .findFirst()//gets most recent log entry
                .orElse(null); // handle the case when there's no log entry
    }

    private void scheduleNextWateringNotification(UserPlant userPlant) {
        PlantWateringHistory mostRecentLog = fetchMostRecentWateringLog(userPlant.getId());
        if (mostRecentLog == null) {
            // if theres no watering history
            return;
        }

        LocalDateTime lastWateringDateTime = mostRecentLog.getWateringDate();
        LocalDateTime nextWateringDay = calculateNextWateringDay(userPlant, lastWateringDateTime.toLocalDate()).atStartOfDay();
        LocalDateTime nextWateringTime = LocalDateTime.of(LocalDate.from(nextWateringDay), userPlant.getNotificationTime());

        // TODO: implement something to handle notifs
    }

    private LocalDate calculateNextWateringDay(UserPlant userPlant, LocalDate lastWateringDate) {
        // Assuming userPlant has fields like wateringFrequency and frequencyUnit
        Period frequencyPeriod = getFrequencyPeriod(userPlant.getWateringFrequency(), userPlant.getFrequencyUnit());
        return lastWateringDate.plus(frequencyPeriod);
    }

    private Period getFrequencyPeriod(int frequency, String unit) {
        return switch (unit.toLowerCase()) {
            case "days" -> Period.ofDays(frequency);
            case "weeks" -> Period.ofWeeks(frequency);
            case "months" -> Period.ofMonths(frequency);
            default -> throw new IllegalArgumentException("Invalid frequency unit: " + unit);
        };
    }

    public UserPlant updateNotificationSettings(Long userPlantId, UserPlantDto userPlantDto) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new EntityNotFoundException("UserPlant not found with id: " + userPlantId));

        userPlant.setWateringFrequency(userPlantDto.getWateringFrequency());
        userPlant.setFrequencyUnit(userPlantDto.getFrequencyUnit());
        userPlant.setNotificationTime(userPlantDto.getNotificationTime());

        return userPlantRepository.save(userPlant);
    }


    //users can update the time and date of their entry
    public PlantWateringHistory updateWateringLog(Long logId, LocalDateTime newWateringDateTime) {
        PlantWateringHistory log = wateringHistoryRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("WateringLog not found with id: " + logId));

        log.setWateringDate(newWateringDateTime);

        return wateringHistoryRepository.save(log);
    }


    public void deleteWateringHistory(Long id) {
    wateringHistoryRepository.deleteById(id);
    }
}
