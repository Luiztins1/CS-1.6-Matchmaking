package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnamed.matchmaking.cs16_matchmaking.configTest.TestSecurityConfig;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.controller.LobbyController;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.dto.LobbyRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.service.LobbyService;
import com.unnamed.matchmaking.cs16_matchmaking.match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
import lombok.With;
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

@WebMvcTest(LobbyController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class LobbyControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LobbyService lobbyService;

    Player player;

    Match match;

    LobbyRequestDTO lobbyRequestDTO;
    Lobby lobbyInit;

    @BeforeEach
    void setUp(){
        match = createDefaultMatch(new ArrayList<>());
        lobbyInit = createDefaultLobby(match, new ArrayList<>());
        lobbyInit.setMatchLobby(match);

        player = createDefaultPlayer();
        player.setMatch(match);
        player.setLobby(lobbyInit);

        lobbyRequestDTO = createRequestDefaultLobby(lobbyInit);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindAll() throws Exception{
        when(lobbyService.findAllLobby())
                .thenReturn(List.of(lobbyInit));

        mvc.perform(get("/api/v1/lobbies")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForFindAll() throws Exception{
        mvc.perform(get("/api/v1/lobbies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNoContentFindAll() throws Exception{
        when(lobbyService.findAllLobby())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/lobbies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddListPlayer() throws Exception{
        when(lobbyService.addListLobbyPlayer(Mockito.eq(match.getId()), Mockito.eq(player.getId())))
                .thenReturn(lobbyInit);

        mvc.perform(post("/api/v1/lobbies")
                        .param("matchId", match.getId().toString())
                        .param("playerId", player.getId().toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForAddListPlayer() throws Exception{
        mvc.perform(post("/api/v1/lobbies")
                        .param("matchId", match.getId().toString())
                        .param("playerId", player.getId().toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNoContentAddListPlayer() throws Exception{
        mvc.perform(post("/api/v1/lobbies")
                        .param("matchId", match.getId().toString())
                        .param("playerId", player.getId().toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRemoveListLobbyPlayer() throws Exception{
        doNothing().when(lobbyService)
                .removeListLobbyPlayer(Mockito.eq(match.getId()),
                        Mockito.eq(player.getId()));

        mvc.perform(delete("/api/v1/lobbies")
                        .with(csrf())
                .param("matchId", match.getId().toString())
                .param("playerId", player.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForRemoveListLobbyPlayer() throws Exception{
        mvc.perform(delete("/api/v1/lobbies")
                        .with(csrf())
                        .param("matchId", match.getId().toString())
                        .param("playerId", player.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindByIdLobby() throws Exception{
        when(lobbyService.findByIdLobby(Mockito.eq(lobbyInit.getId())))
                .thenReturn(Optional.of(lobbyInit));

        mvc.perform(get("/api/v1/lobbies/{id}", lobbyInit.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForFindByIdLobby() throws Exception{
        mvc.perform(get("/api/v1/lobbies/{id}", lobbyInit.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotFoundFindByIdLobby() throws Exception{
        mvc.perform(get("/api/v1/lobbies/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private Player createDefaultPlayer(){
        return new Player(
                UUID.randomUUID(),
                "Luiz",
                Ranking.BRONZE_1,
                0,
                0,
                "Brasil",
                null,
                null,
                null
        );
    }

    private Match createDefaultMatch(List<Player> playerList){
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

    private LobbyRequestDTO createRequestDefaultLobby(Lobby lobby){
        List<UUID> playerList = lobby.getListLobbyPlayer()
                .stream()
                .map(Player::getId)
                .toList();

        return new LobbyRequestDTO(
                lobby.getId(),
                lobby.getName(),
                lobby.getMatchLobby().getId(),
                playerList
        );
    }
}
