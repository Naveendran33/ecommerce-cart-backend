package com.project.ecommerse_card_backend.controller;

import com.project.ecommerse_card_backend.dto.userdto.AuthRequest;
import com.project.ecommerse_card_backend.dto.userdto.AuthResponse;
import com.project.ecommerse_card_backend.dto.userdto.RegisterRequest;
import com.project.ecommerse_card_backend.dto.userdto.UserResponse;
import com.project.ecommerse_card_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.loginUser(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
