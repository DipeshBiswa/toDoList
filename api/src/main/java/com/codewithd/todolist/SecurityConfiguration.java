package com.codewithd.todolist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // 1. Explicitly disable CSRF for EVERYTHING to test (standard for REST APIs)
        .csrf(csrf -> csrf.disable()) 
        
        // 2. Enable CORS
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        
        // 3. Update the matcher to be more broad for testing
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/user/**").permitAll() 
            .requestMatchers("/user/create").permitAll() // Added explicit path
            .anyRequest().permitAll() // Temporarily allow everything to confirm connection
        );

    return http.build();
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}
