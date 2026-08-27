package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.Controller.MatchInteractionController;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.dto.MatchInteractionRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.dto.MatchInteractionResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.service.MatchInteractionService;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(MatchInteractionController.class)
@ActiveProfiles("test")
public class MatchInteractionControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MatchInteractionService matchInteractionService;

    MatchInteractionRequestDTO matchInteractionRequestDTO;
    Player playerInit;
    Match matchInit;
    Lobby lobbyInit;

    @BeforeEach()
    void setUp(){
        matchInit = createDefaultMatch();
        lobbyInit = createDefaultLobby(matchInit, new ArrayList<>());
        matchInit.setLobbyMatch(lobbyInit);

        playerInit = createDefaultPlayer();
        playerInit.setMatch(matchInit);
        playerInit.setLobby(matchInit.getLobbyMatch());

        matchInteractionRequestDTO = createDefaultMatchInteraction(InteractionEvent.ENTER, matchInit.getId(), playerInit.getId());
    }

    @Test
    void shouldHandlerMatchInteraction() throws Exception{
        when(matchInteractionService.handlerMatchInteraction(matchInteractionRequestDTO))
                .thenReturn(true);

        mvc.perform(post("/api/v1/match-interactions")
                        .content(objectMapper.writeValueAsString(matchInteractionRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void shouldReturnFalseHandlerMatchInteraction() throws Exception{
        MatchInteractionRequestDTO request = createDefaultMatchInteraction(InteractionEvent.MATCHING, matchInit.getId(), playerInit.getId());
        when(matchInteractionService.handlerMatchInteraction(request))
                .thenReturn(false);

        mvc.perform(post("/api/v1/match-interactions")
                        .content(objectMapper.writeValueAsString(matchInteractionRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

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

    private MatchInteractionRequestDTO createDefaultMatchInteraction(InteractionEvent event, UUID matchId, UUID playerId){
        return new MatchInteractionRequestDTO(
                event,
                matchId,
                playerId
        );
    }

    private Match createDefaultMatch(){
        return new Match(
                UUID.randomUUID(),
                "Test",
                GameMap.DE_DUST_2,
                MatchState.COLD,
                TypeMatch.COMPETITIVE,
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

}
