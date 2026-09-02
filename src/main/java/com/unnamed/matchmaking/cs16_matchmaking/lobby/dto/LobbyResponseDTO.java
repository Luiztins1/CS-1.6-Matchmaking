package com.unnamed.matchmaking.cs16_matchmaking.lobby.dto;

import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;

import java.util.List;
import java.util.UUID;

public record LobbyResponseDTO(

        UUID id,
        String name,
        UUID matchLobby,
        TypeMatch typeMatchEvent,
        List<UUID> listLobbyPlayer) {

}
