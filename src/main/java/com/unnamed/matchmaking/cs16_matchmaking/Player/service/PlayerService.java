package com.unnamed.matchmaking.cs16_matchmaking.Player.service;

import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.Match.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.Player.validator.PlayerValidator;
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
    public Player savePlayer(PlayerResponseDTO playerResponseDTO) {
        Match match = matchValidator.validateIdForReturnNullMapper(playerResponseDTO);
        Lobby lobby = lobbyValidator.validateIdForReturnNullMapper(playerResponseDTO);

        Player player = new Player(
                null,
                playerResponseDTO.nickname(),
                playerResponseDTO.rank(),
                playerResponseDTO.kills(),
                playerResponseDTO.deaths(),
                playerResponseDTO.country(),
                playerResponseDTO.lastConnection(),
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
    public Optional<Player> updatePlayer(UUID id, PlayerResponseDTO playerResponseDTO){
        Player player = playerValidator.validateSource(id);

        Match match = matchValidator.validateIdForReturnNullMapper(playerResponseDTO);
        Lobby lobby = lobbyValidator.validateIdForReturnNullMapper(playerResponseDTO);

        player.setNickname(playerResponseDTO.nickname());
        player.setRank(playerResponseDTO.rank());
        player.setKills(playerResponseDTO.kills());
        player.setDeaths(playerResponseDTO.deaths());
        player.setCountry(playerResponseDTO.country());
        player.setLastConnection(playerResponseDTO.lastConnection());
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
