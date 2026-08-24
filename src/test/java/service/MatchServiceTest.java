package service;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.dto.MatchResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Match.mapper.MatchMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Match.service.MatchService;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.MatchNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MatchServiceTest {

    @InjectMocks
    MatchService matchService;

    @Mock
    MatchRepository matchRepository;

    @Mock
    PlayerRepository playerRepository;

    MatchResponseDTO matchResponseDTO;
    Match matchInit;

    Lobby lobby;

    Player player;

    List<Player> playerList;

    @Captor
    private ArgumentCaptor<Match> matchArgumentCaptor;

    @BeforeEach
    void setUp(){
        playerList = new ArrayList<>();
        player = createDefaultPlayer();

        playerList.add(player);

        matchInit = createDefaultMatch(playerList);
        lobby = createDefaultLobby(matchInit, playerList);

        matchInit.setLobbyMatch(lobby);
        lobby.setMatchLobby(matchInit);

        player.setMatch(matchInit);
        player.setLobby(lobby);

        matchResponseDTO = MatchMapper.toDto(matchInit);
    }

    @Test
    void shouldSaveMatch(){
        when(matchRepository.save(Mockito.any(Match.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(playerRepository.findAll())
                .thenReturn(List.of(player));

        Match matchUpdated = matchService.saveMatch(matchResponseDTO);

        verify(matchRepository, times(1))
                .save(matchArgumentCaptor.capture());

        Match matchCaptor = matchArgumentCaptor.getValue();

        assertThat(matchCaptor).isNotNull();
        assertThat(matchCaptor.getId()).isEqualTo(matchInit.getId());

        assertThat(player.getMatch()).isEqualTo(matchCaptor);
        assertThat(player.getLobby()).isEqualTo(matchCaptor.getLobbyMatch());

        verify(matchRepository, times(1))
                .save(matchCaptor);

        verify(playerRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnResourceNotFoundExceptionMatch(){
        when(playerRepository.findAll())
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () ->{
            matchService.saveMatch(matchResponseDTO);
        }, "Lista de players vazia.");

        verify(playerRepository, times(1))
                .findAll();
    }

    @Test
    void shouldFindAllMatch(){
        when(matchRepository.findAll())
                .thenReturn(List.of(matchInit));

        List<Match> matchList = matchService.findAllMatch();

        assertThat(matchList).isNotEmpty().doesNotContainNull();
        assertThat(matchList).allSatisfy(match -> assertThat(match).isNotNull());

        verify(matchRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnListEmptyMatch(){
        when(matchRepository.findAll())
                .thenReturn(List.of());

        List<Match> matchList = matchService.findAllMatch();

        assertThat(matchList).isEmpty();

        verify(matchRepository, times(1))
                .findAll();
    }

    @Test
    void shouldUpdateMatch(){
        when(matchRepository.findById(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        when(matchRepository.save(Mockito.any(Match.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Match matchUpdated = matchService.updateMatchState(matchInit.getId(), MatchState.WAITING)
                .orElseThrow(() -> new RuntimeException("Update error."));

        verify(matchRepository, times(1))
                .save(matchArgumentCaptor.capture());

        Match matchCaptor = matchArgumentCaptor.getValue();

        assertThat(matchCaptor).isNotNull();
        assertThat(matchCaptor.getId()).isEqualTo(matchInit.getId());
        assertThat(matchCaptor.getMatchState()).isEqualTo(MatchState.WAITING);

        verify(matchRepository, times(1))
                .findById(matchInit.getId());
    }

    @Test
    void shouldReturnMatchNotFoundExceptionMatch(){
        assertThrows(MatchNotFoundException.class, () -> {
            matchService.updateMatchState(matchInit.getId(), MatchState.WAITING);
        }, "Match não encontrado.");
    }

    @Test
    void shouldDeleteMatch(){
        Match match = matchInit;

        Player player = new Player(
                UUID.randomUUID(),
                "Luiz",
                Ranking.BRONZE_1,
                0,
                0,
                "Brasil",
                null,
                match,
                match.getLobbyMatch()
        );

        match.setListPlayer(List.of(player));

        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.of(match));

        doNothing().when(matchRepository)
                .delete(match);

        matchService.deleteMatch(match.getId());

        assertThat(player.getMatch()).isNull();
        assertThat(player.getLobby()).isNull();

        verify(matchRepository, times(1))
                .findById(match.getId());

        verify(matchRepository, times(1))
                .delete(match);
    }


    @Test
    void shouldDeleteMatchInEachPlayer(){
        when(matchRepository.findById(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        Player player1 = new Player(
                UUID.randomUUID(), "Luiz", Ranking.BRONZE_1,0, 0, "Brasil", null, matchInit, matchInit.getLobbyMatch());

        Player player2 = new Player(
                UUID.randomUUID(), "Luasdz", Ranking.BRONZE_2, 0, 0, "Brasil", null, matchInit, matchInit.getLobbyMatch());

        matchInit.setListPlayer(List.of(player1, player2));

        doNothing().when(matchRepository)
                .delete(matchInit);

        matchService.deleteMatch(matchInit.getId());

        assertThat(player1.getMatch()).isNull();
        assertThat(player1.getLobby()).isNull();

        assertThat(player2.getMatch()).isNull();
        assertThat(player2.getLobby()).isNull();

        verify(matchRepository, times(1))
                .findById(matchInit.getId());

        verify(matchRepository, times(1))
                .delete(matchInit);
    }

    @Test
    void shouldReturnMatchNotFoundExceptionDeleteMatch(){
        assertThrows(MatchNotFoundException.class, () -> {
            matchService.deleteMatch(matchInit.getId());
        }, "Match não encontrado.");
    }

    @Test
    void shouldFindByIdMatch(){
        when(matchRepository.findById(Mockito.eq(matchInit.getId())))
                .thenReturn(Optional.of(matchInit));

        Match match = matchService.findByIdMatch(matchInit.getId())
                .orElseThrow(() -> new MatchNotFoundException("Match não encontrado."));

        assertThat(match).isNotNull();

        verify(matchRepository, times(1))
                .findById(match.getId());
    }

    @Test
    void shouldReturnMatchNotFoundExceptionMatchFindById(){
        assertThrows(MatchNotFoundException.class, () ->{
            matchService.findByIdMatch(matchInit.getId());
        }, "Match não encontrado.");
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
                Instant.now(),
                null,
                playerList
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
