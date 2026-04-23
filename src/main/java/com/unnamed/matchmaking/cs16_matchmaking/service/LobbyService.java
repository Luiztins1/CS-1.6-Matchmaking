package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
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
    private final PlayerRepository playerRepository;

    @Transactional
    public Lobby saveLobby(LobbyDTO lobbyDTO){
        List<Player> lobbyList = lobbyDTO.listLobby() != null ?
                lobbyDTO.listLobby()
                        .stream()
                        .map(id -> playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado." + id)))
                        .toList()
                : List.of();

        Lobby lobby = new Lobby(
                null,
                lobbyDTO.lobby(),
                lobbyDTO.state(),
                lobbyList
        );

        return lobbyRepository.save(lobby);
    }

    public List<Lobby> findAllLobby(){
        return lobbyRepository.findAll();
    }

    @Transactional
    public Optional<Lobby> updateLobby(UUID uuid, LobbyDTO lobbyDTO){
        Lobby lobby = lobbyRepository.findById(uuid).orElseThrow();

        List<Player> playerList = lobbyDTO.listLobby() != null ? lobbyDTO.listLobby()
                .stream()
                .map(id -> playerRepository.findById(id).orElseThrow())
                .toList()
                :List.of();
        lobby.setLobby(lobbyDTO.lobby());
        lobby.setListLobby(playerList);

        return Optional.of(lobbyRepository.save(lobby));
    }

    @Transactional
    public void deleteLobby(UUID uuid){
        Lobby lobby = lobbyRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Lobby não encontrado." + uuid));
        lobbyRepository.delete(lobby);
    }

    public Optional<Lobby> findByIdLobby(UUID uuid){
        return Optional.of(lobbyRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Lobby não encontrado." + uuid)));
    }
}
