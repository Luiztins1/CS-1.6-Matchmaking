package com.unnamed.matchmaking.cs16_matchmaking.client.dto;

import java.util.UUID;

public record ClientResponseDto(
        UUID id,
        String clientLogin,
        String clientPassword,
        String redirectUri,
        String scope) {
}
