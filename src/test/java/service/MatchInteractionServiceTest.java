package service;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.service.LobbyService;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Match.service.MatchService;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.dto.MatchInteractionRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.dto.MatchInteractionResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.service.MatchInteractionService;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.service.PlayerService;
import com.unnamed.matchmaking.cs16_matchmaking.enums.*;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.MatchNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MatchInteractionServiceTest {

    @InjectMocks
    MatchInteractionService matchInteractionService;

    @Mock
    MatchService matchService;

    @Mock
    LobbyService lobbyService;

    @Mock
    PlayerService playerService;


    MatchInteractionRequestDTO matchInteractionEnter;
    MatchInteractionRequestDTO matchInteractionExit;

    Player playerInit;
    Match matchInit;
    Lobby lobbyInit;

    @BeforeEach
    void setUp(){
        matchInit = createDefaultMatch();
        lobbyInit = createDefaultLobby(matchInit, new ArrayList<>());
        matchInit.setLobbyMatch(lobbyInit);

        playerInit = createDefaultPlayer();
        playerInit.setMatch(matchInit);
        playerInit.setLobby(matchInit.getLobbyMatch());

        matchInteractionEnter = createDefaultMatchInteraction(InteractionEvent.ENTER, matchInit.getId(), playerInit.getId());
        matchInteractionExit = createDefaultMatchInteraction(InteractionEvent.EXIT, matchInit.getId(), playerInit.getId());
    }

    @Test
    void shouldHandlerMatchInteractionOnEnterSuccess(){
        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        when(playerService.findByIdPlayer(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        boolean result = matchInteractionService.handlerMatchInteraction(matchInteractionEnter);

        assertThat(result).isTrue();

        verify(matchService, times(2))
                .findByIdMatch(matchInit.getId());

        verify(playerService, times(1))
                .findByIdPlayer(playerInit.getId());
    }

    @Test
    void shouldMatchInteractionOnExitSuccess(){
        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        when(playerService.findByIdPlayer(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        boolean result = matchInteractionService.handlerMatchInteraction(matchInteractionExit);

        assertThat(result).isTrue();

        verify(matchService, times(2))
                .findByIdMatch(matchInit.getId());

        verify(playerService, times(1))
                .findByIdPlayer(playerInit.getId());
    }

    @Test
    void shouldReturnFalseMatchInteractionOnExit(){
        matchInit.setMatchState(MatchState.READY_MATCH);

        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        boolean result = matchInteractionService.handlerMatchInteraction(matchInteractionExit);

        assertThat(result).isFalse();

        verify(matchService, times(1))
                .findByIdMatch(matchInit.getId());
    }

    @Test
    void shouldReturnDefaultHandlerMatchInteraction(){
        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        MatchInteractionRequestDTO request = createDefaultMatchInteraction(InteractionEvent.MATCHING, matchInit.getId(), playerInit.getId());
        boolean result = matchInteractionService.handlerMatchInteraction(request);

        assertThat(result).isFalse();

        verify(matchService, times(1))
                .findByIdMatch(matchInit.getId());
    }

    @Test
    void shouldHandlerMatchInteractionWhenMatchNotFound(){
        MatchInteractionRequestDTO request = createDefaultMatchInteraction(InteractionEvent.ENTER, UUID.randomUUID(), UUID.randomUUID());
        assertThrows(MatchNotFoundException.class, () ->{
           matchInteractionService.handlerMatchInteraction(request);
        }, "Match não encontrado.");
    }

    @Test
    void shouldReturnFalseMatchInteractionOnEnter(){
        matchInit.setMatchState(MatchState.READY_MATCH);

        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        boolean result = matchInteractionService.handlerMatchInteraction(matchInteractionEnter);

        assertThat(result).isFalse();

        verify(matchService, times(1))
                .findByIdMatch(matchInit.getId());
    }

    @Test
    void shouldEnterInMatch(){
        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        when(playerService.findByIdPlayer(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        boolean result = matchInteractionService.enterInMatch(matchInit.getId(), playerInit.getId());
        assertThat(result).isTrue();

        verify(matchService, times(1))
                .findByIdMatch(matchInit.getId());

        verify(playerService, times(1))
                .findByIdPlayer(playerInit.getId());
    }

    @Test
    void shouldReturnsFalseWhenMatchDoesNotExistInEnterMatch(){
        when(playerService.findByIdPlayer(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));
        boolean result = matchInteractionService.enterInMatch(UUID.randomUUID(), playerInit.getId());

        assertThat(result).isFalse();

        verify(playerService, times(1))
                .findByIdPlayer(playerInit.getId());
    }

    @Test
    void shouldReturnsFalseWhenPlayerDoesNotExistInEnterMatch(){
        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));
        boolean result = matchInteractionService.enterInMatch(matchInit.getId(), UUID.randomUUID());

        assertThat(result).isFalse();

        verify(matchService, times(1))
                .findByIdMatch(matchInit.getId());
    }

    @Test
    void shouldResultFalseWhenBothDoesNotExistEnterInMatch(){
        boolean result = matchInteractionService.enterInMatch(UUID.randomUUID(), UUID.randomUUID());
        assertThat(result).isFalse();
    }

    @Test
    void shouldExitInMatch(){
        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        when(playerService.findByIdPlayer(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        boolean result = matchInteractionService.exitMatch(matchInit.getId(), playerInit.getId());
        assertThat(result).isTrue();

        verify(matchService, times(1))
                .findByIdMatch(matchInit.getId());

        verify(playerService, times(1))
                .findByIdPlayer(playerInit.getId());
    }

    @Test
    void shouldReturnsFalseWhenMatchDoesNotExistInExitMatch(){
        when(playerService.findByIdPlayer(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        boolean result = matchInteractionService.enterInMatch(UUID.randomUUID(), playerInit.getId());

        assertThat(result).isFalse();

        verify(playerService, times(1))
                .findByIdPlayer(playerInit.getId());
    }

    @Test
    void shouldReturnsFalseWhenPlayerDoesNotExistInExitMatch(){
        when(matchService.findByIdMatch(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        boolean result = matchInteractionService.enterInMatch(matchInit.getId(), UUID.randomUUID());

        assertThat(result).isFalse();

        verify(matchService, times(1))
                .findByIdMatch(matchInit.getId());
    }

    @Test
    void shouldResultFalseWhenBothDoesNotExistExitMatch(){
        boolean result = matchInteractionService.exitMatch(UUID.randomUUID(), UUID.randomUUID());
        assertThat(result).isFalse();
    }

    @Test
    void shouldUpdateMatchStateBasedOnLobbySizeStateCold(){
        matchInit.getLobbyMatch().setListLobbyPlayer(new ArrayList<>());
        matchInit.setMatchState(MatchState.WAITING);

        matchInteractionService.updateMatchStateBasedOnLobbySize(matchInit);

        verify(matchService, times(1))
                .updateMatchState(matchInit.getId(), MatchState.COLD);
    }

    @Test
    void shouldUpdateMatchStateBasedOnLobbySizeStateWaiting(){
        int maxPlayer = matchInit.getTypeMatch().getValue();
        List<Player> fullListPlayer = new ArrayList<>();

        fullListPlayer.add(playerInit);

        matchInit.getLobbyMatch().setListLobbyPlayer(fullListPlayer);
        matchInit.setMatchState(MatchState.COLD);

        matchInteractionService.updateMatchStateBasedOnLobbySize(matchInit);

        verify(matchService, times(1))
                .updateMatchState(matchInit.getId(), MatchState.WAITING);
    }

    @Test
    void shouldUpdateMatchStateBasedOnLobbySizeStateReadyMatch(){
       int maxPlayer = matchInit.getTypeMatch().getValue();
       List<Player> fullListPlayer = new ArrayList<>();

       for(int i = 0; i < maxPlayer; i++)
           fullListPlayer.add(playerInit);

       matchInit.getLobbyMatch().setListLobbyPlayer(fullListPlayer);
       matchInit.setMatchState(MatchState.WAITING);

       matchInteractionService.updateMatchStateBasedOnLobbySize(matchInit);

       verify(matchService, times(1))
               .updateMatchState(matchInit.getId(), MatchState.READY_MATCH);
    }

    @Test
    void shouldNotUpdateMatchStateWhenStateHasNotChanged(){
        matchInit.getLobbyMatch().setListLobbyPlayer(new ArrayList<>());
        matchInteractionService.updateMatchStateBasedOnLobbySize(matchInit);

        verify(matchService, never())
                .updateMatchState(any(), any());
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

    private MatchInteractionRequestDTO createDefaultMatchInteraction(InteractionEvent event, UUID matchId, UUID playerId){
        return new MatchInteractionRequestDTO(
                event,
                matchId,
                playerId
        );
    }

}
