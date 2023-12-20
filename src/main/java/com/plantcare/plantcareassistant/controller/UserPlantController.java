package com.plantcare.plantcareassistant.controller;

import com.plantcare.plantcareassistant.dto.CombinedPlantDto;
import com.plantcare.plantcareassistant.dto.UserPlantDto;
import com.plantcare.plantcareassistant.dto.WateringEventDto;
import com.plantcare.plantcareassistant.entities.PlantWateringHistory;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.services.PlantWateringHistoryService;
import com.plantcare.plantcareassistant.services.UserPlantService;
import com.plantcare.plantcareassistant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userplants")
public class UserPlantController {

    private final UserPlantService userPlantService;
    private final PlantWateringHistoryService plantWateringHistoryService;
    private final UserService userService;

    @Autowired
    public UserPlantController(UserPlantService userPlantService, PlantWateringHistoryService plantWateringHistoryService, UserService userService) {
        this.userPlantService = userPlantService;
        this.plantWateringHistoryService = plantWateringHistoryService;
        this.userService = userService;
    }

    //method for saving a plant
    @PostMapping
    public ResponseEntity<UserPlant> addUserPlant(@RequestBody UserPlantDto userPlantDto) {
        Long currentUserId = getCurrentUserId(); // Method to get the authenticated user's ID
        UserPlant newUserPlant = userPlantService.addUserPlant(currentUserId, userPlantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserPlant);
    }


    //methods for displaying all the saved plants
    @GetMapping("/yourPlants")
    public ResponseEntity<List<UserPlant>> getAllSavedPlants() {
        Long currentUserId = getCurrentUserId();
        List<UserPlant> userPlants = userPlantService.getAllUserPlantsByUserId(currentUserId);
        return ResponseEntity.ok(userPlants);
    }

    //methods for when user clicks on a plant
    @GetMapping("/{userPlantId}")
    public ResponseEntity<CombinedPlantDto> getUserPlant(@PathVariable Long userPlantId) {
        Long currentUserId = getCurrentUserId();
        CombinedPlantDto combinedPlantDetails = userPlantService.getUserPlantById(userPlantId, currentUserId);
        return ResponseEntity.ok(combinedPlantDetails);
    }


    //methods for the watering log
    @PostMapping("/{userPlantId}/watering")
    public ResponseEntity<?> addWateringEvent(@PathVariable Long userPlantId, @RequestBody WateringEventDto wateringEventDto) {
        Long currentUserId = getCurrentUserId(); // Method to get the authenticated user's ID
        PlantWateringHistory newWateringEvent = plantWateringHistoryService.addWateringHistory(userPlantId, wateringEventDto, currentUserId);
        return ResponseEntity.ok(newWateringEvent);
    }

    //displays the watering log
    @GetMapping("/{userPlantId}/watering-log")
    public ResponseEntity<?> getWateringLog(@PathVariable Long userPlantId) {
        Long currentUserId = getCurrentUserId();
        UserPlant userPlant = userPlantService.getUserPlantForWateringLog(userPlantId, currentUserId);
        List<PlantWateringHistory> wateringHistory = plantWateringHistoryService.getAllWateringHistoryByPlantId(userPlantId, currentUserId);

        Map<String, Object> response = new HashMap<>();
        response.put("userPlant", userPlant); // Includes notification settings
        response.put("wateringHistory", wateringHistory);

        return ResponseEntity.ok(response);
    }


    // Endpoint to update notification settings
    @PutMapping("/{userPlantId}/notification-settings")
    public ResponseEntity<UserPlant> updateNotificationSettings(@PathVariable Long userPlantId, @RequestBody UserPlantDto userPlantDto) {
        Long currentUserId = getCurrentUserId();
        UserPlant updatedUserPlant = plantWateringHistoryService.updateNotificationSettings(userPlantId, userPlantDto, currentUserId);
        return ResponseEntity.ok(updatedUserPlant);
    }

    // Endpoint to update a watering log
    @PutMapping("/watering-logs/{logId}")
    public ResponseEntity<PlantWateringHistory> updateWateringLog(@PathVariable Long logId, @RequestBody LocalDateTime newWateringDateTime) {
        Long currentUserId = getCurrentUserId();
        PlantWateringHistory updatedLog = plantWateringHistoryService.updateWateringLog(logId, newWateringDateTime, currentUserId);
        return ResponseEntity.ok(updatedLog);
    }

    @DeleteMapping("/watering-logs/{id}")
    public ResponseEntity<?> deleteWateringHistory(@PathVariable Long id) {
        Long currentUserId = getCurrentUserId();
        plantWateringHistoryService.deleteWateringHistory(id, currentUserId);
        return ResponseEntity.noContent().build();
    }



    //methods regarding notifications
    @PostMapping("/{userPlantId}/notifications")
    public ResponseEntity<?> toggleNotifications(@PathVariable Long userPlantId, @RequestParam boolean enable) {
        Long currentUserId = getCurrentUserId();
        UserPlant wateringNotif = plantWateringHistoryService.toggleNotifications(userPlantId, enable, currentUserId);
        return ResponseEntity.ok(wateringNotif);
    }

    //methods for updating plant image and name
    @PutMapping("/{userPlantId}/picture")
    public ResponseEntity<UserPlant> updateUserPlantPicture(@PathVariable Long userPlantId, @RequestBody String newPicture) {
        Long currentUserId = getCurrentUserId();
        UserPlant updatedUserPlant = userPlantService.updateUserPlantPicture(userPlantId, currentUserId, newPicture);
        return ResponseEntity.ok(updatedUserPlant);
    }

    @PutMapping("/{userPlantId}/name")
    public ResponseEntity<UserPlant> updatePlantName(@PathVariable Long userPlantId, @RequestBody String newName) {
        Long currentUserId = getCurrentUserId();
        UserPlant updatedUserPlant = userPlantService.updatePlantName(userPlantId, currentUserId, newName);
        return ResponseEntity.ok(updatedUserPlant);
    }

    //method for deleting saved plant
    @DeleteMapping("/{userPlantId}")
    public ResponseEntity<?> deleteUserPlant(@PathVariable Long userPlantId) {
        Long currentUserId = getCurrentUserId();
        userPlantService.deleteUserPlant(userPlantId, currentUserId);
        return ResponseEntity.ok().build();
    }


    // Utility method to get the current user's ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        return user.getId();
    }


}
