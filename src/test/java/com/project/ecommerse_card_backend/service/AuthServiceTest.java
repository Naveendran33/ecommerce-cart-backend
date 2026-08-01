package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.dto.userdto.AuthRequest;
import com.project.ecommerse_card_backend.dto.userdto.AuthResponse;
import com.project.ecommerse_card_backend.dto.userdto.RegisterRequest;
import com.project.ecommerse_card_backend.entity.User;
import com.project.ecommerse_card_backend.exception.UserAlreadyExistsException;
import com.project.ecommerse_card_backend.repository.UserRepository;
import com.project.ecommerse_card_backend.secutiy.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@gmail.com");
        testUser.setPasswordHash("hashed_password");
    }

    // ==========================================
    // registerUser() — Positive
    // ==========================================
    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@gmail.com");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");

        when(userRepository.findByEmail("new@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        var response = authService.registerUser(request);

        assertNotNull(response);
        assertEquals("new@gmail.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ==========================================
    // registerUser() — Negative: Email exists
    // ==========================================
    @Test
    void testRegisterUser_EmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(testUser));

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.registerUser(request);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    // ==========================================
    // loginUser() — Positive
    // ==========================================
    @Test
    void testLoginUser_Success() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // successful auth returns authentication object, we don't care about it
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken("test@gmail.com", 1L)).thenReturn("fake-jwt-token");

        AuthResponse response = authService.loginUser(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
    }

    // ==========================================
    // loginUser() — Negative: Wrong credentials
    // ==========================================
    @Test
    void testLoginUser_InvalidCredentials() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            authService.loginUser(request);
        });
    }
}
