package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.controller.LobbyController;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.dto.LobbyRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.service.LobbyService;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(LobbyController.class)
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
    void shouldFindAll() throws Exception{
        when(lobbyService.findAllLobby())
                .thenReturn(List.of(lobbyInit));

        mvc.perform(get("/api/v1/lobbies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldNoContentFindAll() throws Exception{
        when(lobbyService.findAllLobby())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/lobbies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldRemoveListLobbyPlayer() throws Exception{
        doNothing().when(lobbyService)
                .removeListLobbyPlayer(Mockito.eq(match.getId()),
                        Mockito.eq(player.getId()));

        mvc.perform(delete("/api/v1/lobbies")
                .param("matchId", match.getId().toString())
                .param("playerId", player.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldFindByIdLobby() throws Exception{
        when(lobbyService.findByIdLobby(Mockito.eq(lobbyInit.getId())))
                .thenReturn(Optional.of(lobbyInit));

        mvc.perform(get("/api/v1/lobbies/{id}", lobbyInit.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
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
