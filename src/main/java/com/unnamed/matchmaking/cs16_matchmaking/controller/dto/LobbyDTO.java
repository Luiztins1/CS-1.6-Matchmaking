package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.enums.TypeMatchEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record LobbyDTO(

        UUID id,

        @NotBlank(message = "String can't empty.")
        String name,

        @NotNull(message = "It's not null.")
        UUID matchLobby,

        TypeMatchEvent typeMatchEvent,

        @NotNull(message = "It's not null.")
        List<UUID> listLobbyPlayer) {

}
