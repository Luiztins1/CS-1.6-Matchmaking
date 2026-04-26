package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Mapper.LobbyMapper;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final LobbyMapper lobbyMapper;

    @Deprecated
    @Transactional
    public Lobby saveLobby(LobbyDTO lobbyDTO){
        return lobbyRepository.save(lobbyMapper.createLobby(lobbyDTO));
    }

    public List<Lobby> findAllLobby(){
        return lobbyRepository.findAll();
    }

    @Deprecated
    @Transactional
    public Optional<Lobby> updateLobby(UUID id, LobbyDTO lobbyDTO){
        return Optional.of(lobbyMapper.updateLobby(id, lobbyDTO));
    }

    @Transactional
    public Optional<List<PlayerDTO>> updateListLobbyPlayer(UUID matchId, UUID playerId){
        return Optional.of(lobbyMapper.updateListLobbyPlayer(matchId, playerId));
    }

    @Deprecated
    @Transactional
    public void deleteLobby(UUID id){
        lobbyRepository.delete(lobbyMapper.deleteLobby(id));
    }

    public Optional<Lobby> findByIdLobby(UUID id){
        return Optional.of(lobbyMapper.findByIdLobby(id));
    }

    @Transactional
    public void deleteListLobbyPlayer(UUID matchId, UUID playerId){
        lobbyMapper.deleteListLobbyPlayer(matchId, playerId);
    }
}
