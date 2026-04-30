package com.unnamed.matchmaking.cs16_matchmaking.model.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LobbyMapper {

    private final MatchValidator matchValidator;
    private final LobbyValidator lobbyValidator;

    public static LobbyDTO fromEntity(Lobby lobby){
        if(lobby == null) return null;


        return new LobbyDTO(
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

    public Lobby toDto(LobbyDTO lobbyDTO){
       if(lobbyDTO == null) return null;

       Lobby lobby = new Lobby();
       Match match = matchValidator.validateSource(lobbyDTO.id());

       lobby.setId(lobbyDTO.id());
       lobby.setName(lobbyDTO.name());
       lobby.setMatchLobby(match);
       lobby.setListLobbyPlayer(lobbyValidator.validateListLobby(match.getId()));
       
       return lobby;
    }
}
