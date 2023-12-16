package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.PlantListResponse;
import com.plantcare.plantcareassistant.entities.SimplePlant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final RestTemplate restTemplate;

    public PlantService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SimplePlant> searchPlants(String query) {
        String apiUrl = "https://perenual.com/api/species-list?key=sk-PlIJ657dffdc96fdb3485&q=" + query;
        ResponseEntity<PlantListResponse> response = restTemplate.getForEntity(apiUrl, PlantListResponse.class);
        return response.getBody().getPlants().stream()
                .map(this::transformToSimplePlant)
                .collect(Collectors.toList());
    }

    //method to only show the image and plant name when searching
    private SimplePlant transformToSimplePlant(Plant plant) {
        SimplePlant simplePlant = new SimplePlant();
        simplePlant.setName(plant.getName());
        simplePlant.setImageUrl(plant.getImageUrl());
        return simplePlant;
    }

    public Plant getPlantDetails(String plantId) {
        String detailUrl = "https://perenual.com/api/species/details/" + plantId + "?key=sk-PlIJ657dffdc96fdb3485";
        ResponseEntity<Plant> response = restTemplate.getForEntity(detailUrl, Plant.class);
        return response.getBody();
    }


   /* private ResponseEntity<?> handleApiError(Exception e) {
        // Log error details
        // Return an appropriate error response
    }*/



}


//https://perenual.com/api/species-list?key=sk-PlIJ657dffdc96fdb3485