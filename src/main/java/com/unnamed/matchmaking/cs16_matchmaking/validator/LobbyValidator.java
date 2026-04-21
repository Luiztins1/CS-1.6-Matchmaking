package com.unnamed.matchmaking.cs16_matchmaking.validator;

import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LobbyValidator {

    private final LobbyRepository lobbyRepository;
}
