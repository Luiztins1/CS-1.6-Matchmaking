package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.controller.PlayerController;
import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.service.PlayerService;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PlayerController.class)
@ActiveProfiles("test")
public class PlayerControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PlayerService playerService;

    PlayerResponseDTO responseDTO;
    PlayerRequestDTO requestDTO;
    Player playerInit;


    @BeforeEach
    void setUp(){
        playerInit = createDefaultPlayer();
        responseDTO = createDefaultResponse(playerInit);
        requestDTO = createDefaultRequest(playerInit);
    }

    @Test
    void shouldSavePlayer() throws Exception{
        when(playerService.savePlayer(Mockito.any(PlayerRequestDTO.class)))
                .thenReturn(playerInit);

        mvc.perform(post("/api/v1/players")
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("location"));
    }

    @Test
    void shouldInvalidRequestSavePlayer() throws Exception{
        PlayerRequestDTO request = createInvalidRequest();

        mvc.perform(post("/api/v1/players")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldNullRequestSavePlayer() throws Exception{
        mvc.perform(post("/api/v1/players")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

   @Test
   void shouldConflictWhenAlreadyExistPlayer() throws Exception{
        when(playerService.savePlayer(Mockito.any(PlayerRequestDTO.class)))
                .thenThrow(new DuplicateException("Player já cadastrado"));

        mvc.perform(post("/api/v1/players")
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
   }

   @Test
   void shouldFindAllPlayer() throws Exception{
       when(playerService.findAllPlayer())
               .thenReturn(List.of(playerInit));

       mvc.perform(get("/api/v1/players")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
   }

   @Test
   void shouldFindAllPlayerWhenIsEmpty() throws Exception{
       when(playerService.findAllPlayer())
               .thenReturn(List.of());

       mvc.perform(get("/api/v1/players")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNoContent());
   }

   @Test
   void shouldUpdatePlayer() throws Exception{
        when(playerService.updatePlayer(Mockito.eq(playerInit.getId()),
                Mockito.any(PlayerRequestDTO.class)))
                .thenReturn(Optional.of(playerInit));

        mvc.perform(put("/api/v1/players/{id}/update", playerInit.getId())
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
   }

   @Test
   void shouldUpdatePlayerWhenNotFound() throws Exception{
        Player player = createDefaultPlayer();
        mvc.perform(put("/api/v1/players/{id}/update", player.getId())
                       .content(objectMapper.writeValueAsString(requestDTO))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
   }

   @Test
   void shouldUpdatePlayerInvalidRequest() throws Exception{
       PlayerRequestDTO request = createInvalidRequest();

       mvc.perform(put("/api/v1/players/{id}/update", playerInit.getId())
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
   }

   @Test
   void shouldNullRequestUpdatePlayer() throws Exception{
       mvc.perform(put("/api/v1/players/{id}/update", playerInit.getId())
                       .content(objectMapper.writeValueAsString(null))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
   }

   @Test
   void shouldUpdateRelationships() throws Exception{
        Match match = createDefaultMatch(new ArrayList<>());
        Lobby lobby = createDefaultLobby(match, new ArrayList<>());
        match.setLobbyMatch(lobby);

        Player player = createDefaultPlayer();
        player.setMatch(match);
        player.setLobby(lobby);

        when(playerService.updateRelationships(
                Mockito.eq(player.getId()),
                Mockito.eq(match.getId()),
                Mockito.eq(lobby.getId())))
                .thenReturn(Optional.of(player));

        mvc.perform(put("/api/v1/players/{id}/update-relationships",
                        player.getId())
                        .param("matchId", match.getId().toString())
                        .param("lobbyId", lobby.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
   }
   @Test
   void shouldNotFoundUpdateRelationships() throws Exception{
        mvc.perform(put("/api/v1/players/{id}/update-relationships",
                        UUID.randomUUID())
                        .param("matchId", UUID.randomUUID().toString())
                        .param("lobbyId", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldDeletePlayer() throws Exception{
        doNothing().when(playerService)
                .deletePlayer(playerInit.getId());

        mvc.perform(delete("/api/v1/players/{id}/delete", playerInit.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldFindById() throws Exception{
        when(playerService.findByIdPlayer(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(playerInit));

        mvc.perform(get("/api/v1/players/{id}", playerInit.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldNotFoundFindById() throws Exception{
        mvc.perform(get("/api/v1/players/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

   private PlayerRequestDTO createInvalidRequest(){
       return new PlayerRequestDTO(
               null,
               null,
               Ranking.BRONZE_1,
               null,
               0,
               " ",
               null,
               null
               ,null
       );
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

    private PlayerRequestDTO createDefaultRequest(Player player){
        return new PlayerRequestDTO(
                player.getId(),
                player.getNickname(),
                player.getRank(),
                player.getKills(),
                player.getDeaths(),
                player.getCountry(),
                player.getLastConnection(),
                null,
                null
        );
    }

    private PlayerResponseDTO createDefaultResponse(Player player){
        return new PlayerResponseDTO(
                player.getId(),
                player.getNickname(),
                player.getRank(),
                player.getKills(),
                player.getDeaths(),
                player.getCountry(),
                player.getLastConnection(),
                null,
                null
        );
    }
}
