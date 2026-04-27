package com.unnamed.matchmaking.cs16_matchmaking.model.Mapper;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.InteractionEvent;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MatchMapper {

    private final MatchValidator matchValidator;
    private final PlayerValidator playerValidator;

    public Match createMatch(MatchDTO matchDTO){
        List<Player> playerList = matchDTO.listPlayer() != null ? matchDTO.listPlayer()
                .stream()
                .map(playerValidator::validateSource)
                .toList()
                : List.of();

        Match match = new Match(
                null,
                matchDTO.nameMatch(),
                matchDTO.map(),
                matchDTO.matchState(),
                matchDTO.timeMatchMap(),
                null,
                playerList
        );

        Lobby lobby = new Lobby(
                null,
                matchDTO.nameMatch(),
                match,
                playerList
        );

        match.setLobbyMatch(lobby);

        playerList.forEach(player -> {
            player.setMatch(match);
            player.setLobby(lobby);
        });

        return match;
    }

    @Deprecated
    public Match updateMatch(UUID uuid, MatchDTO matchDTO){
        Match match = matchValidator.validateSource(uuid);

        List<Player> playerList = matchDTO.listPlayer() != null ?
                matchDTO.listPlayer()
                        .stream()
                        .map(playerValidator::validateSource)
                        .toList()
                :List.of();

        match.setNameMatch(matchDTO.nameMatch());
        match.setMap(matchDTO.map());
        match.setMatchState(matchDTO.matchState());
        match.setTimeMatchMap(matchDTO.timeMatchMap());
        match.setListPlayer(playerList);

        return match;
    }

    public Match updateMatchMap(UUID id, GameMap gameMap){
        return matchValidator.validateSource(id);
    }

    public Match updateMatchState(UUID id, MatchState nextState){
        return matchValidator.validateState(id, nextState);
    }

    public Match deleteMatch(UUID id){
        Match match = matchValidator.validateSource(id);

        if(match.getListPlayer() != null){
            match.getListPlayer().forEach(player -> {
                player.setMatch(null);
                player.setLobby(null);
            });
        }
        return match;
    }

    public Match findByIdMatch(UUID id){
        return matchValidator.validateSource(id);
    }
}
