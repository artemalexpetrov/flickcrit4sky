package com.flickcrit.app.config;

import com.flickcrit.app.infrastructure.security.service.TokenService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.mockito.Mockito.mock;

@TestConfiguration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class TestSecurityConfig {

    @Bean
    TokenService tokenService() {
        return mock(TokenService.class);
    }

    @Bean
    public SecurityFilterChain securityWebFilterChain(
        HttpSecurity http) throws Exception {

        HttpSecurity httpSecurity = http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/movies/**").permitAll()
                .anyRequest()
                .authenticated()
            );

        return httpSecurity.build();
    }
}
