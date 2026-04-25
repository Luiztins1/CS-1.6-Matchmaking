package com.unnamed.matchmaking.cs16_matchmaking.validator;

import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LobbyValidator {

    private final LobbyRepository lobbyRepository;

    public Lobby validateSource(UUID id){
        return lobbyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lobby não encontrado: " + id));
    }
}
