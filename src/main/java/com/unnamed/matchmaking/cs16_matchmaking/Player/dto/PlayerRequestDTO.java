package com.unnamed.matchmaking.cs16_matchmaking.Player.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.Ranking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record PlayerRequestDTO(
        UUID id,

        @NotBlank(message = "It's not blank.")
        String nickname,

        @NotNull(message = "It's not null.")
        Ranking rank,

        @NotNull(message = "It's not null.")
        Integer kills,

        @NotNull(message = "It's not null.")
        Integer deaths,

        @NotBlank(message = "It's not blank.")
        String country,

        Instant lastConnection,

        UUID matchId,
        UUID lobbyId
) {
}
