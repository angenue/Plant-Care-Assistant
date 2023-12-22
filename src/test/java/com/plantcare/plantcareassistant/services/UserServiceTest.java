package com.plantcare.plantcareassistant.services;

import com.plantcare.plantcareassistant.dto.UserDto;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        // Initializes mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenCreateUserWithValidDetails_thenSucceed() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPasswordHash("StrongPassword123!");
        userDto.setConfirmPasswordHash("StrongPassword123!");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash("encodedPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPasswordHash());
    }

    @Test
    public void whenCreateUserWithNonMatchingPasswords_thenThrowException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("user@example.com");
        userDto.setPasswordHash("Password123!");
        userDto.setConfirmPasswordHash("DifferentPassword123!");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    public void whenCreateUserWithWeakPassword_thenThrowException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("user@example.com");
        userDto.setPasswordHash("weak");
        userDto.setConfirmPasswordHash("weak");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Password does not meet the strength requirements", exception.getMessage());
    }

    @Test
    public void whenCreateUserWithTakenEmail_thenThrowException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("taken@example.com");
        userDto.setPasswordHash("Password123!");
        userDto.setConfirmPasswordHash("Password123!");

        when(userRepository.findByEmail("taken@example.com")).thenReturn(Optional.of(new User()));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Email already taken", exception.getMessage());
    }


}
