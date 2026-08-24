package service;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.dto.LobbyResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.mapper.LobbyMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.service.LobbyService;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.LobbyNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.MatchNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.PlayerNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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
public class LobbyServiceTest {

    @InjectMocks
    LobbyService lobbyService;

    @Mock
    LobbyRepository lobbyRepository;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    MatchRepository matchRepository;

    LobbyResponseDTO lobbyResponseDTO;
    Lobby lobbyInit;

    Player player;

    Match match;

    List<Player> playerList;

    /*This annotation create an instance of ArgumentCaptor.
    The ArgumentCaptor serves for capture argument values passed in methods that be mockito.
    Normally when we pass methods mockito, we can passed an false positive. Than for avoid this,
    we use ArgumentCaptor for verify the values.
    .*/
    @Captor
    private ArgumentCaptor<Lobby> lobbyArgumentCaptor;

    @Captor
    private ArgumentCaptor<Match> matchArgumentCaptor;


    @BeforeEach
    void setUp(){
        playerList = new ArrayList<>();
        player = createDefaultPlayer();

        playerList.add(player);

        match = createDefaultMatch(playerList);
        lobbyInit = createDefaultLobby(match, playerList);

        match.setLobbyMatch(lobbyInit);
        lobbyInit.setMatchLobby(match);

        player.setMatch(match);
        player.setLobby(lobbyInit);

        lobbyResponseDTO = LobbyMapper.toDto(lobbyInit);
    }

    @Test
    void shouldFindAllLobby(){
        when(lobbyRepository.findAll())
                .thenReturn(List.of(lobbyInit));

        List<Lobby> lobbies = lobbyService.findAllLobby();

        assertThat(lobbies).isNotEmpty().doesNotContainNull();
        assertThat(lobbies).allSatisfy(lobby -> assertThat(lobby).isNotNull());

        verify(lobbyRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnEmptyListLobby(){
        when(lobbyRepository.findAll())
                .thenReturn(List.of());

        List<Lobby> lobbies = lobbyService.findAllLobby();

        assertThat(lobbies).isEmpty();

        verify(lobbyRepository, times(1))
                .findAll();
    }

    @Test
    void shouldAddListLobbyPlayer(){

        when(playerRepository.findById(Mockito.eq(player.getId())))
                .thenReturn(Optional.of(player));

        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.of(match));

        when(lobbyRepository.save(Mockito.any(Lobby.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Lobby lobbyAddListLobbyPlayer = lobbyService.addListLobbyPlayer(match.getId(), player.getId());

        verify(lobbyRepository, times(1))
                .save(lobbyArgumentCaptor.capture());

        Lobby lobbyCapture = lobbyArgumentCaptor.getValue();

        assertThat(lobbyCapture).isNotNull();
        assertThat(lobbyCapture.getMatchLobby()).isEqualTo(match);
        assertThat(lobbyCapture.getListLobbyPlayer()).contains(player);

        assertThat(player.getMatch()).isEqualTo(match);
        assertThat(player.getLobby()).isEqualTo(lobbyCapture);

        verify(playerRepository, times(1))
                .findById(player.getId());

        verify(matchRepository, times(1))
                .findById(match.getId());


    }

    @Test
    void shouldRemoveListLobbyPlayer(){
        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.of(match));

        when(playerRepository.findById(Mockito.eq(player.getId())))
                .thenReturn(Optional.of(player));

        //Take the first argument passed of method mock and return him same.
        when(matchRepository.save(Mockito.any(Match.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        lobbyService.removeListLobbyPlayer(match.getId(), player.getId());

        verify(matchRepository, times(1))
                .save(matchArgumentCaptor.capture());

        Match lobbyCapture = matchArgumentCaptor.getValue();

        Lobby lobby = lobbyCapture.getLobbyMatch();

        assertThat(lobby.getMatchLobby()).isNotNull();
        assertThat(lobby.getListLobbyPlayer()).isEmpty();
        assertThat(lobby.getListLobbyPlayer()).doesNotContain(player);

        assertThat(player.getMatch()).isNull();
        assertThat(player.getLobby()).isNull();
    }

    @Test
    void shouldReturnMatchNotFoundExceptionInRemoveListLobbyPlayer(){
        assertThrows(MatchNotFoundException.class, () ->{
            lobbyService.removeListLobbyPlayer(match.getId(), player.getId());
        }, "Match não encontrado.");
    }

    @Test
    void shouldReturnPlayerNotFoundExceptionInRemoveListLobbyPlayer(){
        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.of(match));

        assertThrows(PlayerNotFoundException.class, () ->{
            lobbyService.removeListLobbyPlayer(match.getId(), player.getId());
        }, "Player não encontrado.");

        verify(matchRepository)
                .findById(match.getId());
    }

    @Test
    void shouldReturnResourceNotFoundExceptionInRemoveListLobbyPlayer(){
        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.of(match));

        when(playerRepository.findById(Mockito.eq(player.getId())))
                .thenReturn(Optional.of(player));

        Match matchLobby = match;
        match.getLobbyMatch().setListLobbyPlayer(List.of());

        assertThrows(ResourceNotFoundException.class, () ->{
            lobbyService.removeListLobbyPlayer(matchLobby.getId(), player.getId());
        }, "Lista de players vazia.");

        verify(matchRepository)
                .findById(match.getId());

        verify(playerRepository)
                .findById(player.getId());
    }

    @Test
    void shouldFindByIdLobby(){
        when(lobbyRepository.findById(Mockito.eq(lobbyInit.getId())))
                .thenReturn(Optional.of(lobbyInit));

        Lobby lobby = lobbyService.findByIdLobby(lobbyInit.getId())
                .orElseThrow(null);

        assertThat(lobby).isNotNull();

        verify(lobbyRepository, times(1))
                .findById(lobbyInit.getId());
    }

    @Test
    void shouldReturnLobbyNotFoundException(){
        assertThrows(LobbyNotFoundException.class, () ->{
            lobbyService.findByIdLobby(lobbyInit.getId());
        }, "Lobby não encontrado");
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
}
