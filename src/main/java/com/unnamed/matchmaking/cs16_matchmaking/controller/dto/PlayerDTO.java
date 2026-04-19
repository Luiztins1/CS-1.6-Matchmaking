package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.Rank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record PlayerDTO(
        @NotBlank(message = "It's not blank.")
        @Size(min = 5, max = 20, message = "About Me must be between 5 and 20 characters")
        String nickname,

        @NotNull(message = "It's not null.")
        Rank rank,

        @NotNull(message = "It's not null.")
        Integer kills,

        @NotNull
        Integer deaths,

        @NotNull
        @Size(min = 0, max = 30, message = "About Me must be between 5 and 20 characters")
        String country,

        @NotBlank(message = "It's not blank.")
        Instant lastConnection,

        UUID matchId,
        UUID lobbyId) {
}
