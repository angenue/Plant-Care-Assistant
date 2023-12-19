package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.dto.UserPlantDto;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.repository.UserPlantRepository;
import com.plantcare.plantcareassistant.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPlantService {

    private final UserPlantRepository userPlantRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserPlantService(UserPlantRepository userPlantRepository, UserRepository userRepository) {
        this.userPlantRepository = userPlantRepository;
        this.userRepository = userRepository;
    }

    public List<UserPlant> getAllUserPlantsByUserId(Long userId) {
        return userPlantRepository.findByUserId(userId);
    }

    public UserPlant getUserPlantById(Long id) {
        return userPlantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plant not found with id " + id));
    }

    public UserPlant addUserPlant(Long userId, UserPlantDto userPlantDto) {
        UserPlant userPlant = new UserPlant();

        //retrieving user id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserPlant not found with id " + userPlantDto.getUserId()));

        userPlant.setUser(user);
        userPlant.setApiPlantId(userPlantDto.getApiPlantId());
        userPlant.setCustomName(userPlantDto.getCustomName());
        userPlant.setPictureUrl(userPlantDto.getPictureUrl());

        return userPlantRepository.save(userPlant);
    }



    public UserPlant updateUserPlantPicture(Long userPlantId, Long userId, String newPicture) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new EntityNotFoundException("UserPlant not found with id " + userPlantId));

        // Check if the userPlant belongs to the authenticated user
        if (!userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }

        userPlant.setPictureUrl(newPicture);
        return userPlantRepository.save(userPlant);
    }


    public UserPlant updatePlantName(Long userPlantId, Long userId, String newName) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new EntityNotFoundException("UserPlant not found with id " + userPlantId));

        if (!userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }

        userPlant.setPictureUrl(newName);

        return userPlantRepository.save(userPlant);
    }

    //checks to see if user plant belongs to logged in user
    public void deleteUserPlant(Long userPlantId, Long userId) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new EntityNotFoundException("Plant not found with id " + userPlantId));

        if (!userPlant.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this plant");
        }

        userPlantRepository.deleteById(userPlantId);
    }

}
