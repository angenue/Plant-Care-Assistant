package com.plantcare.plantcareassistant.controller;

import com.plantcare.plantcareassistant.dto.RecentlySearchedPlantsDto;
import com.plantcare.plantcareassistant.entities.ImageRequest;
import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.SimplePlant;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.services.PlantService;
import com.plantcare.plantcareassistant.services.RecentlySearchedPlantsService;
import com.plantcare.plantcareassistant.services.UserPlantService;
import com.plantcare.plantcareassistant.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final RestTemplate restTemplate;
    private final PlantService plantService;
    private final UserService userService;
    private final UserPlantService userPlantService;
    private final RecentlySearchedPlantsService recentlySearchedPlantsService;

    public PlantController(RestTemplate restTemplate, PlantService plantService, UserService userService, UserPlantService userPlantService, RecentlySearchedPlantsService recentlySearchedPlantsService) {
        this.restTemplate = restTemplate;
        this.plantService = plantService;
        this.userService = userService;
        this.userPlantService = userPlantService;
        this.recentlySearchedPlantsService = recentlySearchedPlantsService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SimplePlant>> searchPlants(@RequestParam String query) {
        List<SimplePlant> plants = plantService.searchPlants(query);
        return ResponseEntity.ok(plants);
    }


    @PostMapping("/identify-from-image")
    public ResponseEntity<Plant> identifyPlantFromImage(@RequestBody ImageRequest imageRequest) {
        String base64Image = imageRequest.getImages().get(0);
        Plant plantDetails = plantService.getPlantDetailsFromImage(base64Image);
        return ResponseEntity.ok(plantDetails);
    }

    @PostMapping("/identify")
    public ResponseEntity<String> identifyPlant(@RequestBody ImageRequest imageRequest) {
        String base64Image = imageRequest.getImages().get(0);
        String response = plantService.identifyPlant(base64Image);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details/{plantId}")
    public ResponseEntity<Plant> getPlantDetails(@PathVariable String plantId) {
        Long currentUserId = getCurrentUserId(); // Method to obtain the current user's ID
        Plant plant = plantService.getPlantDetails(plantId);

        // Add to recently searched if the plant is not already in the user's saved plants
        if (!userPlantService.isPlantSavedByUser(plantId, currentUserId)) {
            //if its not saved then add it to recently searched
            recentlySearchedPlantsService.addRecentlySearched(currentUserId, plantId);
        }
        return ResponseEntity.ok(plant);
    }

    @GetMapping("/recently-searched")
    public ResponseEntity<List<RecentlySearchedPlantsDto>> getRecentlySearchedPlants() {
        Long currentUserId = getCurrentUserId();
        List<RecentlySearchedPlantsDto> recentlySearched = recentlySearchedPlantsService.getRecentlySearchedPlants(currentUserId);
        return ResponseEntity.ok(recentlySearched);
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

