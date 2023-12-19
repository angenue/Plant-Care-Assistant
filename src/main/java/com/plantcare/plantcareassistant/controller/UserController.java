package com.plantcare.plantcareassistant.controller;

import com.plantcare.plantcareassistant.dto.UserDto;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{id}/update-password")
    public ResponseEntity<User> updatePassword(@PathVariable Long id,
                                               @RequestParam String newPassword,
                                               @RequestParam String currentPassword) {
        User updatedUser = userService.updateUserPassword(id, newPassword, currentPassword);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/update-email")
    public ResponseEntity<User> updateEmail(@PathVariable Long id, @RequestParam String newEmail) {
        User updatedUser = userService.updateUserEmail(id, newEmail);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestParam String password) {
        String response = userService.deleteUser(id, password);
        return ResponseEntity.ok(response);
    }

    // Other endpoints as needed...

}
