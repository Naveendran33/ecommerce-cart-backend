package com.project.ecommerse_card_backend.secutiy.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;


/**
 * Utility class for generating, parsing, and validating JWT tokens.
 * Handles signing keys and claims extraction (like email and userId).
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Generates a signed JWT token valid for 1 hour.
     * Includes standard claims (subject, issuedAt, expiration) and custom claims (userId).
     *
     * @param mail The user's email address (used as the subject).
     * @param id The user's database ID (used as a custom claim).
     * @return The signed JWT string.
     */
    public String generateToken(String mail, Long id) {
        return Jwts.builder()
                .subject(mail)
                .claim("userId", id)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .issuedAt(new Date(Instant.now().toEpochMilli()))
                .expiration(new Date(Instant.now().toEpochMilli() + 60 * 60 * 1000L)) // 1 hour
                .compact();
    }

    public boolean validateToken(String token) {
        return tokenParser(token) != null;
    }

    public String extractMail(String token) {
        Claims claim = tokenParser(token);
        return claim.getSubject();
    }

    public Long extractUserId(String token) {
        Claims claim = tokenParser(token);
        return claim.get("userId", Long.class);
    }

    private Claims tokenParser(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
