package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;

import java.util.List;
import java.util.UUID;

public record LobbyDTO(String lobby, List<UUID> listLobby) {
}
