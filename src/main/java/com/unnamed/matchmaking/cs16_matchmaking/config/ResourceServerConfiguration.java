package com.unnamed.matchmaking.cs16_matchmaking.config;

import com.unnamed.matchmaking.cs16_matchmaking.security.LoginSocialSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class ResourceServerConfiguration {

    private final LoginSocialSuccessHandler loginSocialSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->{
                    authorize.requestMatchers("/login/**", "/oauth2/**").permitAll();
                    authorize.requestMatchers("/api/v1/matches/**").hasRole("ADMIN");
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(oauth ->
                        oauth.successHandler(loginSocialSuccessHandler))

                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(jwt ->
                                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        var convert = new JwtAuthenticationConverter();
        convert.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return convert;
    }
}
