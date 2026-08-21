package com.unnamed.matchmaking.cs16_matchmaking.Lobby.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.dto.LobbyResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.Match.validator.MatchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LobbyMapper {

    private final MatchValidator matchValidator;
    private final LobbyValidator lobbyValidator;

    public static LobbyResponseDTO fromEntity(Lobby lobby){
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

    public Lobby toDto(LobbyResponseDTO lobbyResponseDTO){
       if(lobbyResponseDTO == null) return null;

       Lobby lobby = new Lobby();
       Match match = matchValidator.validateSource(lobbyResponseDTO.id());

       lobby.setId(lobbyResponseDTO.id());
       lobby.setName(lobbyResponseDTO.name());
       lobby.setMatchLobby(match);
       lobby.setListLobbyPlayer(lobbyValidator.validateListLobby(match.getId()));
       
       return lobby;
    }
}
