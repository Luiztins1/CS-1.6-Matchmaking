package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.controller.UserAuthController;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.dto.UserAuthRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.entity.UserAuth;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.service.UserAuthService;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserAuthController.class)
@ActiveProfiles("test")
public class UserAuthControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserAuthService userAuthService;

    UserAuthRequestDTO requestDTO;
    UserAuth userAuth;

    @BeforeEach
    void setUp(){
        userAuth = createDefaultUserAuth();
        requestDTO = createDefaultUserAuthRequestDto(userAuth);
    }

    @Test
    void shouldRegisterUser() throws Exception{
        when(userAuthService.registerUserAuth(Mockito.any(UserAuthRequestDTO.class)))
                .thenReturn(userAuth);

        mvc.perform(post("/api/v1/auth/users")
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void shouldInvalidRequestRegisterUser() throws Exception{
        UserAuthRequestDTO request = invalidRequest(userAuth);

        mvc.perform(post("/api/v1/auth/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void shouldNullRequestRegisterUser() throws Exception{
        mvc.perform(post("/api/v1/auth/users")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldConflictWhenAlreadyExistUserAuth() throws Exception{
        when(userAuthService.registerUserAuth(Mockito.any(UserAuthRequestDTO.class)))
                .thenThrow(new DuplicateException("Usuário já cadastrado."));

        mvc.perform(post("/api/v1/auth/users")
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void shouldFindAll() throws Exception{
        when(userAuthService.findAll())
                .thenReturn(List.of(userAuth));

        mvc.perform(get("/api/v1/auth/users")
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturnListEmptyFindAll() throws Exception{
        when(userAuthService.findAll())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/auth/users")
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldUpdateUserAuth() throws Exception{
        UserAuthRequestDTO request = new UserAuthRequestDTO(
                userAuth.getId(),
                "Luiz",
                "Kepler-186f",
                List.of("USER")
        );

        when(userAuthService.updateUserAuth(Mockito.eq(userAuth.getId()), Mockito.any(UserAuthRequestDTO.class)))
                .thenReturn(Optional.of(userAuth));

        mvc.perform(put("/api/v1/auth/users/{id}/update", userAuth.getId())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldUpdateUserWhenNotFound() throws Exception{
        mvc.perform(put("/api/v1/auth/users/{id}/update", userAuth.getId())
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldUpdateUserWhenIsNull() throws Exception{
        mvc.perform(put("/api/v1/auth/users/{id}/update", userAuth.getId())
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldInvalidRequestUpdateUser() throws Exception{
        UserAuthRequestDTO request = invalidRequest(userAuth);

        mvc.perform(put("/api/v1/auth/users/{id}/update", userAuth.getId())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldCancelUserAuth() throws Exception{
        doNothing().when(userAuthService)
                .cancelUserAuth(Mockito.eq(userAuth.getLogin()));

        mvc.perform(delete("/api/v1/auth/users/{login}/cancel", userAuth.getLogin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldFindById() throws Exception{
        when(userAuthService.findById(Mockito.eq(userAuth.getId())))
                .thenReturn(Optional.of(userAuth));

        mvc.perform(get("/api/v1/auth/users/{id}/find", userAuth.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void shouldFindByIdWhenIsEmpty() throws Exception{
        mvc.perform(get("/api/v1/auth/users/{id}/find", userAuth.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldFindByLogin() throws Exception{
        when(userAuthService.findByLogin(Mockito.eq(userAuth.getLogin())))
                .thenReturn(Optional.of(userAuth));

        mvc.perform(get("/api/v1/auth/users/{login}/login", userAuth.getLogin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldFindByLoginWhenIsEmpty() throws Exception{
        mvc.perform(get("/api/v1/auth/users/{login}/login", userAuth.getLogin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private UserAuth createDefaultUserAuth(){
        return new UserAuth(
                UUID.randomUUID(),
                "admin",
                "admin123",
                List.of("ADMIN")
        );
    }

    private UserAuthRequestDTO createDefaultUserAuthRequestDto(UserAuth userAuth){
        if(userAuth == null) throw new ResourceNotFoundException("Usuário não encontrado.");
        return new UserAuthRequestDTO(
                userAuth.getId(),
                userAuth.getLogin(),
                userAuth.getPassword(),
                userAuth.getRoles()
        );
    }

    private UserAuthRequestDTO invalidRequest(UserAuth userAuth){
        return new UserAuthRequestDTO(
                userAuth.getId(),
                null,
                userAuth.getPassword(),
                null
        );
    }
}
