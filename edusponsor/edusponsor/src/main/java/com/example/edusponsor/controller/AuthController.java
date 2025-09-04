package com.example.edusponsor.controller;

import com.example.edusponsor.dto.login.LoginRequest;
import com.example.edusponsor.dto.register.InstitutionRegister;
import com.example.edusponsor.dto.register.SponsorRegister;
import com.example.edusponsor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/institution")
    public ResponseEntity<?> registerInstitution(@RequestBody InstitutionRegister request) {
        return authService.registerInstitution(request);
    }

    @PostMapping("/register/sponsor")
    public ResponseEntity<?> registerSponsor(@RequestBody SponsorRegister request) {
        return authService.registerSponsor(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.authenticateUser(request);
    }
}
