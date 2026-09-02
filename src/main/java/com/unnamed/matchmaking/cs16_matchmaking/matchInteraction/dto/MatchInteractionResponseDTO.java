package com.unnamed.matchmaking.cs16_matchmaking.matchInteraction.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.InteractionEvent;

import java.util.UUID;

public record MatchInteractionResponseDTO(
        InteractionEvent event,
        UUID matchId,
        UUID playerId) {
}
