package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Mapper.PlayerMapper;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
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
    private final PlayerMapper playerMapper;

    @Transactional
    public Player savePlayer(PlayerDTO playerDTO) {
        return playerRepository.save(playerMapper.createPlayer(playerDTO));
    }

    public List<Player> findAllPlayer(){
        return playerRepository.findAll();
    }

    public void deletePlayer(UUID id){
        playerRepository.delete(playerMapper.deletePlayer(id));
    }

    @Transactional
    public Optional<Player> updatePlayer(UUID id, PlayerDTO playerDTO){
        return Optional.of(playerRepository.save(playerMapper.updatePlayer(id, playerDTO)));
    }

    @Transactional
    public Optional<Player> updateRelationships(UUID id, UUID matchId, UUID lobbyId){
        return Optional.of(playerRepository.save(playerMapper.updateRelationships(id, matchId, lobbyId)));
    }

    public Optional<Player> findByIdPlayer(UUID id){
        return Optional.of(playerMapper.findByIdPlayer(id));
    }
}
