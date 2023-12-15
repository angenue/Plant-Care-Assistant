package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.repository.UserPlantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPlantService {

    private final UserPlantRepository userPlantRepository;

    @Autowired
    public UserPlantService(UserPlantRepository userPlantRepository) {
        this.userPlantRepository = userPlantRepository;
    }

    public List<UserPlant> getAllUserPlantsByUserId(Long userId) {
        return userPlantRepository.findByUserId(userId);
    }

    public UserPlant getUserPlantById(Long id) {
        return userPlantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plant not found with id " + id));
    }

    public UserPlant addUserPlant(UserPlant userPlant) {
        return userPlantRepository.save(userPlant);
    }

    public UserPlant updateUserPlant(Long id, UserPlant updatedUserPlant) {
        return userPlantRepository.findById(id)
                .map(userPlant -> {
                    userPlant.setCustomName(updatedUserPlant.getCustomName());
                    userPlant.setPictureUrl(updatedUserPlant.getPictureUrl());
                    userPlant.setLastWatered(updatedUserPlant.getLastWatered());
                    userPlant.setNotificationsEnabled(updatedUserPlant.getNotificationsEnabled());

                    return userPlantRepository.save(userPlant);
                }).orElseThrow(() -> new EntityNotFoundException("UserPlant not found with id " + id));
    }

    public void deleteUserPlant(Long id) {
        userPlantRepository.deleteById(id);
    }
}
