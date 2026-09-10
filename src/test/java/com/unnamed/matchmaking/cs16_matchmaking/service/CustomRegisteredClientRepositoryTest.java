package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;
import com.unnamed.matchmaking.cs16_matchmaking.client.service.ClientService;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ClientNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.security.CustomRegisteredClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CustomRegisteredClientRepositoryTest {

    @InjectMocks
    CustomRegisteredClientRepository customRegisteredClientRepository;

    @Mock
    TokenSettings tokenSettings;

    @Mock
    ClientSettings clientSettings;

    @Mock
    ClientService clientService;

    Client client;

    @BeforeEach
    void setUp(){
        client = createDefaultClient();
    }

    @Test
    void shouldFindByClientId(){
        when(clientService.findClientByLogin(Mockito.eq(client.getClientLogin())))
                .thenReturn(client);

        RegisteredClient registeredClient = customRegisteredClientRepository.findByClientId(client.getClientLogin());

        assertThat(registeredClient).isNotNull();
        assertThat(registeredClient.getClientId()).isEqualTo("Test");
        assertThat(registeredClient.getClientSecret()).isEqualTo("test123");
        assertThat(registeredClient.getScopes()).contains("ADMIN");
        assertThat(registeredClient.getRedirectUris()).contains("http://localhost:8080/callback");

        verify(clientService, times(1))
                .findClientByLogin(client.getClientLogin());
    }

    @Test
    void shouldReturnWhenNotFoundFindByClientId(){
        assertThrows(ClientNotFoundException.class, () ->{
            customRegisteredClientRepository.findByClientId("Luiz");
        }, "Cliente não encontrado.");
    }

    @Test
    void shouldFindById(){
        when(clientService.findClientByLogin(Mockito.eq(client.getClientLogin())))
                .thenReturn(client);

        RegisteredClient registeredClient = customRegisteredClientRepository.findById(client.getClientLogin());

        assertThat(registeredClient).isNotNull();
        assertThat(registeredClient.getClientId()).isEqualTo(client.getClientLogin());

        verify(clientService, times(1))
                .findClientByLogin(client.getClientLogin());
    }

    @Test
    void shouldReturnWhenNotFoundById(){
        assertThrows(ClientNotFoundException.class, () ->{
            customRegisteredClientRepository.findById("Luiz");
        }, "Cliente não encontrado.");
    }

    private Client createDefaultClient(){
        return new Client(
                UUID.randomUUID(),
                "Test",
                "test123",
                "http://localhost:8080/callback",
                "ADMIN"
        );
    }
}
