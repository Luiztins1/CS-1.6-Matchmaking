package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.Rank;

import java.time.Instant;
import java.util.UUID;

public record PlayerDTO(
        String nickname,
        Rank rank,
        Integer kills,
        Integer deaths,
        String country,
        Instant lastConnection,
        UUID matchId,
        UUID lobbyId) {
}
