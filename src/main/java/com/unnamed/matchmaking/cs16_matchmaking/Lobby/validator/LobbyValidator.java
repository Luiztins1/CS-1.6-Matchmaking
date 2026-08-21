package com.unnamed.matchmaking.cs16_matchmaking.Lobby.validator;

import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.repository.LobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LobbyValidator {

    private final LobbyRepository lobbyRepository;

    public Lobby validateSource(UUID id){
        return lobbyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lobby não encontrado: " + id));
    }

    public List<Player> validateListLobby(UUID id){
        Lobby lobby = lobbyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lobby não encontrado: " + id));
        return lobby.getListLobbyPlayer();
    }

    public Lobby validateIdForReturnNullMapper(PlayerResponseDTO playerResponseDTO){
        if(playerResponseDTO.lobbyId() == null){
            return null;
        }
        return validateSource(playerResponseDTO.lobbyId());
    }
}
