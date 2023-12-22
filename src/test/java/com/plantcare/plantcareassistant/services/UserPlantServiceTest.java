package com.plantcare.plantcareassistant.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantcare.plantcareassistant.dto.CombinedPlantDto;
import com.plantcare.plantcareassistant.dto.UserPlantDto;
import com.plantcare.plantcareassistant.entities.Plant;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.repository.UserPlantRepository;
import com.plantcare.plantcareassistant.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserPlantServiceTest {
    @Mock
    private UserPlantRepository userPlantRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PlantService plantService;
    @InjectMocks
    private UserPlantService userPlantService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAllUserPlantsByUserId_thenReturnUserPlantsList() {
        Long userId = 1L;
        List<UserPlant> userPlants = Arrays.asList(new UserPlant(), new UserPlant());

        when(userPlantRepository.findByUserId(userId)).thenReturn(userPlants);

        List<UserPlant> result = userPlantService.getAllUserPlantsByUserId(userId);

        assertEquals(2, result.size());
        Mockito.verify(userPlantRepository).findByUserId(userId);
    }

    @Test
    public void whenAddUserPlant_thenSaveUserPlant() {
        Long userId = 1L;
        UserPlantDto userPlantDto = new UserPlantDto();
        User user = new User();
        user.setId(userId);
        UserPlant userPlant = new UserPlant();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userPlantRepository.save(any(UserPlant.class))).thenReturn(userPlant);

        UserPlant result = userPlantService.addUserPlant(userId, userPlantDto);

        assertNotNull(result);
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(userPlantRepository).save(any(UserPlant.class));
    }

    @Test
    public void whenDeleteUserPlant_thenDelete() {
        Long userPlantId = 1L;
        Long userId = 1L;
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(userId);
        userPlant.setUser(user);

        when(userPlantRepository.findById(userPlantId)).thenReturn(Optional.of(userPlant));

        userPlantService.deleteUserPlant(userPlantId, userId);

        Mockito.verify(userPlantRepository).deleteById(userPlantId);
    }

    @Test
    public void whenPlantIsSavedByUser_thenReturnTrue() {
        String plantId = "plant123";
        Long userId = 1L;
        UserPlant userPlant = new UserPlant();
        userPlant.setApiPlantId(plantId);

        List<UserPlant> userPlants = List.of(userPlant);
        when(userPlantRepository.findByUserId(userId)).thenReturn(userPlants);

        boolean isSaved = userPlantService.isPlantSavedByUser(plantId, userId);

        assertTrue(isSaved);
    }

    @Test
    public void whenPlantIsNotSavedByUser_thenReturnFalse() {
        String plantId = "plant123";
        Long userId = 1L;
        String differentPlantId = "differentPlantId";
        UserPlant userPlant = new UserPlant();
        userPlant.setApiPlantId(differentPlantId);

        List<UserPlant> userPlants = List.of(userPlant);
        when(userPlantRepository.findByUserId(userId)).thenReturn(userPlants);

        boolean isSaved = userPlantService.isPlantSavedByUser(plantId, userId);

        assertFalse(isSaved);
    }

    @Test
    public void whenUpdateUserPlantPicture_thenUpdatePicture() {
        Long userPlantId = 1L;
        Long userId = 1L;
        String newPicture = "newPictureUrl";
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(userId);
        userPlant.setUser(user);

        when(userPlantRepository.findById(userPlantId)).thenReturn(Optional.of(userPlant));
        when(userPlantRepository.save(any(UserPlant.class))).thenReturn(userPlant);

        UserPlant updatedUserPlant = userPlantService.updateUserPlantPicture(userPlantId, userId, newPicture);

        assertEquals(newPicture, updatedUserPlant.getPictureUrl());
        Mockito.verify(userPlantRepository).save(userPlant);
    }

    @Test
    public void whenUpdateUserPlantPictureWithWrongUser_thenThrowException() {
        Long userPlantId = 1L;
        Long wrongUserId = 2L;
        String newPicture = "newPictureUrl";
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(1L);
        userPlant.setUser(user);

        when(userPlantRepository.findById(userPlantId)).thenReturn(Optional.of(userPlant));

        assertThrows(AccessDeniedException.class, () -> {
            userPlantService.updateUserPlantPicture(userPlantId, wrongUserId, newPicture);
        });
    }

    @Test
    public void whenUpdatePlantName_thenUpdateName() {
        Long userPlantId = 1L;
        Long userId = 1L;
        String newName = "newPlantName";
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(userId);
        userPlant.setUser(user);

        when(userPlantRepository.findById(userPlantId)).thenReturn(Optional.of(userPlant));
        when(userPlantRepository.save(any(UserPlant.class))).thenReturn(userPlant);

        UserPlant updatedUserPlant = userPlantService.updatePlantName(userPlantId, userId, newName);

        assertEquals(newName, updatedUserPlant.getPictureUrl());
        Mockito.verify(userPlantRepository).save(userPlant);
    }

    @Test
    public void whenUpdatePlantNameWithWrongUser_thenThrowException() {
        Long userPlantId = 1L;
        Long wrongUserId = 2L;
        String newName = "newPlantName";
        UserPlant userPlant = new UserPlant();
        User user = new User();
        user.setId(1L);
        userPlant.setUser(user);

        when(userPlantRepository.findById(userPlantId)).thenReturn(Optional.of(userPlant));

        assertThrows(AccessDeniedException.class, () -> {
            userPlantService.updatePlantName(userPlantId, wrongUserId, newName);
        });
    }

}
