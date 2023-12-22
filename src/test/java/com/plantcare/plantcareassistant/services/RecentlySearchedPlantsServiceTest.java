package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.dto.RecentlySearchedPlantsDto;
import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.RecentlySearchedPlants;
import com.plantcare.plantcareassistant.repository.RecentlySearchedRepository;
import com.plantcare.plantcareassistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.plantcare.plantcareassistant.dto.UserPlantDto;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.repository.UserPlantRepository;
import com.plantcare.plantcareassistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RecentlySearchedPlantsServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PlantService plantService;

    @Mock
    private RecentlySearchedRepository recentlySearchedRepository;

    @InjectMocks
    private RecentlySearchedPlantsService recentlySearchedPlantsService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenAddRecentlySearched_thenSaveNewSearch() {
        Long userId = 1L;
        String apiPlantId = "plant123";
        User mockUser = new User();
        Plant mockPlant = new Plant();
        mockPlant.setCommonName("Mock Plant");
        mockPlant.setImageUrl("http://mockplantimage.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(plantService.getPlantDetails(apiPlantId)).thenReturn(mockPlant);
        when(recentlySearchedRepository.findByUserIdOrderByTimestampDesc(userId)).thenReturn(Collections.emptyList());

        recentlySearchedPlantsService.addRecentlySearched(userId, apiPlantId);

        Mockito.verify(recentlySearchedRepository).save(any(RecentlySearchedPlants.class));
    }

    @Test
    public void whenAddRecentlySearchedAndExceedsLimit_thenRemoveOldest() {
        Long userId = 1L;
        String apiPlantId = "plant123";
        User mockUser = new User();
        Plant mockPlant = new Plant();
        mockPlant.setCommonName("Mock Plant");
        mockPlant.setImageUrl("http://mockplantimage.com");

        // Mocking 16 searches (1 more than the limit of 15)
        List<RecentlySearchedPlants> mockSearches = IntStream.range(0, 16)
                .mapToObj(i -> new RecentlySearchedPlants())
                .collect(Collectors.toList());

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(plantService.getPlantDetails(apiPlantId)).thenReturn(mockPlant);
        when(recentlySearchedRepository.findByUserIdOrderByTimestampDesc(userId)).thenReturn(mockSearches);

        recentlySearchedPlantsService.addRecentlySearched(userId, apiPlantId);

        Mockito.verify(recentlySearchedRepository).delete(mockSearches.get(15)); // Verify deletion of the oldest search
        Mockito.verify(recentlySearchedRepository).save(any(RecentlySearchedPlants.class));
    }

    @Test
    public void whenGetRecentlySearchedPlants_thenReturnDtoList() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        List<RecentlySearchedPlants> mockSearches = Arrays.asList(
                createMockRecentlySearchedPlants("plant1", "Plant 1", "url1", now),
                createMockRecentlySearchedPlants("plant2", "Plant 2", "url2", now.minusDays(1))
        );

        when(recentlySearchedRepository.findByUserIdOrderByTimestampDesc(userId)).thenReturn(mockSearches);

        List<RecentlySearchedPlantsDto> result = recentlySearchedPlantsService.getRecentlySearchedPlants(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Plant 1", result.get(0).getPlantName());
        assertEquals("url1", result.get(0).getPlantImageUrl());
        assertEquals("Plant 2", result.get(1).getPlantName());
        assertEquals("url2", result.get(1).getPlantImageUrl());
    }

    private RecentlySearchedPlants createMockRecentlySearchedPlants(String apiPlantId, String plantName, String pictureUrl, LocalDateTime timestamp) {
        RecentlySearchedPlants search = new RecentlySearchedPlants();
        search.setApiPlantId(apiPlantId);
        search.setPlantName(plantName);
        search.setPictureUrl(pictureUrl);
        search.setTimestamp(timestamp);
        return search;
    }

    @Test
    public void whenDeleteMultipleSearches_thenRepositoryMethodCalled() {
        List<Long> searchIds = Arrays.asList(1L, 2L, 3L);

        recentlySearchedPlantsService.deleteMultipleSearches(searchIds);

        Mockito.verify(recentlySearchedRepository).deleteAllByIdIn(searchIds);
    }
}
