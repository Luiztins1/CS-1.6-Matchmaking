package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
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
    private final LobbyRepository lobbyRepository;
    private final MatchRepository matchRepository;

    @Transactional
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
        playerValidator.validate(player);
        return playerRepository.save(player);
    }

    public List<Player> findAllPlayer(){
        return playerRepository.findAll();
    }

    public void deletePlayer(UUID uuid){
        Player player = playerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado." + uuid));
        playerRepository.delete(player);
    }

    @Transactional
    public Optional<Player> updatePlayer(UUID uuid, PlayerDTO playerDTO){
        Player player = playerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado." + uuid));

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

    @Transactional
    public Optional<Player> updateRelationships(UUID id, UUID matchId, UUID lobbyId){
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado." + id));

        if(matchId != null){
            Match match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrado." + matchId));
            player.setMatch(match);
        }

        if(lobbyId != null){
            Lobby lobby = lobbyRepository.findById(lobbyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lobby não encontrado." + lobbyId));
            player.setLobby(lobby);
        }

        return Optional.of(playerRepository.save(player));
    }

    public Optional<Player> findByIdPlayer(UUID uuid){
        return Optional.of(playerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado." + uuid)));
    }
}
