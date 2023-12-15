package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.entities.UserPlant;
import com.plantcare.plantcareassistant.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //checks if the password matches the pw in the database

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if(isEmailTaken(user.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalStateException("Invalid credentials");
        }
        return user;
    }

    //checks if email is taken
    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

//needs current password in order to update password
    public User updateUserPassword(Long id, String newPassword, String currentPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalStateException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User updateUserEmail(Long id, String newEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    //users need to verify password before deleting acc
    public String deleteUser(Long id, String password) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("This user does not exist"));

        if(passwordEncoder.matches(password, user.getPasswordHash())) {
            userRepository.deleteById(id);
            return "Account deleted";
        }
        else {
            return "Passwords do not match";
        }

    }
}