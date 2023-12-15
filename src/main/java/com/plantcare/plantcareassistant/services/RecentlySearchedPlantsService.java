package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.entities.RecentlySearchedPlants;
import com.plantcare.plantcareassistant.repository.RecentlySearchedRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecentlySearchedPlantsService {
    private final RecentlySearchedRepository recentlySearchedRepository;

    public RecentlySearchedPlantsService(RecentlySearchedRepository recentlySearchedRepository) {
        this.recentlySearchedRepository = recentlySearchedRepository;
    }

    public RecentlySearchedPlants addRecentlySearched(Long userId, String apiPlantId) {
        LocalDateTime now = LocalDateTime.now();
        RecentlySearchedPlants newSearch = new RecentlySearchedPlants(userId, apiPlantId, now);
        recentlySearchedRepository.save(newSearch);

        // Check if there are more than 30 entries and delete the oldest
        List<RecentlySearchedPlants> searches = recentlySearchedRepository.findByUserIdOrderByTimestampDesc(userId);
        if (searches.size() > 30) {
            RecentlySearchedPlants oldestSearch = searches.get(searches.size() - 1);
            recentlySearchedRepository.delete(oldestSearch);
        }

        return newSearch;
    }

    public List<RecentlySearchedPlants> getAllHistoryByUserId(Long id) {
        //sort the history by descending order
        return recentlySearchedRepository.findByUserIdOrderByTimestampDesc(id);
    }

    //users can delete multiple search history at once
    public void deleteMultipleSearches(List<Long> searchIds) {
        recentlySearchedRepository.deleteAllByIdIn(searchIds);
    }

}
