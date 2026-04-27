package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.enums.InteractionEvent;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MatchInteractionDTO(
        @NotNull(message = "It's not null.")
        InteractionEvent event,

        UUID matchId,
        UUID playerId) {
}
