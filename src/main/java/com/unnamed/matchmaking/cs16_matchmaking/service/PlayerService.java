package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Mapper.PlayerMapper;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerValidator playerValidator;
    private final MatchValidator matchValidator;
    private final LobbyValidator lobbyValidator;

    @Transactional
    public Player savePlayer(PlayerDTO playerDTO) {
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
        return playerRepository.save(player);
    }

    public List<Player> findAllPlayer(){
        return playerRepository.findAll();
    }

    @Transactional
    public void deletePlayer(UUID id){
        playerRepository.delete(playerValidator.validateSource(id));
    }

    @Transactional
    public Optional<Player> updatePlayer(UUID id, PlayerDTO playerDTO){
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

        return Optional.of(playerRepository.save(player));
    }

    @Transactional
    public Optional<Player> updateRelationships(UUID id, UUID matchId, UUID lobbyId){
        Player player = playerValidator.validateSource(id);

        if(matchId != null){
            Match match = matchValidator.validateSource(matchId);
            player.setMatch(match);
        }

        if(lobbyId != null){
            Lobby lobby = lobbyValidator.validateSource(lobbyId);
            player.setLobby(lobby);
        }

        return Optional.of(playerRepository.save(player));
    }

    public Optional<Player> findByIdPlayer(UUID id){
        return Optional.of(playerValidator.validateSource(id));
    }
}
