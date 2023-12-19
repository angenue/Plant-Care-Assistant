package com.plantcare.plantcareassistant.controller;

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
    @GetMapping("/user/plants")
    public ResponseEntity<List<UserPlant>> getAllSavedPlants() {
        Long currentUserId = getCurrentUserId();
        List<UserPlant> userPlants = userPlantService.getAllUserPlantsByUserId(currentUserId);
        return ResponseEntity.ok(userPlants);
    }

    //methods for when user clicks on a plant

    //gets the plant info from api
    /*@GetMapping("/{userPlantId}/details")
    public ResponseEntity<?> getPlantDetails(@PathVariable Long userPlantId) {
        //need to get plantid
        UserPlant userPlant = userPlantService.getUserPlantById(userPlantId);
    }*/

    //methods for the watering log
    @PostMapping("/{userPlantId}/watering")
    public ResponseEntity<?> addWateringEvent(@PathVariable Long userPlantId, @RequestBody WateringEventDto wateringEventDto) {
        PlantWateringHistory newWateringEvent = plantWateringHistoryService.addWateringHistory(userPlantId, wateringEventDto);
        return ResponseEntity.ok(newWateringEvent);
    }

    //displays the watering log
    @GetMapping("/{userPlantId}")
    public ResponseEntity<?> getWateringLog(@PathVariable Long userPlantId) {
        UserPlant userPlant = userPlantService.getUserPlantById(userPlantId);
        List<PlantWateringHistory> wateringHistory = plantWateringHistoryService.getAllWateringHistoryByPlantId(userPlantId);

        Map<String, Object> response = new HashMap<>();
        response.put("userPlant", userPlant);
        response.put("wateringHistory", wateringHistory);

        return ResponseEntity.ok(response);
    }

    // Endpoint to update notification settings
    @PutMapping("/{userPlantId}/notification-settings")
    public ResponseEntity<UserPlant> updateNotificationSettings(@PathVariable Long userPlantId, @RequestBody UserPlantDto userPlantDto) {
        UserPlant updatedUserPlant = plantWateringHistoryService.updateNotificationSettings(userPlantId, userPlantDto);
        return ResponseEntity.ok(updatedUserPlant);
    }

    // Endpoint to update a watering log
    @PutMapping("/watering-logs/{logId}")
    public ResponseEntity<PlantWateringHistory> updateWateringLog(@PathVariable Long logId, @RequestBody LocalDateTime newWateringDateTime) {
        PlantWateringHistory updatedLog = plantWateringHistoryService.updateWateringLog(logId, newWateringDateTime);
        return ResponseEntity.ok(updatedLog);
    }

    // Endpoint to delete a watering history entry
    @DeleteMapping("/watering-logs/{id}")
    public ResponseEntity<?> deleteWateringHistory(@PathVariable Long id) {
        plantWateringHistoryService.deleteWateringHistory(id);
        return ResponseEntity.noContent().build();
    }


    //methods regarding notifications
    @PostMapping("/{userPlantId}/notifications")
    public ResponseEntity<?> toggleNotifications(@PathVariable Long userPlantId, @RequestParam boolean enable) {
        UserPlant wateringNotif = plantWateringHistoryService.toggleNotifications(userPlantId, enable);
        return ResponseEntity.ok(wateringNotif);
    }

    //methods for updating plant image and name
    @PutMapping("/{userPlantId}/picture")
    public ResponseEntity<UserPlant> updateUserPlantPicture(@PathVariable Long userPlantId, @RequestBody String newPicture) {
        UserPlant updatedUserPlant = userPlantService.updateUserPlant(userPlantId, newPicture);
        return ResponseEntity.ok(updatedUserPlant);
    }

    @PutMapping("/{userPlantId}/name")
    public ResponseEntity<UserPlant> updatePlantName(@PathVariable Long userPlantId, @RequestBody String newName) {
        UserPlant updatedUserPlant = userPlantService.updatePlantName(userPlantId, newName);
        return ResponseEntity.ok(updatedUserPlant);
    }

    //method for deleting saved plant
    @DeleteMapping("/{userPlantId}")
    public ResponseEntity<?> deleteUserPlant(@PathVariable Long userPlantId) {
        userPlantService.deleteUserPlant(userPlantId);
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
