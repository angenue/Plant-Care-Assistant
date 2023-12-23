package com.plantcare.plantcareassistant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plantcare.plantcareassistant.dto.CombinedPlantDto;
import com.plantcare.plantcareassistant.dto.UserPlantDto;
import com.plantcare.plantcareassistant.dto.WateringEventDto;
import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.PlantWateringHistory;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.services.PlantWateringHistoryService;
import com.plantcare.plantcareassistant.services.UserPlantService;
import com.plantcare.plantcareassistant.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserPlantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserPlantService userPlantService;
    @Mock
    private PlantWateringHistoryService plantWateringHistoryService;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserPlantController userPlantController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userPlantController).build();
        setupMockSecurityContext();
    }

    //to authorize user for testing
    private void setupMockSecurityContext() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("user@example.com", "password", authorities);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("user@example.com");

        when(userService.getUserByEmail("user@example.com")).thenReturn(mockUser);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    //tests for adding and retrieiving user plant
    @Test
    void whenAddUserPlant_thenReturnNewUserPlant() throws Exception {
        UserPlantDto userPlantDto = new UserPlantDto();
        userPlantDto.setApiPlantId("plant123");
        userPlantDto.setCustomName("My Favorite Plant");
        userPlantDto.setPictureUrl("http://example.com/plant.jpg");

        UserPlant mockUserPlant = mockUserPlant();

        given(userPlantService.addUserPlant(anyLong(), any(UserPlantDto.class))).willReturn(mockUserPlant);

        mockMvc.perform(post("/api/userplants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userPlantDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockUserPlant.getId()));
    }

    @Test
    void whenGetAllSavedPlants_thenReturnListOfUserPlants() throws Exception {
        List<UserPlant> mockUserPlants = Arrays.asList(
                createUserPlant(1L, "Plant1", "http://example.com/plant1.jpg"),
                createUserPlant(2L, "Plant2", "http://example.com/plant2.jpg"),
                createUserPlant(3L, "Plant3", "http://example.com/plant3.jpg")
        );

        given(userPlantService.getAllUserPlantsByUserId(anyLong())).willReturn(mockUserPlants);

        mockMvc.perform(get("/api/userplants/yourPlants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(mockUserPlants.size())));
    }

    @Test
    void whenGetUserPlant_thenReturnCombinedPlantDetails() throws Exception {
        Long userPlantId = 1L;
        Long currentUserId = 1L;
        Plant mockPlant = mockPlant();
        UserPlant mockUserPlant = mockUserPlant();
        CombinedPlantDto mockCombinedPlantDto = new CombinedPlantDto();
        mockCombinedPlantDto.setPlantDetails(mockPlant);
        mockCombinedPlantDto.setUserPlantDetails(mockUserPlant);

        given(userPlantService.getUserPlantById(eq(userPlantId), eq(currentUserId))).willReturn(mockCombinedPlantDto);

        mockMvc.perform(get("/api/userplants/{userPlantId}", userPlantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plantDetails.id").value(mockCombinedPlantDto.getPlantDetails().getId()))
                .andExpect(jsonPath("$.userPlantDetails.id").value(mockCombinedPlantDto.getUserPlantDetails().getId()));
    }

    //tests for updating plant info
    @Test
    void whenUpdateUserPlantPicture_thenReturnUpdatedUserPlant() throws Exception {
        Long userPlantId = 1L;
        String newPicture = "http://example.com/new-picture.jpg";
        UserPlant updatedUserPlant = new UserPlant();
        updatedUserPlant.setPictureUrl(newPicture);

        given(userPlantService.updateUserPlantPicture(eq(userPlantId), anyLong(), eq(newPicture))).willReturn(updatedUserPlant);

        mockMvc.perform(put("/api/userplants/{userPlantId}/picture", userPlantId)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newPicture))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pictureUrl").value(newPicture));
    }

    @Test
    void whenUpdatePlantName_thenReturnUpdatedUserPlant() throws Exception {
        Long userPlantId = 1L;
        String newName = "New Plant Name";
        UserPlant updatedUserPlant = new UserPlant();
        updatedUserPlant.setCustomName(newName);

        given(userPlantService.updatePlantName(eq(userPlantId), anyLong(), eq(newName))).willReturn(updatedUserPlant);

        mockMvc.perform(put("/api/userplants/{userPlantId}/name", userPlantId)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customName").value(newName));
    }

    //test for deleting saved plant
    @Test
    void whenDeleteUserPlant_thenReturnOkStatus() throws Exception {
        Long userPlantId = 1L;
        Long currentUserId = 1L;

        doNothing().when(userPlantService).deleteUserPlant(eq(userPlantId), eq(currentUserId));

        mockMvc.perform(delete("/api/userplants/{userPlantId}", userPlantId))
                .andExpect(status().isOk());

        verify(userPlantService).deleteUserPlant(eq(userPlantId), eq(currentUserId));
    }

    //tests regarding watering events
    @Test
    void whenAddWateringEvent_thenReturnNewWateringEvent() throws Exception {
        Long userPlantId = 1L;
        WateringEventDto wateringEventDto = new WateringEventDto();
        PlantWateringHistory newWateringEvent = new PlantWateringHistory();

        given(plantWateringHistoryService.addWateringHistory(eq(userPlantId), any(WateringEventDto.class), anyLong()))
                .willReturn(newWateringEvent);

        mockMvc.perform(post("/api/userplants/{userPlantId}/watering", userPlantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wateringEventDto)))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetWateringLog_thenReturnWateringLog() throws Exception {
        Long userPlantId = 1L;
        UserPlant userPlant = new UserPlant(); // set properties
        List<PlantWateringHistory> wateringHistory = Arrays.asList(new PlantWateringHistory()); // set properties

        given(userPlantService.getUserPlantForWateringLog(eq(userPlantId), anyLong())).willReturn(userPlant);
        given(plantWateringHistoryService.getAllWateringHistoryByPlantId(eq(userPlantId), anyLong())).willReturn(wateringHistory);

        mockMvc.perform(get("/api/userplants/{userPlantId}/watering-log", userPlantId))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateWateringLog_thenReturnUpdatedLog() throws Exception {
        Long logId = 1L;
        LocalDateTime newWateringDateTime = LocalDateTime.now();
        PlantWateringHistory updatedLog = new PlantWateringHistory();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String newWateringDateTimeAsString = objectMapper.writeValueAsString(newWateringDateTime);

        given(plantWateringHistoryService.updateWateringLog(eq(logId), any(LocalDateTime.class), anyLong()))
                .willReturn(updatedLog);

        mockMvc.perform(put("/api/userplants/watering-logs/{logId}", logId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newWateringDateTimeAsString))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteWateringHistory_thenRespondWithNoContent() throws Exception {
        Long logId = 1L;

        doNothing().when(plantWateringHistoryService).deleteWateringHistory(eq(logId), anyLong());

        mockMvc.perform(delete("/api/userplants/watering-logs/{logId}", logId))
                .andExpect(status().isNoContent());

        verify(plantWateringHistoryService).deleteWateringHistory(eq(logId), anyLong());
    }


    //helper methods
    private UserPlant createUserPlant(Long id, String customName, String pictureUrl) {
        UserPlant userPlant = new UserPlant();
        userPlant.setId(id);
        userPlant.setCustomName(customName);
        userPlant.setPictureUrl(pictureUrl);
        return userPlant;
    }

    private Plant mockPlant() {
        Plant mockPlant = new Plant();
        mockPlant.setId("123");
        mockPlant.setCommonName("Mock Plant");
        mockPlant.setScientificName(Arrays.asList("Scientific Name 1", "Scientific Name 2"));
        mockPlant.setType("Herb");
        mockPlant.setIndoor(true);
        mockPlant.setWatering("Moderate");
        mockPlant.setDefaultImage(new Plant.DefaultImage("http://example.com/image.jpg"));
        mockPlant.setCareLevel("Easy");
        mockPlant.setDescription("Mock plant description");
        mockPlant.setSunlight(new HashSet<>(Arrays.asList("Partial Sun", "Shade")));
        mockPlant.setDepthWaterRequirement(null);
        mockPlant.setWateringTime(new Plant.WateringTime("Days", "3"));
        mockPlant.setImageUrl("http://example.com/image.jpg");

        return mockPlant;
    }

    private UserPlant mockUserPlant() {
        UserPlant mockUserPlant = new UserPlant();
        mockUserPlant.setId(1L);
        mockUserPlant.setApiPlantId("plant123");
        mockUserPlant.setCustomName("My Favorite Plant");
        mockUserPlant.setPictureUrl("http://example.com/plant.jpg");

        return mockUserPlant;
    }

}
