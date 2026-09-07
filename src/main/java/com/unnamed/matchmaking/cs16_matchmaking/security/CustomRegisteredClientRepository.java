package com.unnamed.matchmaking.cs16_matchmaking.security;

import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;
import com.unnamed.matchmaking.cs16_matchmaking.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService clientService;

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client client = clientService.findClientByLogin(clientId);

        if(client == null ) return null;

        return RegisteredClient
                .withId(client.getClientLogin())
                .clientSecret(client.getClientPassword())
                .scope(client.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri(client.getRedirectUri())
                .tokenSettings(null)
                .clientSettings(null)
                .build();
    }
}
