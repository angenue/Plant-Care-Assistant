package com.plantcare.plantcareassistant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantcare.plantcareassistant.dto.UserDto;
import com.plantcare.plantcareassistant.entities.User;
import com.plantcare.plantcareassistant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    //to authorize user for testing
    private void setupMockSecurityContext() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("user@example.com", "password", authorities);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("user@example.com");

        when(userService.getUserByEmail("user@example.com")).thenReturn(mockUser);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Test
    void whenRegisterUser_thenReturnUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPasswordHash("StrongPassword123!");
        userDto.setConfirmPasswordHash("StrongPassword123!");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash("encodedPassword");

        given(userService.createUser(any(UserDto.class))).willReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    void whenUpdatePassword_thenReturnUpdatedUser() throws Exception {
        setupMockSecurityContext();
        String newPassword = "newPassword123";
        String currentPassword = "currentPassword123";
        User updatedUser = new User();

        given(userService.updateUserPassword(anyLong(), eq(newPassword), eq(currentPassword))).willReturn(updatedUser);

        mockMvc.perform(put("/api/users/update-password")
                        .param("newPassword", newPassword)
                        .param("currentPassword", currentPassword))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateEmail_thenReturnUpdatedUser() throws Exception {
        setupMockSecurityContext();
        String newEmail = "newemail@example.com";
        User updatedUser = new User(); 

        given(userService.updateUserEmail(anyLong(), eq(newEmail))).willReturn(updatedUser);

        mockMvc.perform(put("/api/users/update-email")
                        .param("newEmail", newEmail))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteUser_thenReturnDeletionResponse() throws Exception {
        setupMockSecurityContext();
        String password = "userPassword";
        String deletionResponse = "Account deleted";

        given(userService.deleteUser(anyLong(), eq(password))).willReturn(deletionResponse);

        mockMvc.perform(delete("/api/users/delete")
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string(deletionResponse));
    }



}
