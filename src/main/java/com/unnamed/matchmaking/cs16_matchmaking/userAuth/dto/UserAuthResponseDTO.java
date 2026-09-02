package com.unnamed.matchmaking.cs16_matchmaking.userAuth.dto;

import java.util.List;
import java.util.UUID;

public record UserAuthResponseDTO(
        UUID id,

        String login,
        String password,
        List<String> roles) {
}
