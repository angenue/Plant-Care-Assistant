package com.plantcare.plantcareassistant.controller;

import com.plantcare.plantcareassistant.dto.UserDto;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userDto);
        return ResponseEntity.status(201).body(user);
    }

    @PutMapping("/update-password")
    public ResponseEntity<User> updatePassword(
                                               @RequestParam String newPassword,
                                               @RequestParam String currentPassword) {
        Long id = getCurrentUserId();
        User updatedUser = userService.updateUserPassword(id, newPassword, currentPassword);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update-email")
    public ResponseEntity<User> updateEmail(@RequestParam String newEmail) {
        Long currentUserId = getCurrentUserId();
        User updatedUser = userService.updateUserEmail(currentUserId, newEmail);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String password) {
        Long currentUserId = getCurrentUserId();
        String response = userService.deleteUser(currentUserId, password);
        return ResponseEntity.ok(response);
    }

    // Utility method to get the current user's ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        return user.getId();
    }

}
