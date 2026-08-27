package com.unnamed.matchmaking.cs16_matchmaking.Lobby.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record LobbyRequestDTO(
        UUID id,

        @NotBlank(message = "String can't empty.")
        String name,

        @NotNull(message = "It's not null.")
        UUID matchLobby,

        @NotNull(message = "It's not null.")
        List<UUID> listLobbyPlayer) {
}
