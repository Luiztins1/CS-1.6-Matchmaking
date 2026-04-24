package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record MatchDTO(
        UUID id,

        @NotNull(message = "It's not null.")
        GameMap map,

        @NotNull(message = "It's not null.")
        Instant timeMatchMap,

        @NotNull(message = "It's not null.")
        List<UUID> listPlayer) {

        public static MatchDTO fromEntity(Match match){
                return new MatchDTO(
                        match.getId(),
                        match.getMap(),
                        match.getTimeMatchMap(),
                        match.getListPlayer()
                                .stream()
                                .map(Player::getId)
                                .toList()
                );
        }
}
