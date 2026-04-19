package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final LobbyRepository lobbyRepository;
    private final MatchRepository matchRepository;

    public Player savePlayer(PlayerDTO playerDTO) {
        Match match = playerDTO.matchId() != null ? matchRepository.findById(playerDTO.matchId()).orElse(null): null;
        Lobby lobby = playerDTO.lobbyId() != null ? lobbyRepository.findById(playerDTO.lobbyId()).orElse(null): null;

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
        return playerRepository.save(player);
    }

    public List<Player> findAllPlayer(){
        return playerRepository.findAll();
    }

    public void deletePlayer(UUID uuid){
        playerRepository.deleteById(uuid);
    }

    public Optional<Player> updatePlayer(UUID uuid, PlayerDTO playerDTO){
        Player player = playerRepository.findById(uuid).orElseThrow();

        Match match = playerDTO.matchId() != null ? matchRepository.findById(playerDTO.matchId()).orElse(null): null;
        Lobby lobby = playerDTO.lobbyId() != null ? lobbyRepository.findById(playerDTO.lobbyId()).orElse(null): null;

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

    public Optional<Player> updateRelationships(UUID id, UUID matchId, UUID lobbyId){
        Player player = playerRepository.findById(id).orElseThrow();

        if(matchId != null){
            Match match = matchRepository.findById(matchId).orElseThrow();
            player.setMatch(match);
        }

        if(lobbyId != null){
            Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow();
            player.setLobby(lobby);
        }

        return Optional.of(playerRepository.save(player));
    }

    public Optional<Player> findByIdPlayer(UUID uuid){
        return playerRepository.findById(uuid);
    }
}
