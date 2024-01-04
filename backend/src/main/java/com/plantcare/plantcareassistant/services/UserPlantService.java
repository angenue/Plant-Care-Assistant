package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.dto.CombinedPlantDto;
import com.plantcare.plantcareassistant.dto.UserPlantDto;
import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.repository.UserPlantRepository;
import com.plantcare.plantcareassistant.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserPlantService {

    private final UserPlantRepository userPlantRepository;
    private final UserRepository userRepository;
    private final PlantService plantService;
    @Value("${app.uploaded-images-path}")
    private String imageStoragePath;
    @Autowired
    public UserPlantService(UserPlantRepository userPlantRepository, UserRepository userRepository, PlantService plantService) {
        this.userPlantRepository = userPlantRepository;
        this.userRepository = userRepository;
        this.plantService = plantService;
    }

    public List<UserPlant> getAllUserPlantsByUserId(Long userId) {
        return userPlantRepository.findByUserId(userId);
    }

    public CombinedPlantDto getUserPlantById(Long userPlantId, Long userId) {
        UserPlant userPlant = checkIfPlantExists(userPlantId);

        if (!userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }

        Plant plant = plantService.getPlantDetails(userPlant.getApiPlantId());

        CombinedPlantDto combinedPlantDto = new CombinedPlantDto();
        combinedPlantDto.setPlantDetails(plant);
        combinedPlantDto.setUserPlantDetails(userPlant);

        return combinedPlantDto;
    }


    public UserPlant getUserPlantForWateringLog(Long userPlantId, Long userId) {
        UserPlant userPlant = checkIfPlantExists(userPlantId);

        if (!userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }

        return userPlant;
    }



    public UserPlant addUserPlant(Long userId, UserPlantDto userPlantDto) {
        UserPlant userPlant = new UserPlant();

        //retrieving user id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        userPlant.setUser(user);

        if (!userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }


        userPlant.setApiPlantId(userPlantDto.getApiPlantId());
        userPlant.setScientificName(userPlantDto.getScientificName());
        userPlant.setCustomName(userPlantDto.getCustomName());
        userPlant.setPictureUrl(userPlantDto.getPictureUrl());
        userPlant.setNotificationsEnabled(userPlantDto.getNotificationsEnabled());
        userPlant.setNotificationTime(userPlantDto.getNotificationTime());
        userPlant.setWateringFrequency(userPlantDto.getWateringFrequency());
        userPlant.setFrequencyUnit(userPlantDto.getFrequencyUnit());

        return userPlantRepository.save(userPlant);
    }



    public UserPlant updateUserPlantPicture(Long userPlantId, Long userId, String newPicture) {
        UserPlant userPlant = checkIfPlantExists(userPlantId);
        // Check if the userPlant belongs to the authenticated user
        if (userPlant.getUser() == null || !userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }

        userPlant.setPictureUrl(newPicture);
        return userPlantRepository.save(userPlant);
    }

    public String storeImageAndReturnUrl(Long userPlantId, MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Resolve the path relative to the current working directory
        Path directoryPath = Paths.get(imageStoragePath);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        Path filePath = directoryPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Update UserPlant with the URL
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));
        userPlant.setPictureUrl("/images/" + fileName);
        userPlantRepository.save(userPlant);

        return userPlant.getPictureUrl();
    }



    public UserPlant updatePlantName(Long userPlantId, Long userId, String newName) {
        UserPlant userPlant = checkIfPlantExists(userPlantId);

        if (userPlant.getUser() == null || !userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }

        userPlant.setCustomName(newName);

        return userPlantRepository.save(userPlant);
    }

    public void deleteUserPlant(Long userPlantId, Long userId) {
        UserPlant userPlant = checkIfPlantExists(userPlantId);

        if (userPlant.getUser() == null || !userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }

        userPlantRepository.deleteById(userPlantId);
    }

    private UserPlant checkIfPlantExists(Long userPlantId) {
        return userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new EntityNotFoundException("UserPlant not found with id " + userPlantId));
    }


    public boolean isPlantSavedByUser(String plantId, Long userId) {
        List<UserPlant> userPlants = userPlantRepository.findByUserId(userId);
        return userPlants.stream().anyMatch(up -> plantId.equals(up.getApiPlantId()));
    }
}
