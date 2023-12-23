package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.dto.RecentlySearchedPlantsDto;
import com.plantcare.plantcareassistant.dto.UserPlantDto;
import com.plantcare.plantcareassistant.dto.WateringEventDto;
import com.plantcare.plantcareassistant.entities.*;
import com.plantcare.plantcareassistant.repository.RecentlySearchedRepository;
import com.plantcare.plantcareassistant.repository.UserPlantRepository;
import com.plantcare.plantcareassistant.repository.UserRepository;
import com.plantcare.plantcareassistant.repository.WateringHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
public class PlantWateringHistoryServiceTest {
    @Mock
    private WateringHistoryRepository wateringHistoryRepository;

    @Mock
    private UserPlantRepository userPlantRepository;

    @InjectMocks
    private PlantWateringHistoryService service;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenAddWateringHistory_thenSaveEvent() {
        Long userPlantId = 1L;
        Long userId = 1L;
        LocalDateTime expectedDateTime = LocalDateTime.now();
        WateringEventDto dto = new WateringEventDto();
        dto.setWateringDate(expectedDateTime);
        dto.setWaterAmount(5.0);

        UserPlant userPlant = new UserPlant();
        userPlant.setId(userPlantId);
        User user = new User();
        user.setId(userId);
        userPlant.setUser(user);

        when(userPlantRepository.findById(userPlantId)).thenReturn(Optional.of(userPlant));
        when(wateringHistoryRepository.findByUserPlantIdOrderByWateringDateDesc(userPlantId)).thenReturn(new ArrayList<>());
        when(wateringHistoryRepository.save(any(PlantWateringHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlantWateringHistory result = service.addWateringHistory(userPlantId, dto, userId);

        assertNotNull(result);
        assertEquals(dto.getWateringDate(), result.getWateringDate());
        assertEquals(dto.getWaterAmount(), result.getWaterAmount());
        Mockito.verify(wateringHistoryRepository).save(any(PlantWateringHistory.class));
    }

    @Test
    public void whenUpdateNotificationSettings_thenNoException() {
        Long userPlantId = 1L;
        Long userId = 1L;
        UserPlantDto userPlantDto = new UserPlantDto();
        userPlantDto.setWateringFrequency(7);
        userPlantDto.setFrequencyUnit("days");

        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(userId);
        userPlant.setUser(user);
        userPlant.setFrequencyUnit("days");

        when(userPlantRepository.findById(userPlantId)).thenReturn(Optional.of(userPlant));
        when(userPlantRepository.save(any(UserPlant.class))).thenReturn(userPlant);

        UserPlant updatedUserPlant = service.updateNotificationSettings(userPlantId, userPlantDto, userId);

        assertNotNull(updatedUserPlant);

    }

    @Test
    public void whenUpdateWateringLog_thenUpdateSuccessfully() {
        Long logId = 1L;
        Long userId = 1L;
        LocalDateTime newDateTime = LocalDateTime.now();
        PlantWateringHistory log = new PlantWateringHistory();
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(userId);
        userPlant.setUser(user);
        log.setUserPlant(userPlant);

        when(wateringHistoryRepository.findById(logId)).thenReturn(Optional.of(log));
        when(wateringHistoryRepository.save(any(PlantWateringHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlantWateringHistory updatedLog = service.updateWateringLog(logId, newDateTime, userId);

        assertNotNull(updatedLog);
        assertEquals(newDateTime, updatedLog.getWateringDate());
        Mockito.verify(wateringHistoryRepository).save(log);
    }

    @Test
    public void whenUpdateWateringLogWithNonexistentLog_thenThrowEntityNotFoundException() {
        Long logId = 1L;
        Long userId = 1L;
        LocalDateTime newDateTime = LocalDateTime.now();

        when(wateringHistoryRepository.findById(logId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.updateWateringLog(logId, newDateTime, userId);
        });
    }

    @Test
    public void whenUpdateWateringLogWithUnauthorizedUser_thenThrowAccessDeniedException() {
        Long logId = 1L;
        Long userId = 1L;
        LocalDateTime newDateTime = LocalDateTime.now();
        PlantWateringHistory log = new PlantWateringHistory();
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(2L); // Different user ID
        userPlant.setUser(user);
        log.setUserPlant(userPlant);

        when(wateringHistoryRepository.findById(logId)).thenReturn(Optional.of(log));

        assertThrows(AccessDeniedException.class, () -> {
            service.updateWateringLog(logId, newDateTime, userId);
        });
    }

    @Test
    public void whenDeleteWateringHistory_thenDeleteSuccessfully() {
        Long id = 1L;
        Long userId = 1L;
        PlantWateringHistory log = new PlantWateringHistory();
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(userId);
        userPlant.setUser(user);
        log.setUserPlant(userPlant);

        when(wateringHistoryRepository.findById(id)).thenReturn(Optional.of(log));

        service.deleteWateringHistory(id, userId);

        Mockito.verify(wateringHistoryRepository).deleteById(id);
    }

    @Test
    public void whenDeleteWateringHistoryWithNonexistentLog_thenThrowEntityNotFoundException() {
        Long id = 1L;
        Long userId = 1L;

        when(wateringHistoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.deleteWateringHistory(id, userId);
        });
    }

    @Test
    public void whenDeleteWateringHistoryWithUnauthorizedUser_thenThrowAccessDeniedException() {
        Long id = 1L;
        Long userId = 1L;
        PlantWateringHistory log = new PlantWateringHistory();
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(2L); // Different user ID
        userPlant.setUser(user);
        log.setUserPlant(userPlant);

        when(wateringHistoryRepository.findById(id)).thenReturn(Optional.of(log));

        assertThrows(AccessDeniedException.class, () -> {
            service.deleteWateringHistory(id, userId);
        });
    }


}
