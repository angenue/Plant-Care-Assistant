package com.plantcare.plantcareassistant.controller;

import com.plantcare.plantcareassistant.dto.WateringEventDto;
import com.plantcare.plantcareassistant.entities.PlantWateringHistory;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.services.PlantWateringHistoryService;
import com.plantcare.plantcareassistant.services.UserPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userplants")
public class UserPlantController {

    private final UserPlantService userPlantService;
    private final PlantWateringHistoryService plantWateringHistoryService;

    @Autowired
    public UserPlantController(UserPlantService userPlantService, PlantWateringHistoryService plantWateringHistoryService) {
        this.userPlantService = userPlantService;
        this.plantWateringHistoryService = plantWateringHistoryService;
    }

    //methods for displaying all the saved plants
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserPlant>> getAllSavedPlants (@PathVariable Long userId) {
        List<UserPlant> userPlants = userPlantService.getAllUserPlantsByUserId(userId);
        return ResponseEntity.ok(userPlants);
    }

    //methods for when user clicks on a plant

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

    @PostMapping("/{userPlantId}/notifications")
    public ResponseEntity<?> toggleNotifications(@PathVariable Long userPlantId, @RequestParam boolean enable) {
        PlantWateringHistory newWateringEvent = plantWateringHistoryService.toggleNotifications(userPlantId, enable);
        return ResponseEntity.ok(newWateringEvent);
    }
}
