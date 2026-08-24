package service;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.mapper.PlayerMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Player.service.PlayerService;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.LobbyNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.MatchNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PlayerServiceTest {

    @InjectMocks
    PlayerService playerService;

    @Mock
    PlayerRepository playerRepository;

    @Mock
    MatchRepository matchRepository;

    @Mock
    LobbyRepository lobbyRepository;

    PlayerResponseDTO playerResponseDTO;
    Player playerInit;

    Match match;

    Lobby lobby;

    List<Player> playerList;

    @Captor
    private ArgumentCaptor<Player> playerArgumentCaptor;

    @BeforeEach
    void setUp(){
        playerList = new ArrayList<>();
        playerInit = createDefaultPlayer();

        playerList.add(playerInit);

        match = createDefaultMatch(playerList);
        lobby = createDefaultLobby(match, playerList);

        match.setLobbyMatch(lobby);
        lobby.setMatchLobby(match);

        playerResponseDTO = PlayerMapper.toDto(playerInit);

    }

    @Test
    void shouldSavePlayer(){
        when(playerRepository.existsByIdOrNickname(Mockito.eq(playerInit.getId()),
                Mockito.eq(playerInit.getNickname()))).
                thenReturn(false);

        when(playerRepository.save(Mockito.any(Player.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Player playerSaved = playerService.savePlayer(playerResponseDTO);

        verify(playerRepository, times(1))
                .save(playerArgumentCaptor.capture());

        Player playerCaptor = playerArgumentCaptor.getValue();

        assertThat(playerCaptor).isNotNull();
        assertThat(playerCaptor.getNickname()).isNotBlank();
        assertThat(playerCaptor.getRank()).isNotNull();
        assertThat(playerCaptor.getKills()).isEqualTo(0);
        assertThat(playerCaptor.getDeaths()).isEqualTo(0);
        assertThat(playerCaptor.getCountry()).isNotBlank();
        assertThat(playerCaptor.getLastConnection()).isNull();
        assertThat(playerCaptor.getMatch()).isNull();
        assertThat(playerCaptor.getLobby()).isNull();

        verify(playerRepository, times(1))
                .existsByIdOrNickname(playerInit.getId(), playerInit.getNickname());
    }

    @Test
    void shouldReturnPlayerNotFoundExceptionWhetherPlayerNotExist (){
        assertThrows(PlayerNotFoundException.class, () -> {
            playerService.savePlayer(PlayerMapper.toDto(new Player()));
        }, "Player não encontrado.");
    }

    @Test
    void shouldReturnDuplicateExceptionWhetherPlayerForDuplicate(){
        assertThrows(DuplicateException.class, () -> {
            when(playerRepository.existsByIdOrNickname(Mockito.eq(playerInit.getId()), Mockito.eq(playerInit.getNickname())))
                    .thenReturn(true);

            Player duplicatePlayer = playerInit;

            playerService.savePlayer(PlayerMapper.toDto(duplicatePlayer));
                }, "Player duplicado.");

        verify(playerRepository, times(1))
                .existsByIdOrNickname(Mockito.eq(playerInit.getId()),
                        Mockito.eq(playerInit.getNickname()));
    }

    @Test
    void shouldFindAllPlayers(){
        when(playerRepository.findAll())
                .thenReturn(List.of(playerInit));

        List<Player> playerList = playerService.findAllPlayer();

        assertThat(playerList)
                .isNotEmpty()
                .doesNotContainNull();

        assertThat(playerList).allSatisfy(
                player -> assertThat(player).isNotNull());

        verify(playerRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnListEmptyPlayer(){
        when(playerRepository.findAll())
                .thenReturn(List.of());

        List<Player> playerList = playerService.findAllPlayer();

        assertThat(playerList).isEmpty();

        verify(playerRepository, times(1))
                .findAll();
    }

    @Test
    void shouldDeletePlayer(){
        when(playerRepository.findById(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        doNothing().when(playerRepository)
                .delete(playerInit);

        playerService.deletePlayer(playerInit.getId());

        verify(playerRepository, times(1)).
                findById(playerInit.getId());

        verify(playerRepository, times(1))
                .delete(playerInit);
    }

    @Test
    void shouldReturnPlayerNotFoundExceptionForDelete(){
        assertThrows(PlayerNotFoundException.class, () ->{
            playerService.deletePlayer(UUID.randomUUID());
        }, "Player não encontrado.");
    }

    @Test
    void shouldUpdatePlayer(){
        when(playerRepository.findById(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        when(playerRepository.save(Mockito.any(Player.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlayerResponseDTO dto = new PlayerResponseDTO(
                playerInit.getId(),
                "Update",
                Ranking.BRONZE_2,
                12,
                2,
                "Canada",
                Instant.now(),
                null,
                null
        );

       Player updated = playerService.updatePlayer(dto.id(), dto)
               .orElseThrow(() -> new PlayerNotFoundException("Player não encontrado."));

       verify(playerRepository, times(1))
               .save(playerArgumentCaptor.capture());

       Player playerCapture = playerArgumentCaptor.getValue();

       assertThat(playerCapture).isNotNull();

       verify(playerRepository, times(1))
               .save(updated);

       verify(playerRepository, times(1))
               .findById(playerInit.getId());
    }

    @Test
    void shouldReturnPlayerNotFoundExceptionUpdate(){
        assertThrows(PlayerNotFoundException.class, () ->{
           playerService.updatePlayer(UUID.randomUUID(), PlayerMapper.toDto(playerInit));
        }, "Player não encontrado.");
    }

    @Test
    void shouldUpdateRelationshipsPlayer(){

        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.of(match));

        when(lobbyRepository.findById(Mockito.eq(lobby.getId())))
                .thenReturn(Optional.of(lobby));

        when(playerRepository.findById(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        when(playerRepository.save(Mockito.any(Player.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Player playerUpdated = playerService.updateRelationships(
                playerInit.getId(), match.getId(), lobby.getId())
                .orElseThrow(() -> new RuntimeException("Error."));

        verify(playerRepository, times(1))
                .save(playerArgumentCaptor.capture());

        Player playerCapture = playerArgumentCaptor.getValue();

        assertThat(playerCapture).isNotNull();
        assertThat(playerCapture.getMatch()).isNotNull();
        assertThat(playerCapture.getLobby()).isNotNull();

        assertThat(playerCapture.getMatch().getId()).isEqualTo(match.getId());
        assertThat(playerCapture.getLobby().getId()).isEqualTo(lobby.getId());

        verify(matchRepository, times(1))
                .findById(match.getId());

        verify(lobbyRepository, times(1))
                .findById(lobby.getId());

        verify(playerRepository, times(1))
                .findById(playerCapture.getId());

        verify(playerRepository, times(1))
                .save(playerCapture);


    }

    @Test
    void shouldReturnMatchNotFoundExceptionUpdateRelationshipsPlayer(){
        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.empty());

        assertThrows(MatchNotFoundException.class, () ->{
            playerService.updateRelationships(playerInit.getId(), match.getId(), lobby.getId());
        }, "Match não encontrado.");
    }

    @Test
    void shouldReturnLobbyNotFoundExceptionUpdateRelationshipsPlayer(){
        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.of(match));

        assertThrows(LobbyNotFoundException.class, () ->{
            playerService.updateRelationships(playerInit.getId(), match.getId(), lobby.getId());
        }, "Lobby não encontrado.");
    }

    @Test
    void shouldReturnPlayerNotFoundExceptionUpdateRelationshipsPlayer(){
        when(matchRepository.findById(Mockito.eq(match.getId())))
                .thenReturn(Optional.of(match));

        when(lobbyRepository.findById(Mockito.eq(lobby.getId())))
                .thenReturn(Optional.of(lobby));

        when(playerRepository.findById(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () ->{
            playerService.updateRelationships(playerInit.getId(), match.getId(), lobby.getId());
        }, "Player não encontrado.");

        verify(matchRepository, times(1))
                .findById(match.getId());

        verify(lobbyRepository, times(1))
                .findById(lobby.getId());

        verify(playerRepository, times(1))
                .findById(playerInit.getId());

    }

    @Test
    void shouldFindByIdPlayer(){
        when(playerRepository.findById(Mockito.eq(playerInit.getId())))
                .thenReturn(Optional.of(playerInit));

        Player player = playerService.findByIdPlayer(playerInit.getId())
                .orElseThrow(() -> new PlayerNotFoundException("Player não encontrado"));

        assertThat(player).isNotNull();

        verify(playerRepository, times(1))
                .findById(player.getId());
    }

    @Test
    void shouldResultPlayerNotFoundExceptionFindByIdPlayer(){
        assertThrows(PlayerNotFoundException.class, () ->{
            playerService.findByIdPlayer(playerInit.getId());
        }, "Player não encontrado.");
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
