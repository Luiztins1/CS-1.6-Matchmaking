package com.unnamed.matchmaking.cs16_matchmaking.security;

import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.entity.UserAuth;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final UserAuth userAuth;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.userAuth.getRoles() == null) return List.of();

        return this.userAuth.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return userAuth;
    }

    @Override
    public Object getPrincipal() {
        return userAuth;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return userAuth.getLogin();
    }
}
