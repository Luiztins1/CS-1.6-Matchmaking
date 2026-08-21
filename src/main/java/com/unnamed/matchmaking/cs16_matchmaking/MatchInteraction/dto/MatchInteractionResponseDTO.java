package com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.InteractionEvent;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MatchInteractionResponseDTO(
        @NotNull(message = "It's not null.")
        InteractionEvent event,

        UUID matchId,
        UUID playerId) {
}
