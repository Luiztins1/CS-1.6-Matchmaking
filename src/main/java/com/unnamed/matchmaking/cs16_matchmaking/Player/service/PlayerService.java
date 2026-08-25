package com.unnamed.matchmaking.cs16_matchmaking.Player.service;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.mapper.PlayerMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.*;
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
    private final MatchRepository matchRepository;
    private final LobbyRepository lobbyRepository;


    @Transactional
    public Player savePlayer(PlayerRequestDTO playerRequestDTO) {
        if(playerRequestDTO == null)
            throw new ResourceNotFoundException("Dto está vazio.");

        Player player = PlayerMapper.toEntity(playerRequestDTO);

        if(player.getId() == null)
            throw new PlayerNotFoundException("Player não encontrado.");

        if(playerRepository.existsByIdOrNickname(player.getId(), player.getNickname()))
            throw new DuplicateException("Player duplicado.");

        return playerRepository.save(player);
    }

    public List<Player> findAllPlayer(){
        return playerRepository.findAll();
    }

    @Transactional
    public void deletePlayer(UUID id) {
        var player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player não encontrado."));
        playerRepository.delete(player);
    }

    @Transactional
    public Optional<Player> updatePlayer(UUID id, PlayerResponseDTO playerResponseDTO){

        if(playerResponseDTO == null)
            throw new ResourceNotFoundException("Dto está vazio.");

        Player player = playerRepository.findById(playerResponseDTO.id())
                .orElseThrow(() -> new PlayerNotFoundException("Player não encontrado."));


        player.setNickname(playerResponseDTO.nickname());
        player.setRank(playerResponseDTO.rank());
        player.setKills(playerResponseDTO.kills());
        player.setDeaths(playerResponseDTO.deaths());
        player.setCountry(playerResponseDTO.country());
        player.setLastConnection(playerResponseDTO.lastConnection());

        player.setMatch(null);
        player.setLobby(null);

        return Optional.of(playerRepository.save(player));
    }

    @Transactional
    public Optional<Player> updateRelationships(UUID id, UUID matchId, UUID lobbyId){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Partida não encontrado."));

        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException("Lobby não encontrado."));

        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player não encontrado."));

        player.setMatch(match);
        player.setLobby(lobby);

        return Optional.of(playerRepository.save(player));
    }

    public Optional<Player> findByIdPlayer(UUID id){
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player não encontrado."));

        return Optional.of(player);
    }
}
