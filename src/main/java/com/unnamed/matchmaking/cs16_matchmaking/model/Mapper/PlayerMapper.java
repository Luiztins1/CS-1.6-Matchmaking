package com.unnamed.matchmaking.cs16_matchmaking.model.Mapper;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    private final MatchValidator matchValidator;
    private final LobbyValidator lobbyValidator;
    private final PlayerValidator playerValidator;

    public Player createPlayer(PlayerDTO playerDTO){
        Match match = matchValidator.validateIdForReturnNullMapper(playerDTO);
        Lobby lobby = lobbyValidator.validateIdForReturnNullMapper(playerDTO);

        Player player = new Player(
                null,
                playerDTO.nickname(),
                playerDTO.rank(),
                playerDTO.kills(),
                playerDTO.deaths(),
                playerDTO.country(),
                playerDTO.lastConnection(),
                match,
                lobby
        );
        playerValidator.validateDuplicate(player);
        return player;
    }

    public Player deletePlayer(UUID id){
        return playerValidator.validateSource(id);
    }

    public Player updatePlayer(UUID id, PlayerDTO playerDTO){
        Player player = playerValidator.validateSource(id);

        Match match = matchValidator.validateIdForReturnNullMapper(playerDTO);
        Lobby lobby = lobbyValidator.validateIdForReturnNullMapper(playerDTO);

        player.setNickname(playerDTO.nickname());
        player.setRank(playerDTO.rank());
        player.setKills(playerDTO.kills());
        player.setDeaths(playerDTO.deaths());
        player.setCountry(playerDTO.country());
        player.setLastConnection(playerDTO.lastConnection());
        player.setMatch(match);
        player.setLobby(lobby);

        return player;
    }

    public Player updateRelationships(UUID id, UUID matchId, UUID lobbyId){
        Player player = playerValidator.validateSource(id);

        if(matchId != null){
            Match match = matchValidator.validateSource(matchId);
            player.setMatch(match);
        }

        if(lobbyId != null){
            Lobby lobby = lobbyValidator.validateSource(lobbyId);
            player.setLobby(lobby);
        }

        return player;
    }

    public Player findByIdPlayer(UUID id){
        return playerValidator.validateSource(id);
    }

}
