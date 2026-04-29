package com.unnamed.matchmaking.cs16_matchmaking.model.Mapper;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
