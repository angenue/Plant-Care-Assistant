package com.plantcare.plantcareassistant.services;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.SimplePlant;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
public class PlantServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PlantService plantService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void whenSearchPlants_thenReturnsSimplePlantsList() {
        String query = "fern";
        String mockJsonResponse = "{\"data\": [{\"id\": \"1\", \"common_name\": \"Fern\", \"scientific_name\": [\"Pteridium\"], \"default_image\": {\"medium_url\": \"http://example.com/fern.jpg\"}}]}";
        ResponseEntity<String> mockResponseEntity = ResponseEntity.ok(mockJsonResponse);

        // Using anyString() to match any URL regardless of the actual API key
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(mockResponseEntity);

        List<SimplePlant> result = plantService.searchPlants(query);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        SimplePlant firstPlant = result.get(0);
        assertEquals("1", firstPlant.getId());
        assertEquals("Fern", firstPlant.getName());
        assertEquals("Pteridium", firstPlant.getScientificName());
        assertEquals("http://example.com/fern.jpg", firstPlant.getImageUrl());
    }

    @Test
    public void whenGetPlantDetails_thenReturnsPlant() {
        String plantId = "123";
        Plant mockPlant = new Plant();
        mockPlant.setId(plantId);
        mockPlant.setCommonName("Mock Plant Name");

        ResponseEntity<Plant> mockResponse = ResponseEntity.ok(mockPlant);

        when(restTemplate.getForEntity(anyString(), eq(Plant.class))).thenReturn(mockResponse);

        Plant result = plantService.getPlantDetails(plantId);

        assertNotNull(result);
        assertEquals(plantId, result.getId());
        assertEquals("Mock Plant Name", result.getCommonName());
    }

    /*@Test
    public void whenGetPlantDetailsFromImage_thenReturnsPlant() {
        String base64Image = "data:image/jpeg;base64,iVBORw0KGgo="; // Placeholder base64 string
        String plantIdentificationApiResponse = "{\"result\":{\"classification\":{\"suggestions\":[{\"name\":\"Fern\"}]}}}";
        String plantId = "123";
        Plant mockPlant = new Plant();
        mockPlant.setId(plantId);
        mockPlant.setCommonName("Fern");

        // Mocking the RestTemplate used in identifyPlant method
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(plantIdentificationApiResponse));

        // Mocking the RestTemplate used in getPlantDetails method
        when(restTemplate.getForEntity(anyString(), eq(Plant.class)))
                .thenReturn(ResponseEntity.ok(mockPlant));

        Plant result = plantService.getPlantDetailsFromImage(base64Image);

        assertNotNull(result);
        assertEquals("Fern", result.getCommonName());
    }


    @Test
    public void whenNoPlantsFoundForImage_thenThrowsEntityNotFoundException() {
        String base64Image = "data:image/jpeg;base64,iVBORw0KGgo=";

        String plantIdentificationApiResponse = "{\"result\":{\"classification\":{\"suggestions\":[]}}}";
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(plantIdentificationApiResponse));

        assertThrows(EntityNotFoundException.class, () -> {
            plantService.getPlantDetailsFromImage(base64Image);
        });
    }*/

}
