package com.unnamed.matchmaking.cs16_matchmaking.lobby.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.lobby.dto.LobbyResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LobbyMapper {

    public static LobbyResponseDTO toDto(Lobby lobby){
        if(lobby == null) return null;

        return new LobbyResponseDTO(
                lobby.getId(),
                lobby.getName(),
                lobby.getMatchLobby().getId(),
                lobby.getTypeMatchEvent(),
                lobby.getListLobbyPlayer()
                        .stream()
                        .map(Player::getId)
                        .toList()
        );
    }
}
