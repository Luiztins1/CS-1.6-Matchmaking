package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnamed.matchmaking.cs16_matchmaking.configTest.TestSecurityConfig;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.player.controller.PlayerController;
import com.unnamed.matchmaking.cs16_matchmaking.player.dto.PlayerRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.player.service.PlayerService;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import static org.hamcrest.CoreMatchers.nullValue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PlayerController.class)
@Import(TestSecurityConfig.class)
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
    @WithMockUser(roles = "ADMIN")
    void shouldSavePlayer() throws Exception{
        when(playerService.savePlayer(Mockito.any(PlayerRequestDTO.class)))
                .thenReturn(playerInit);

        mvc.perform(post("/api/v1/players")
                        .with(csrf())
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("Luiz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value(Ranking.BRONZE_1.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kills").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deaths").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("Brasil"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastConnection").value(nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchId").value(nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lobbyId").value(nullValue()))
                .andExpect(MockMvcResultMatchers.header().exists("location"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForSavePlayer() throws Exception{
        mvc.perform(post("/api/v1/players")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldInvalidRequestSavePlayer() throws Exception{
        PlayerRequestDTO request = createInvalidRequest();

        mvc.perform(post("/api/v1/players")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNullRequestSavePlayer() throws Exception{
        mvc.perform(post("/api/v1/players")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

   @Test
   @WithMockUser(roles = "ADMIN")
   void shouldConflictWhenAlreadyExistPlayer() throws Exception{
        when(playerService.savePlayer(Mockito.any(PlayerRequestDTO.class)))
                .thenThrow(new DuplicateException("Player já cadastrado"));

        mvc.perform(post("/api/v1/players")
                        .with(csrf())
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
   }

   @Test
   @WithMockUser(roles = "ADMIN")
   void shouldFindAllPlayer() throws Exception{
       when(playerService.findAllPlayer())
               .thenReturn(List.of(playerInit));

       mvc.perform(get("/api/v1/players")
                       .with(csrf())
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
   }

   @Test
   @WithMockUser(roles = "USER")
   void shouldForbiddenWhenUserNotHasPermissionForFindAllPlayer() throws Exception{
       mvc.perform(get("/api/v1/players")
                       .with(csrf())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
   }

   @Test
   @WithMockUser(roles = "ADMIN")
   void shouldFindAllPlayerWhenIsEmpty() throws Exception{
       when(playerService.findAllPlayer())
               .thenReturn(List.of());

       mvc.perform(get("/api/v1/players")
                       .with(csrf())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNoContent());
   }

   @Test
   @WithMockUser(roles = "ADMIN")
   void shouldUpdatePlayer() throws Exception{
        when(playerService.updatePlayer(Mockito.eq(playerInit.getId()),
                Mockito.any(PlayerRequestDTO.class)))
                .thenReturn(Optional.of(playerInit));

        mvc.perform(put("/api/v1/players/{id}/update", playerInit.getId())
                        .with(csrf())
                .content(objectMapper.writeValueAsString(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
   }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForUpdatePlayer() throws Exception {
        mvc.perform(put("/api/v1/players/{id}/update", playerInit.getId())
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


   @Test
   @WithMockUser(roles = "ADMIN")
   void shouldUpdatePlayerWhenNotFound() throws Exception{
        Player player = createDefaultPlayer();
        mvc.perform(put("/api/v1/players/{id}/update", player.getId())
                        .with(csrf())
                       .content(objectMapper.writeValueAsString(requestDTO))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
   }

   @Test
   @WithMockUser(roles = "ADMIN")
   void shouldUpdatePlayerInvalidRequest() throws Exception{
       PlayerRequestDTO request = createInvalidRequest();

       mvc.perform(put("/api/v1/players/{id}/update", playerInit.getId())
                       .with(csrf())
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
   }

   @Test
   @WithMockUser(roles = "ADMIN")
   void shouldNullRequestUpdatePlayer() throws Exception{
       mvc.perform(put("/api/v1/players/{id}/update", playerInit.getId())
                       .with(csrf())
                       .content(objectMapper.writeValueAsString(null))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
   }

   @Test
   @WithMockUser(roles = "ADMIN")
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

        mvc.perform(put("/api/v1/players/{id}/update-relationships", player.getId())
                        .with(csrf())
                        .param("matchId", match.getId().toString())
                        .param("lobbyId", lobby.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
   }

   @Test
   @WithMockUser(roles = "USER")
   void shouldForbiddenWhenUserNotHasPermissionForUpdateRelationships() throws Exception{
       mvc.perform(put("/api/v1/players/{id}/update-relationships", UUID.randomUUID())
                       .with(csrf())
                       .param("matchId", UUID.randomUUID().toString())
                       .param("lobbyId", UUID.randomUUID().toString())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
   }

   @Test
   @WithMockUser(roles = "ADMIN")
   void shouldNotFoundUpdateRelationships() throws Exception{
        mvc.perform(put("/api/v1/players/{id}/update-relationships", UUID.randomUUID())
                        .with(csrf())
                        .param("matchId", UUID.randomUUID().toString())
                        .param("lobbyId", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeletePlayer() throws Exception{
        doNothing().when(playerService)
                .deletePlayer(playerInit.getId());

        mvc.perform(delete("/api/v1/players/{id}/delete", playerInit.getId())
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForDeletePlayer() throws Exception{
        mvc.perform(delete("/api/v1/players/{id}/delete", playerInit.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldFindById() throws Exception{
        when(playerService.findByIdPlayer(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(playerInit));

        mvc.perform(get("/api/v1/players/{id}", playerInit.getId())
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForFindById() throws Exception{
        mvc.perform(get("/api/v1/players/{id}", playerInit.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotFoundFindById() throws Exception{
        mvc.perform(get("/api/v1/players/{id}", UUID.randomUUID())
                        .with(csrf())
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
