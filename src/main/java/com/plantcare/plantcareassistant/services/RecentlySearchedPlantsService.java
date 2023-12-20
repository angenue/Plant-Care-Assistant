package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.dto.RecentlySearchedPlantsDto;
import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.RecentlySearchedPlants;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.repository.RecentlySearchedRepository;
import com.plantcare.plantcareassistant.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecentlySearchedPlantsService {
    private final RecentlySearchedRepository recentlySearchedRepository;
    private  final UserRepository userRepository;
    private final PlantService plantService;

    public RecentlySearchedPlantsService(RecentlySearchedRepository recentlySearchedRepository, UserRepository userRepository, PlantService plantService) {
        this.recentlySearchedRepository = recentlySearchedRepository;
        this.userRepository = userRepository;
        this.plantService = plantService;
    }

    public void addRecentlySearched(Long userId, String apiPlantId) {
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Plant plant = plantService.getPlantDetails(apiPlantId);

        RecentlySearchedPlants newSearch = new RecentlySearchedPlants();
        newSearch.setUser(user);
        newSearch.setApiPlantId(apiPlantId);
        newSearch.setTimestamp(now);
        newSearch.setPlantName(plant.getName());
        newSearch.setPictureUrl(plant.getImageUrl());
        recentlySearchedRepository.save(newSearch);

        // Check if there are more than 15 entries and delete the oldest
        List<RecentlySearchedPlants> searches = recentlySearchedRepository.findByUserIdOrderByTimestampDesc(userId);
        if (searches.size() > 15) {
            RecentlySearchedPlants oldestSearch = searches.get(searches.size() - 1);
            recentlySearchedRepository.delete(oldestSearch);
        }

    }


    public List<RecentlySearchedPlantsDto> getRecentlySearchedPlants(Long userId) {
        List<RecentlySearchedPlants> searches = recentlySearchedRepository.findByUserIdOrderByTimestampDesc(userId);
        return searches.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RecentlySearchedPlantsDto convertToDto(RecentlySearchedPlants search) {
        RecentlySearchedPlantsDto dto = new RecentlySearchedPlantsDto();
        dto.setApiPlantId(search.getApiPlantId());
        dto.setPlantName(search.getPlantName());
        dto.setPlantImageUrl(search.getPictureUrl());
        dto.setTimestamp(search.getTimestamp());
        return dto;
    }


    //users can delete multiple search history at once
    public void deleteMultipleSearches(List<Long> searchIds) {
        recentlySearchedRepository.deleteAllByIdIn(searchIds);
    }

}
