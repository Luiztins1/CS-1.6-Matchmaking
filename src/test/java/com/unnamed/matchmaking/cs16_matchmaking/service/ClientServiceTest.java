package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.client.dto.ClientRequestDto;
import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;
import com.unnamed.matchmaking.cs16_matchmaking.client.repository.ClientRepository;
import com.unnamed.matchmaking.cs16_matchmaking.client.service.ClientService;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ClientNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ClientServiceTest {

    @InjectMocks
    ClientService clientService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Captor
    ArgumentCaptor<Client> argumentCaptor;

    ClientRequestDto requestDto;
    Client clientInit;

    @BeforeEach
    void setUp(){
        clientInit = createDefaultClient();
        requestDto = createDefaultRequestDto(clientInit);
    }

    @Test
    void shouldRegisterClient(){
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation ->
                invocation.getArgument(0));

        when(clientRepository.save(Mockito.any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Client registered = clientService.registerClient(requestDto);

        verify(clientRepository, times(1))
                .save(argumentCaptor.capture());

        Client clientCaptor = argumentCaptor.getValue();

        assertThat(clientCaptor).isNotNull();
        assertThat(clientCaptor.getClientLogin()).isEqualTo("Test");
        assertThat(clientCaptor.getClientPassword()).isEqualTo("test123");
        assertThat(clientCaptor.getRedirectUri()).isEqualTo("testuri");
        assertThat(clientCaptor.getScope()).isEqualTo("ADMIN");

        verify(passwordEncoder, times(1))
                .encode("test123");
    }

    @Test
    void shouldReturnWhenDtoResourceNotFountExceptionRegisterClient(){
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.registerClient(null);
        }, "Dto vazio.");
    }

    @Test
    void shouldReturnDuplicateExceptionWhetherClientForDuplicate(){
        ClientRequestDto request = createDefaultRequestDto(clientInit);

        when(clientRepository.existsByClientLoginOrId(Mockito.eq(request.clientLogin()), Mockito.eq(request.id())))
                .thenReturn(true);

        assertThrows(DuplicateException.class, () ->{
           clientService.registerClient(requestDto);
        }, "Cliente já registrado.");

        verify(clientRepository, times(1))
                .existsByClientLoginOrId(Mockito.eq(request.clientLogin()), Mockito.eq(request.id()));
    }

    @Test
    void shouldFindByLogin(){
        when(clientRepository.findByClientLogin(anyString()))
                .thenReturn(Optional.of(clientInit));

        Client client = clientService.findClientByLogin(clientInit.getClientLogin());

        assertThat(client).isNotNull();

        verify(clientRepository, times(1))
                .findByClientLogin(clientInit.getClientLogin());
    }

    @Test
    void shouldNotFoundLogin(){
        when(clientRepository.findByClientLogin(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () ->{
            clientService.findClientByLogin(anyString());
        });
    }

    private Client createDefaultClient(){
        return new Client(
                UUID.randomUUID(),
                "Test",
                "test123",
                "testuri",
                "ADMIN"
        );
    }

    private ClientRequestDto createDefaultRequestDto(Client client){
        return new ClientRequestDto(
                client.getId(),
                client.getClientLogin(),
                client.getClientPassword(),
                client.getRedirectUri(),
                client.getScope()
        );
    }
}
