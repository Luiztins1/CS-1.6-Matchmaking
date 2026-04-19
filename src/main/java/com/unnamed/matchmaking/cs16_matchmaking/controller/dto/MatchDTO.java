package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.GameMap;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record MatchDTO(GameMap map, LocalTime timeMatchMap, List<UUID> listPlayer) {
}
