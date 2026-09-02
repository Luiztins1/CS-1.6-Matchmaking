package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.unnamed.matchmaking.cs16_matchmaking.configTest.TestSecurityConfig;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.mainController.rest.SearchMatchController;
import com.unnamed.matchmaking.cs16_matchmaking.match.dto.MatchResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(SearchMatchController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class SearchMatchControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MatchRepository matchRepository;

    Match matchInit;
    Lobby lobbyInit;

    @BeforeEach
    void setUp(){
        matchInit = createDefaultMatch();
        lobbyInit = createDefaultLobby(matchInit, new ArrayList<>());
        matchInit.setLobbyMatch(lobbyInit);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldSearchMatchMap() throws Exception{

        when(matchRepository.findByMapEquals(Mockito.eq(matchInit.getMap())))
                .thenReturn(List.of(matchInit));

        mvc.perform(get("/api/v1/search-matchs")
                        .with(csrf())
                .param("map", matchInit.getMap().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbiddenWhenUserNotHasPermissionForSearchMatchMap() throws Exception{
        mvc.perform(get("/api/v1/search-matchs")
                        .with(csrf())
                        .param("map", matchInit.getMap().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundSearchMatchMap() throws Exception{
        mvc.perform(get("/api/v1/search-matchs")
                        .with(csrf())
                        .param("map", matchInit.getMap().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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

    private Lobby createDefaultLobby(Match match, List<Player> playerList){
        return new Lobby(
                UUID.randomUUID(),
                "Test",
                match,
                playerList
        );
    }
}
