package com.unnamed.matchmaking.cs16_matchmaking.Match.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.Match.dto.MatchRequestDTO;
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

    public static MatchResponseDTO toDto(Match match){
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

    public static Match toEntity(MatchRequestDTO matchRequestDTO){
        if(matchRequestDTO == null) return null;

        Match match = new Match();
        Lobby lobby = new Lobby();

        match.setId(matchRequestDTO.id());
        match.setNameMatch(matchRequestDTO.nameMatch());
        match.setMap(matchRequestDTO.map());
        match.setMatchState(matchRequestDTO.matchState());
        match.setTimeMatchMap(matchRequestDTO.timeMatchMap());

        match.setLobbyMatch(lobby);
        lobby.setMatchLobby(match);

        match.setListPlayer(lobby.getListLobbyPlayer());

        return match;
    }
}
