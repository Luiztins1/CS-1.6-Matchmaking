package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.GameMap;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record MatchDTO(
        @NotBlank(message = "It's not blank.")
        GameMap map,

        @NotBlank(message = "It's not blank.")
        LocalTime timeMatchMap,

        @NotBlank(message = "It's not blank.")
        List<UUID> listPlayer) {
}
