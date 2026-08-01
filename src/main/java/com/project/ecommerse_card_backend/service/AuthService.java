package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.dto.userdto.AuthRequest;
import com.project.ecommerse_card_backend.dto.userdto.AuthResponse;
import com.project.ecommerse_card_backend.dto.userdto.RegisterRequest;
import com.project.ecommerse_card_backend.dto.userdto.UserResponse;
import com.project.ecommerse_card_backend.entity.User;
import com.project.ecommerse_card_backend.enums.Role;
import com.project.ecommerse_card_backend.exception.ResourceNotFoundException;
import com.project.ecommerse_card_backend.exception.UserAlreadyExistsException;
import com.project.ecommerse_card_backend.repository.UserRepository;
import com.project.ecommerse_card_backend.secutiy.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user authentication and registration.
 * Handles secure password hashing, role assignment, and JWT token generation.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user in the system.
     * Validates if the email is unique, securely hashes the password using BCrypt,
     * and automatically assigns roles (ADMIN if requested, otherwise standard USER).
     *
     * @param request The registration payload containing user details.
     * @return UserResponse containing the newly created user's public details.
     */
    public UserResponse registerUser(RegisterRequest request) {
        // 1. Check if email exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("A user with the email " + request.getEmail() + " already exists.");
        }

        // 2. Create User Entity
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        
        // Hash the password securely!
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        // 3. Save to database
        User savedUser = userRepository.save(user);

        // 4. Return DTO mapping
        return UserResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    /**
     * Authenticates a user and generates a JWT token for session-less access.
     *
     * @param request The login payload containing email and password.
     * @return AuthResponse containing the signed JWT token.
     */
    public AuthResponse loginUser(AuthRequest request) {
        // 1. Authenticate using Spring Security's AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Fetch the user (we know they exist and password is correct if the line above passes)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 3. Generate token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        // 4. Return the token in our AuthResponse wrapper
        return new AuthResponse(token);
    }
}
