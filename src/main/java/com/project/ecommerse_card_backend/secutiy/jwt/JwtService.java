package com.project.ecommerse_card_backend.secutiy.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom security filter executed once per incoming HTTP request.
 * Responsible for intercepting requests, validating JWTs, and setting up the Spring Security context.
 */
@Service
@RequiredArgsConstructor
public class JwtService extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Skip filtering if there's no Bearer token
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        // Extract token and claims
        String token = authHeader.substring(7);
        String email = jwtUtil.extractMail(token);
        Long userId = jwtUtil.extractUserId(token);

        // If claims are valid and the user isn't already authenticated in this session
        if(userId != null && email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),null,userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authenticated user in the security context
            SecurityContextHolder.getContext().setAuthentication(authToken);
            
            // Pass the userId down to controllers via request attributes
            request.setAttribute("userId",userId);
        }

        filterChain.doFilter(request,response);


    }
}
