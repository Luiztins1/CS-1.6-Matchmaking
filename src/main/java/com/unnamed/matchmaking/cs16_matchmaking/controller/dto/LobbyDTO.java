package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.LobbyState;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record LobbyDTO(

        UUID id,

        @NotBlank(message = "String can't empty.")
        String lobby,

        @NotNull(message = "States can't null")
        LobbyState state,

        @NotNull(message = "It's not null.")
        List<UUID> listLobby) {
        public static LobbyDTO fromEntity(Lobby lobby){
                return new LobbyDTO(
                        lobby.getId(),
                        lobby.getLobby(),
                        lobby.getState(),
                        lobby.getListLobby()
                                .stream()
                                .map(Player::getId)
                                .toList()
                );
        }
}
