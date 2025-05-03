package com.plantcare.plantcareassistant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Logged in successfully");
    }

}
