package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnamed.matchmaking.cs16_matchmaking.client.controller.ClientController;
import com.unnamed.matchmaking.cs16_matchmaking.client.dto.ClientRequestDto;
import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;
import com.unnamed.matchmaking.cs16_matchmaking.client.service.ClientService;
import com.unnamed.matchmaking.cs16_matchmaking.configTest.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;
import java.util.UUID;

@WebMvcTest(ClientController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class ClientControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ClientService clientService;

    ClientRequestDto clientRequestDto;
    Client client;

    @BeforeEach
    void setUp(){
        client = createDefaultClient();
        clientRequestDto = createDefaultRequestDto(client);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterClient() throws Exception{
        when(clientService.registerClient(Mockito.any(ClientRequestDto.class)))
                .thenReturn(client);

        mvc.perform(post("/api/v1/clients")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(clientRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(client.getId().toString().trim()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientLogin").value("Test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientPassword").value("test123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.redirectUri").value("testuri"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.scope").value("ADMIN"));

    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForRegisterClient() throws Exception{
        mvc.perform(post("/api/v1/clients")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(clientRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldInvalidRequest() throws Exception{
        ClientRequestDto invalid = invalidRequest();

        mvc.perform(post("/api/v1/clients")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(invalid))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindByClientLogin() throws Exception{
        when(clientService.findClientByLogin(Mockito.eq(client.getClientLogin())))
                .thenReturn(client);

        mvc.perform(get("/api/v1/clients/{login}", client.getClientLogin())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForFindByClientLogin() throws Exception{
        mvc.perform(get("/api/v1/clients/{login}", client.getClientLogin())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNoContentFindByClientLogin() throws Exception{
        mvc.perform(get("/api/v1/clients/{login}", client.getClientLogin())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
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

    private ClientRequestDto invalidRequest(){
        return new ClientRequestDto(
                UUID.randomUUID(),
                null,
                "test123",
                null,
                "Test"

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
