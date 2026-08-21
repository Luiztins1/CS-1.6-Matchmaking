package com.unnamed.matchmaking.cs16_matchmaking.Match.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.Match.dto.MatchResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.Match.validator.MatchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchMapper {

    private final MatchValidator matchValidator;
    private final LobbyValidator lobbyValidator;


    public static MatchResponseDTO fromEntity(Match match){
        if(match == null) return null;

        return new MatchResponseDTO(
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

    public Match toDto(MatchResponseDTO matchResponseDTO){
        if(matchResponseDTO == null) return null;

        Match match = matchValidator.validateSource(matchResponseDTO.id());
        Lobby lobby = lobbyValidator.validateSource(matchResponseDTO.lobbyId());

        match.setId(matchResponseDTO.id());
        match.setNameMatch(match.getNameMatch());
        match.setMap(matchResponseDTO.map());
        match.setMatchState(matchResponseDTO.matchState());
        match.setTimeMatchMap(matchResponseDTO.timeMatchMap());
        match.setLobbyMatch(lobby);
        match.setListPlayer(lobby.getListLobbyPlayer());

        return match;
    }
}
