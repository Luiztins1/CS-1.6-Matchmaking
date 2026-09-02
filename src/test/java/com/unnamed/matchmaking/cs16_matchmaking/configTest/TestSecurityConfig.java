package com.unnamed.matchmaking.cs16_matchmaking.configTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurity(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login").permitAll();
                    authorize.requestMatchers("/api/v1/matches/**").hasRole("ADMIN");
                    authorize.requestMatchers("/api/v1/lobbies/**").hasRole("ADMIN");
                    authorize.requestMatchers("/api/v1/match-interactions/**").hasRole("ADMIN");
                    authorize.requestMatchers("/api/v1/players").hasRole("ADMIN");
                    authorize.requestMatchers("/api/v1/search-matchs/**").hasRole("ADMIN");
                    authorize.requestMatchers("/api/v1/players/**").hasRole("ADMIN");
                    authorize.requestMatchers("/api/v1/auth/users/**").hasRole("ADMIN");
                    authorize.anyRequest().authenticated();
                })

                .build();
    }
}
