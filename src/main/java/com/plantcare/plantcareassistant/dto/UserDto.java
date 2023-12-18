package com.plantcare.plantcareassistant.dto;

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
    @NonNull
    private String email;
    @NonNull
    private String passwordHash;
    @NonNull
    private String confirmPasswordHash;
}
