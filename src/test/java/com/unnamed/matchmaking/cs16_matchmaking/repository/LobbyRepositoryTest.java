package com.unnamed.matchmaking.cs16_matchmaking.repository;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
public class LobbyRepositoryTest {

    @Autowired
    LobbyRepository lobbyRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void shouldPersistAndRetrieveLobbyCorrectly(){
        Match match = createDefaultMatch(new ArrayList<>());
        Lobby lobby = createDefaultLobby(match, new ArrayList<>());
        match.setLobbyMatch(lobby);

        Match savedMatch = testEntityManager.persistAndFlush(match);
        testEntityManager.clear();

        Match foundMatch = testEntityManager.find(Match.class, savedMatch.getId());
        Lobby foundLobby = testEntityManager.find(Lobby.class, savedMatch.getLobbyMatch().getId());

        UUID matchId = foundMatch.getId();
        UUID lobbyId = foundLobby.getId();

        assertThat(savedMatch).isNotNull();
        assertThat(foundMatch).isNotNull();
        assertThat(foundLobby).isNotNull();

        assertThat(foundMatch.getNameMatch()).isEqualTo(foundLobby.getName());
        assertThat(foundLobby.getName()).isEqualTo(foundMatch.getNameMatch());

        assertThat(foundMatch.getMap()).isInstanceOf(GameMap.class);
        assertThat(foundMatch.getMatchState()).isInstanceOf(MatchState.class);
        assertThat(foundMatch.getMap()).isEqualTo(GameMap.DE_DUST_2);
        assertThat(foundMatch.getMatchState()).isEqualTo(MatchState.COLD);
        assertThat(foundMatch.getTimeMatchMap()).isInstanceOf(Instant.class);
        assertThat(foundMatch.getTimeMatchMap()).isNotNull();
        assertThat(foundMatch.getListPlayer()).isEmpty();

        assertThat(foundLobby.getTypeMatchEvent()).isInstanceOf(TypeMatch.class);
        assertThat(foundLobby.getTypeMatchEvent()).isEqualTo(TypeMatch.DEFAULT);
        assertThat(foundLobby.getListLobbyPlayer()).isEmpty();

        assertThat(foundMatch.getLobbyMatch().getId()).isEqualTo(lobbyId);
        assertThat(foundLobby.getMatchLobby().getId()).isEqualTo(matchId);
    }

    @Test
    void shouldFailWhenNameIsNull(){
        Match match = createDefaultMatch(new ArrayList<>());
        Lobby lobby = createDefaultLobby(match, new ArrayList<>());
        match.setLobbyMatch(lobby);
        lobby.setName(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(match))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenTypeMatchEventIsNull(){
        Match match = createDefaultMatch(new ArrayList<>());
        Lobby lobby = createDefaultLobby(match, new ArrayList<>());
        match.setLobbyMatch(lobby);
        lobby.setTypeMatchEvent(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(match))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldCreateLobbyInCascade(){
        Match match = createDefaultMatch(new ArrayList<>());
        Lobby lobby = createDefaultLobby(match, new ArrayList<>());
        match.setLobbyMatch(lobby);

        Match savedMatch = testEntityManager.persistAndFlush(match);

        testEntityManager.clear();

        Match foundMatch = testEntityManager.find(Match.class, savedMatch.getId());
        Lobby foundLobby = testEntityManager.find(Lobby.class, savedMatch.getLobbyMatch().getId());

        UUID matchId = foundMatch.getId();
        UUID lobbyId = foundMatch.getLobbyMatch().getId();

        assertThat(savedMatch).isNotNull();
        assertThat(foundMatch).isNotNull();
        assertThat(foundLobby).isNotNull();

        assertThat(foundMatch.getLobbyMatch().getId()).isEqualTo(lobbyId);
        assertThat(foundLobby.getMatchLobby().getId()).isEqualTo(matchId);

    }

    private Match createDefaultMatch(List<Player> playerList){
        return new Match(
                null,
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
                null,
                "Test",
                match,
                playerList
        );
    }
}
