package com.unnamed.matchmaking.cs16_matchmaking.model.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchMapper {

    private final MatchValidator matchValidator;
    private final LobbyValidator lobbyValidator;


    public static MatchDTO fromEntity(Match match){
        if(match == null) return null;

        return new MatchDTO(
                match.getId(),
                match.getNameMatch(),
                match.getMap(),
                match.getMatchState(),
                match.getTimeMatchMap(),
                match.getLobbyMatch().getId(),
                match.getListPlayer()
                        .stream()
                        .map(Player::getId)
                        .toList()
        );
    }

    public Match toDto(MatchDTO matchDTO){
        if(matchDTO == null) return null;

        Match match = matchValidator.validateSource(matchDTO.id());
        Lobby lobby = lobbyValidator.validateSource(matchDTO.lobbyId());

        match.setId(matchDTO.id());
        match.setNameMatch(match.getNameMatch());
        match.setMap(matchDTO.map());
        match.setMatchState(matchDTO.matchState());
        match.setTimeMatchMap(matchDTO.timeMatchMap());
        match.setLobbyMatch(lobby);
        match.setListPlayer(lobby.getListLobbyPlayer());

        return match;
    }
}
