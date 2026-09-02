package com.unnamed.matchmaking.cs16_matchmaking.player.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;

import java.time.Instant;
import java.util.UUID;

public record PlayerResponseDTO(
        UUID id,
        String nickname,
        Ranking rank,
        Integer kills,
        Integer deaths,
        String country,
        Instant lastConnection,
        UUID matchId,
        UUID lobbyId) {
}
