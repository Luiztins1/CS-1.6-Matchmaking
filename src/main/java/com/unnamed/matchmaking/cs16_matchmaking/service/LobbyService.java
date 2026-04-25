package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Mapper.LobbyMapper;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import jakarta.persistence.Lob;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final LobbyMapper lobbyMapper;

    @Transactional
    public Lobby saveLobby(LobbyDTO lobbyDTO){
        return lobbyRepository.save(lobbyMapper.createLobby(lobbyDTO));
    }

    public List<Lobby> findAllLobby(){
        return lobbyRepository.findAll();
    }

    @Transactional
    public Optional<Lobby> updateLobby(UUID id, LobbyDTO lobbyDTO){
        return Optional.of(lobbyMapper.updateLobby(id, lobbyDTO));
    }

    @Transactional
    public void deleteLobby(UUID id){
        lobbyRepository.delete(lobbyMapper.deleteLobby(id));
    }

    public Optional<Lobby> findByIdLobby(UUID id){
        return Optional.of(lobbyMapper.findByIdLobby(id));
    }
}
