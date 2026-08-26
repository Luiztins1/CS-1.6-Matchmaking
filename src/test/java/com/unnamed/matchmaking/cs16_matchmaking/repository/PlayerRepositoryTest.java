package com.unnamed.matchmaking.cs16_matchmaking.repository;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
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
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    //Serves for to make persistence of data in database.
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldWhenTrueExistsByIdOrNickname(){
        Player playerTest = testEntityManager.persistAndFlush(createDefaultPlayer());
        testEntityManager.clear();

        boolean exists = playerRepository.existsByIdOrNickname(
                playerTest.getId(),
                playerTest.getNickname());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldWhenFalseExistsByIdOrNickname(){
        boolean exists = playerRepository.existsByIdOrNickname(
               UUID.randomUUID(),
               "EaiTest");

        assertThat(exists).isFalse();
    }

    @Test
    void shouldPersistAndRetrievePlayerCorrectly(){
        Player playerTest = testEntityManager.persistAndFlush(createDefaultPlayer());
        testEntityManager.clear();

        Player foundPlayer = testEntityManager.find(Player.class, playerTest.getId());

        assertThat(foundPlayer).isNotNull();
        assertThat(foundPlayer.getNickname()).isEqualTo("Luiz");
        assertThat(foundPlayer.getRank()).isEqualTo(Ranking.BRONZE_1);
        assertThat(foundPlayer.getKills()).isEqualTo(0);
        assertThat(foundPlayer.getDeaths()).isEqualTo(0);
        assertThat(foundPlayer.getCountry()).isEqualTo("Brasil");
        assertThat(foundPlayer.getLastConnection()).isNull();
        assertThat(foundPlayer.getMatch()).isNull();
        assertThat(foundPlayer.getLobby()).isNull();
    }

    @Test
    void shouldFailWhenNicknameIsNull(){
        Player playerTest = createDefaultPlayer();
        playerTest.setNickname(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(playerTest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenKillsIsNull(){
        Player playerTest = createDefaultPlayer();
        playerTest.setKills(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(playerTest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenDeathsIsNull(){
        Player playerTest = createDefaultPlayer();
        playerTest.setDeaths(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(playerTest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenCountryIsNull(){
        Player playerTest = createDefaultPlayer();
        playerTest.setCountry(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(playerTest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldPlayerWithMatchAndLobby(){
        Match match = createDefaultMatch(new ArrayList<>());
        Lobby lobby = createDefaultLobby(match, new ArrayList<>());

        match.setLobbyMatch(lobby);

        Match savedMatch = testEntityManager.persistAndFlush(match);

        Player playerRelationships = createDefaultPlayer();

        playerRelationships.setMatch(savedMatch);
        playerRelationships.setLobby(savedMatch.getLobbyMatch());

        Player saved = testEntityManager.persistAndFlush(playerRelationships);
        testEntityManager.clear();

        Player foundPlayer = testEntityManager.find(Player.class, saved.getId());
        Match foundMatch = testEntityManager.find(Match.class, savedMatch.getId());
        Lobby foundLobby = testEntityManager.find(Lobby.class, savedMatch.getLobbyMatch().getId());

        UUID matchId = foundMatch.getId();
        UUID lobbyId = foundLobby.getId();

        assertThat(saved).isNotNull();
        assertThat(foundMatch).isNotNull();
        assertThat(foundPlayer).isNotNull();

        assertThat(foundPlayer.getMatch().getId()).isEqualTo(matchId);
        assertThat(foundPlayer.getLobby().getId()).isEqualTo(lobbyId);
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
