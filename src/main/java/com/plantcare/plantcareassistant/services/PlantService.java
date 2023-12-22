package com.plantcare.plantcareassistant.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.PlantListResponse;
import com.plantcare.plantcareassistant.entities.SimplePlant;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PlantService {
    private final String PLANT_ID_API_URL = "https://plant.id/api/v3/identification";
    private final String API_KEY = "OUJsjwIdAUJZ4P0qmUBGQblMHbnUw9xvkUfGV82OxKzo6A9jaz";

    private final RestTemplate restTemplate;

    public PlantService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*public String searchPlants(String query) {
        String apiUrl = "https://perenual.com/api/species-list?key=sk-8zkj658232318cd963526&q=" + query;
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return response.getBody();
    }*/

    public List<SimplePlant> searchPlants(String query) {
        String apiUrl = "https://perenual.com/api/species-list?key=sk-8zkj658232318cd963526&q=" + query;
        ResponseEntity<String> rawResponse = restTemplate.getForEntity(apiUrl, String.class);
        return extractSimplePlants(rawResponse.getBody());
    }

    private List<SimplePlant> extractSimplePlants(String rawJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<SimplePlant> simplePlants = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(rawJson);
            JsonNode dataNode = rootNode.path("data");

            if (dataNode.isArray()) {
                for (JsonNode plantNode : dataNode) {
                    SimplePlant simplePlant = new SimplePlant();
                    simplePlant.setId(plantNode.path("id").asText());
                    simplePlant.setName(plantNode.path("common_name").asText());
                    // Handle scientific name as an array
                    JsonNode scientificNamesNode = plantNode.path("scientific_name");
                    if (scientificNamesNode.isArray() && scientificNamesNode.size() > 0) {
                        simplePlant.setScientificName(scientificNamesNode.get(0).asText()); // Get the first scientific name
                    }

                    simplePlant.setImageUrl(plantNode.path("default_image").path("medium_url").asText());
                    simplePlants.add(simplePlant);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        return simplePlants;
    }

    public Plant getPlantDetails(String plantId) {
        String detailUrl = "https://perenual.com/api/species/details/" + plantId + "?key=sk-8zkj658232318cd963526";
        System.out.println("Making API request to URL: " + detailUrl);

        ResponseEntity<Plant> response = restTemplate.getForEntity(detailUrl, Plant.class);
        return response.getBody();
    }

    public Plant getPlantDetailsFromImage(String base64Image) {
        String identifiedName = getPlantNameFromIdentification(base64Image);

        // Use the identified name to search for plants
        List<SimplePlant> plants = searchPlants(identifiedName);
        if (!plants.isEmpty()) {
            // Assuming you want to fetch details of the first plant
            String plantId = plants.get(0).getId();
            return getPlantDetails(plantId);
        } else {
            throw new EntityNotFoundException("No plants found for the identified image.");
        }
    }

    /*public Plant getPlantDetailsFromImage(String base64Image) {
        String identifiedName = getPlantNameFromIdentification(base64Image);
        System.out.println("Identified Plant Name: " + identifiedName);

        // Use the identified name to search for plants
        String rawJson = searchPlants(identifiedName);
        System.out.println("Plant search:" + rawJson);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(rawJson);
            JsonNode plantsNode = rootNode.path("plants");

            if (!plantsNode.isEmpty() && plantsNode.isArray()) {
                JsonNode firstPlantNode = plantsNode.get(0);
                String plantId = firstPlantNode.path("id").asText();
                System.out.println("plant id" + plantId);
                return getPlantDetails(plantId);
            } else {
                throw new EntityNotFoundException("No plants found for the identified image.");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing JSON response", e);
        }
    }*/

    public String getPlantNameFromIdentification(String base64Image) {
        String response = identifyPlant(base64Image);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode suggestions = rootNode.path("result").path("classification").path("suggestions");
            if (!suggestions.isEmpty() && suggestions.isArray()) {
                JsonNode firstSuggestion = suggestions.get(0);

                return firstSuggestion.path("name").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String identifyPlant(String base64Image) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Api-Key", API_KEY);

        String requestBody = createRequestBody(base64Image);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);


        ResponseEntity<String> response = restTemplate.postForEntity(PLANT_ID_API_URL, request, String.class);
        System.out.println("API Response: " + response.getBody());
        return response.getBody();
    }

    private String createRequestBody(String base64Image) {
        String json = "{"
                + "\"images\": [\"" + base64Image + "\"]"
                + "}";
        System.out.println("Request JSON (partial): " + json.substring(0, Math.min(json.length(), 100)) + "...");
        return json;
    }



   /* private ResponseEntity<?> handleApiError(Exception e) {
        // Log error details
        // Return an appropriate error response
    }*/



}


//https://perenual.com/api/species-list?key=sk-PlIJ657dffdc96fdb3485