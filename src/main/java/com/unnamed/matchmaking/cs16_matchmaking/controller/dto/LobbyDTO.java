package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record LobbyDTO(

        @NotBlank(message = "String can't empty.")
        @Size(min = 5, max = 22)
        String lobby,

        @NotNull(message = "It's not null.")
        List<UUID> listLobby) {
}
