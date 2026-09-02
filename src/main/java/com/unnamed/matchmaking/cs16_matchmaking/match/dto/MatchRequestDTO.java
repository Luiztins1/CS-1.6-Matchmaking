package com.unnamed.matchmaking.cs16_matchmaking.match.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record MatchRequestDTO(
        UUID id,

        @NotBlank(message = "It's not blank.")
        String nameMatch,

        @NotNull(message = "It's not null.")
        GameMap map,

        @NotNull(message = "It's not null.")
        MatchState matchState,

        @NotNull(message = "It's not null")
        TypeMatch typeMatch,

        @NotNull(message = "It's not null.")
        Instant timeMatchMap,

        UUID lobbyId) {
}
