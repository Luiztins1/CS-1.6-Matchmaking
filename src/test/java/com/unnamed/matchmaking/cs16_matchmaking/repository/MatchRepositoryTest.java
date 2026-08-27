package com.unnamed.matchmaking.cs16_matchmaking.repository;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
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
public class MatchRepositoryTest {

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void shouldFindByMapEquals(){
        Match matchTest = testEntityManager.persistAndFlush(createDefaultMatch(new ArrayList<>()));
        Match matchTest2 = testEntityManager.persistAndFlush(createDefaultMatch(new ArrayList<>()));

        testEntityManager.clear();

        List<Match> matchList = matchRepository.findByMapEquals(GameMap.DE_DUST_2);

        assertThat(matchTest.getMap()).isEqualTo(GameMap.DE_DUST_2);
        assertThat(matchTest2.getMap()).isEqualTo(GameMap.DE_DUST_2);

        assertThat(matchList).isNotEmpty();
        assertThat(matchList).contains(matchTest);
        assertThat(matchList).contains(matchTest2);
        assertThat(matchList).hasSize(2);
    }

    @Test
    void shouldReturnWhenNotExistsFindByMapEquals(){
        List<Match> matchList = matchRepository.findByMapEquals(GameMap.DE_DUST_2);

        assertThat(matchList).isEmpty();
    }

    @Test
    void shouldPersistAndRetrieveMatchCorrectly(){
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
        assertThat(foundMatch.getTypeMatch()).isInstanceOf(TypeMatch.class);
        assertThat(foundMatch.getTypeMatch()).isEqualTo(TypeMatch.DEFAULT);
        assertThat(foundMatch.getTimeMatchMap()).isInstanceOf(Instant.class);
        assertThat(foundMatch.getTimeMatchMap()).isNotNull();

        assertThat(foundLobby.getTypeMatchEvent()).isInstanceOf(TypeMatch.class);
        assertThat(foundLobby.getTypeMatchEvent()).isEqualTo(TypeMatch.DEFAULT);
        assertThat(foundLobby.getListLobbyPlayer()).isEmpty();

        assertThat(foundMatch.getLobbyMatch().getId()).isEqualTo(lobbyId);
        assertThat(foundLobby.getMatchLobby().getId()).isEqualTo(matchId);
    }

    @Test
    void shouldFailWhenNameMatchIsNull(){
        Match matchTest = createDefaultMatch(new ArrayList<>());
        matchTest.setNameMatch(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(matchTest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenMapIsNull(){
        Match matchTest = createDefaultMatch(new ArrayList<>());
        matchTest.setMap(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(matchTest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenMatchStateIsNull(){
        Match matchTest = createDefaultMatch(new ArrayList<>());
        matchTest.setMatchState(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(matchTest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenTimeMatchIsNull(){
        Match matchTest = createDefaultMatch(new ArrayList<>());
        matchTest.setTimeMatchMap(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(matchTest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldCreateMatchInCascade(){
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
        assertThat(foundMatch.getLobbyMatch()).isNotNull();

        assertThat(foundLobby).isNotNull();
        assertThat(foundLobby.getMatchLobby()).isNotNull();

        assertThat(foundMatch.getLobbyMatch().getId()).isEqualTo(lobbyId);
        assertThat(foundLobby.getMatchLobby().getId()).isEqualTo(matchId);
    }

    private Player createDefaultPlayer(){
        return new Player(
                null,
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
                null,
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
                null,
                "Test",
                match,
                playerList
        );
    }
}
