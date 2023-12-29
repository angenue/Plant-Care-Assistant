package com.plantcare.plantcareassistant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull(message = "is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotNull(message = "is required")
    private String passwordHash;
    @NotNull(message = "is required")
    private String confirmPasswordHash;
}
