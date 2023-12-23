package com.plantcare.plantcareassistant.controller;

import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.SimplePlant;
import com.plantcare.plantcareassistant.services.PlantService;
import com.plantcare.plantcareassistant.services.RecentlySearchedPlantsService;
import com.plantcare.plantcareassistant.services.UserPlantService;
import com.plantcare.plantcareassistant.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PlantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlantService plantService;
    @Mock
    private UserService userService;
    @Mock
    private UserPlantService userPlantService;
    @Mock
    private RecentlySearchedPlantsService recentlySearchedPlantsService;

    @InjectMocks
    private PlantController plantController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(plantController).build();
    }

    @Test
    void whenSearchPlants_thenReturnPlantList() throws Exception {
        String query = "fern";
        List<SimplePlant> mockPlants = Collections.singletonList(new SimplePlant("1", "Fern", "Pteridium", "http://example.com/fern.jpg"));

        given(plantService.searchPlants(query)).willReturn(mockPlants);

        mockMvc.perform(get("/api/plants/search").param("query", query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].common_name").value("Fern"))
                .andExpect(jsonPath("$[0].imageUrl").value("http://example.com/fern.jpg"));
    }

    /*@Test
    void whenIdentifyPlantFromImage_thenReturnPlantDetails() throws Exception {
        String base64Image = "data:image/jpeg;base64,iVBORw0KGgo=";
        Plant mockPlant = new Plant();
        String plantId = "123";
        String commonName = "Mock Plant Name";
        mockPlant.setId(plantId);
        mockPlant.setCommonName(commonName);

        given(plantService.getPlantDetailsFromImage(base64Image)).willReturn(mockPlant);

        mockMvc.perform(post("/api/plants/identify-from-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"images\": [\"" + base64Image + "\"]}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockPlant.getId()))
                .andExpect(jsonPath("$.commonName").value(commonName));
    }*/

    @Test
    void whenIdentifyPlant_thenReturnIdentificationResponse() throws Exception {
        String base64Image = "data:image/jpeg;base64,iVBORw0KGgo=";
        String mockResponse = "Plant Identification Response";

        given(plantService.identifyPlant(base64Image)).willReturn(mockResponse);

        mockMvc.perform(post("/api/plants/identify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"images\": [\"" + base64Image + "\"]}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mockResponse));
    }

    //commented out because i cant figure out how to authenticate user
    /*@Test
    @WithMockUser(username = "user@example.com") // Simulate an authenticated user
    void whenGetPlantDetails_thenReturnPlant() throws Exception {
        String plantId = "123";
        Plant mockPlant = new Plant();
        mockPlant.setId(plantId);
        mockPlant.setCommonName("Mock Plant");

        given(plantService.getPlantDetails(plantId)).willReturn(mockPlant);
        given(userPlantService.isPlantSavedByUser(eq(plantId), anyLong())).willReturn(false);


        mockMvc.perform(get("/api/plants/details/{plantId}", plantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(plantId))
                .andExpect(jsonPath("$.commonName").value("Mock Plant"));

        verify(recentlySearchedPlantsService).addRecentlySearched(anyLong(), eq(plantId));
    }*/
}