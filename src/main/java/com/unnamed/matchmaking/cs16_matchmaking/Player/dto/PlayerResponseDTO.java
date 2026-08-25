package com.unnamed.matchmaking.cs16_matchmaking.Player.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
