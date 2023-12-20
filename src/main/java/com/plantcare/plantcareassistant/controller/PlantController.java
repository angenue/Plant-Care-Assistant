package com.plantcare.plantcareassistant.controller;

import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.services.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final RestTemplate restTemplate;
    private final PlantService plantService;

    public PlantController(RestTemplate restTemplate, PlantService plantService) {
        this.restTemplate = restTemplate;
        this.plantService = plantService;
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Hello, World!";
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPlants(@RequestParam String query) {
        String apiUrl = "https://perenual.com/api/species-list?key=sk-8zkj658232318cd963526&q=" + query;
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/details/{plantId}")
    public ResponseEntity<Plant> getPlantDetails(@PathVariable String plantId) {
        System.out.println(plantId);
        Plant plant = plantService.getPlantDetails(plantId);
        return ResponseEntity.ok(plant);
    }
}

