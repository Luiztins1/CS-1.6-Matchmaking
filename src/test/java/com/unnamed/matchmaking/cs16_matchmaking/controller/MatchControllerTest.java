package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.match.controller.MatchController;
import com.unnamed.matchmaking.cs16_matchmaking.match.dto.MatchRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.match.dto.MatchResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.match.service.MatchService;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.configTest.TestSecurityConfig;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(MatchController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class MatchControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MatchService matchService;

    MatchRequestDTO matchRequestDTO;
    Match matchInit;

    Lobby lobby;

    @BeforeEach
    void setUp(){
        matchInit = createDefaultMatch();
        lobby = createDefaultLobby(matchInit, new ArrayList<>());
        matchInit.setLobbyMatch(lobby);

        matchRequestDTO = createDefaultRequestDto(matchInit);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldSaveMatch() throws Exception{
        when(matchService.saveMatch(Mockito.any(MatchRequestDTO.class)))
                .thenReturn(matchInit);

        mvc.perform(post("/api/v1/matches")
                        .with(csrf())
                .content(objectMapper.writeValueAsString(matchRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nameMatch").value("Test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.map").value(GameMap.DE_DUST_2.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchState").value(MatchState.COLD.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.typeMatch").value(TypeMatch.DEFAULT.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeMatchMap").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lobbyId").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForSaveMatch() throws Exception{
        mvc.perform(post("/api/v1/matches")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(matchRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldInvalidRequestSaveMatch() throws Exception{
        MatchRequestDTO request = createInvalidRequest();
        mvc.perform(post("/api/v1/matches")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNullRequestSaveMatch() throws Exception{
        mvc.perform(post("/api/v1/matches")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindAll() throws Exception{
        when(matchService.findAllMatch())
                .thenReturn(List.of(matchInit));

        mvc.perform(get("/api/v1/matches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForFindAll() throws Exception{
        mvc.perform(get("/api/v1/matches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNoContentFindAll() throws Exception{
        when(matchService.findAllMatch())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/matches")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateMatch() throws Exception{
        matchInit.setMatchState(MatchState.WAITING);

        when(matchService.updateMatchState(matchInit.getId(), MatchState.WAITING))
                .thenReturn(Optional.of(matchInit));

        mvc.perform(put("/api/v1/matches/{id}/match-state", matchInit.getId())
                        .with(csrf())
                        .param("nextState", String.valueOf(MatchState.WAITING))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchState").value(MatchState.WAITING.toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForUpdateMatch() throws Exception{
        mvc.perform(put("/api/v1/matches")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(matchRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotFoundUpdateMatch() throws Exception{
        mvc.perform(put("/api/v1/matches/{id}/match-state", UUID.randomUUID())
                        .param("nextState", String.valueOf(MatchState.WAITING))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteMatch() throws Exception{
        doNothing().when(matchService)
                .deleteMatch(matchInit.getId());

        mvc.perform(delete("/api/v1/matches/{id}/delete", matchInit.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForDeleteMatch() throws Exception{
        mvc.perform(delete("/api/v1/matches/{id}/delete", matchInit.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindByIdMatch() throws Exception{
        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        mvc.perform(get("/api/v1/matches/{id}", matchInit.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForFindByIdMatch() throws Exception{
        mvc.perform(get("/api/v1/matches/{id}", matchInit.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotFoundFindByIdMatch() throws Exception{
        mvc.perform(get("/api/v1/matches/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private MatchRequestDTO createInvalidRequest(){
        return new MatchRequestDTO(
                null,
                " ",
                null,
                MatchState.COLD,
                TypeMatch.DEFAULT,
                Instant.now(),
                null
        );
    }
    private Match createDefaultMatch(){
        return new Match(
                UUID.randomUUID(),
                "Test",
                GameMap.DE_DUST_2,
                MatchState.COLD,
                TypeMatch.DEFAULT,
                Instant.now(),
                null
        );
    }

    private Lobby createDefaultLobby(Match match, List<Player> playerList){
        return new Lobby(
                UUID.randomUUID(),
                "Test",
                match,
                playerList
        );
    }

    private MatchRequestDTO createDefaultRequestDto(Match match){
        return new MatchRequestDTO(
                match.getId(),
                match.getNameMatch(),
                match.getMap(),
                match.getMatchState(),
                match.getTypeMatch(),
                match.getTimeMatchMap(),
                match.getLobbyMatch().getId()
        );
    }

    private MatchResponseDTO createDefaultResponseDto(Match match){
        return new MatchResponseDTO(
                match.getId(),
                match.getNameMatch(),
                match.getMap(),
                match.getMatchState(),
                match.getTypeMatch(),
                match.getTimeMatchMap(),
                null
        );
    }
}
