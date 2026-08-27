package com.unnamed.matchmaking.cs16_matchmaking.Match.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MatchResponseDTO(
        UUID id,
        String nameMatch,
        GameMap map,
        MatchState matchState,
        TypeMatch typeMatch,
        Instant timeMatchMap,
        UUID lobbyId) {

}
