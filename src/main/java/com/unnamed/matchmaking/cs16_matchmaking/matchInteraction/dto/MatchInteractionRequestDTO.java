package com.unnamed.matchmaking.cs16_matchmaking.matchInteraction.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.InteractionEvent;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MatchInteractionRequestDTO(
        @NotNull(message = "It's not null.")
        InteractionEvent event,

        UUID matchId,
        UUID playerId) {
}
