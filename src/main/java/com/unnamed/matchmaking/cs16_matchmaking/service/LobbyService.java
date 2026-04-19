package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final LobbyRepository lobbyRepository;

    public Lobby saveLobby(Lobby lobby){
        return lobbyRepository.save(lobby);
    }

    public List<Lobby> findAllLobby(){
        return lobbyRepository.findAll();
    }

    public Optional<Lobby> updateLobby(UUID uuid, Lobby lobby){
        return lobbyRepository.findById(uuid).map(existLobby ->{
            existLobby.setListLobby(lobby.getListLobby());
            return lobbyRepository.save(existLobby);
        });
    }

    public void deleteLobby(UUID uuid){
        lobbyRepository.deleteById(uuid);
    }

    public Optional<Lobby> findByIdLobby(UUID uuid){
        return lobbyRepository.findById(uuid);
    }
}
