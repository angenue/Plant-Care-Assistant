package com.plantcare.plantcareassistant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final RestTemplate restTemplate;

    public PlantController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPlants(@RequestParam String query) {
        String apiUrl = "https://perenual.com/api/species-list?key=sk-PlIJ657dffdc96fdb3485&q=" + query;
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}

