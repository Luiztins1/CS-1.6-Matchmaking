package com.unnamed.matchmaking.cs16_matchmaking.security;

import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.entity.UserAuth;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.service.UserAuthService;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.LoginNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserAuthService userAuthService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var login = authentication.getName();
        var password = authentication.getCredentials().toString();

        UserAuth userAuth = userAuthService.findByLogin(login)
                .orElseThrow(() -> new LoginNotFoundException("Login não encontrado."));

        var passwordMatch = passwordEncoder.matches(password, userAuth.getPassword());

        if(passwordMatch) return new CustomAuthentication(userAuth);

       throw new UsernameNotFoundException("Usuário não encontrado");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
