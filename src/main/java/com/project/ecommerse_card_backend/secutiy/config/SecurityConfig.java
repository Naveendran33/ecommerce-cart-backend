package com.project.ecommerse_card_backend.secutiy.config;


import com.project.ecommerse_card_backend.secutiy.jwt.JwtService;
import com.project.ecommerse_card_backend.secutiy.jwt.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.SSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration class for the application.
 * Configures stateless JWT authentication, password encoding, and HTTP request authorization rules.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtService jwtService;
    private final MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    /**
     * Configures the core security filter chain.
     * - Disables CSRF (not needed for stateless REST APIs).
     * - Sets session management to STATELESS (since we use JWTs).
     * - Whitelists Auth endpoints and Swagger UI for public access.
     * - Allows public GET access to products and categories.
     * - Injects the custom JwtService filter before the standard UsernamePasswordAuthenticationFilter.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**","/v3/api-docs/**"
                        ,"/swagger-ui/**"
                        ,"/swagger-ui.html").permitAll().requestMatchers(HttpMethod.GET,"/api/products/**","/api/categories/**").permitAll().anyRequest().authenticated()).authenticationProvider(authenticationProvider()).addFilterBefore(jwtService, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}
