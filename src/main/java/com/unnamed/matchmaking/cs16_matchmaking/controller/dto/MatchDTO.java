package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record MatchDTO(
        UUID id,

        @NotBlank(message = "It's not blank.")
        String nameMatch,

        @NotNull(message = "It's not null.")
        GameMap map,

        @NotNull(message = "It's not null.")
        MatchState matchState,

        @NotNull(message = "It's not null.")
        Instant timeMatchMap,

        UUID lobbyId,

        @NotNull(message = "It's not null.")
        List<UUID> listPlayer) {

        public static MatchDTO fromEntity(Match match){
                return new MatchDTO(
                        match.getId(),
                        match.getNameMatch(),
                        match.getMap(),
                        match.getMatchState(),
                        match.getTimeMatchMap(),
                        match.getLobbyMatch().getId(),
                        match.getListPlayer()
                                .stream()
                                .map(Player::getId)
                                .toList()
                );
        }
}
