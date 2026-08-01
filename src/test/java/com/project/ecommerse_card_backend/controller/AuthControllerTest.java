package com.project.ecommerse_card_backend.controller;

import com.project.ecommerse_card_backend.dto.userdto.AuthRequest;
import com.project.ecommerse_card_backend.dto.userdto.AuthResponse;
import com.project.ecommerse_card_backend.dto.userdto.RegisterRequest;
import com.project.ecommerse_card_backend.dto.userdto.UserResponse;
import com.project.ecommerse_card_backend.enums.Role;
import com.project.ecommerse_card_backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    // ==========================================
    // loginUser() — Positive
    // ==========================================
    @Test
    void testLoginUser_Success() {
        AuthResponse authResponse = new AuthResponse("mock-jwt-token");
        when(authService.loginUser(any(AuthRequest.class))).thenReturn(authResponse);

        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        ResponseEntity<AuthResponse> response = authController.loginUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("mock-jwt-token", response.getBody().getToken());
    }

    // ==========================================
    // registerUser() — Positive
    // ==========================================
    @Test
    void testRegisterUser_Success() {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.USER)
                .build();

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(userResponse);

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        ResponseEntity<UserResponse> response = authController.registerUser(request);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertEquals("test@test.com", response.getBody().getEmail());
    }
}
