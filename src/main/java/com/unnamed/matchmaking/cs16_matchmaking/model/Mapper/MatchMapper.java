package com.unnamed.matchmaking.cs16_matchmaking.model.Mapper;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
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
                matchDTO.map(),
                matchDTO.matchState(),
                matchDTO.timeMatchMap(),
                playerList
        );

        playerList.forEach(player -> player.setMatch(match));
        return match;
    }

    public Match updateMatch(UUID uuid, MatchDTO matchDTO){
        Match match = matchValidator.validateSource(uuid);

        List<Player> playerList = matchDTO.listPlayer() != null ?
                matchDTO.listPlayer()
                        .stream()
                        .map(playerValidator::validateSource)
                        .toList()
                :List.of();

        match.setMap(matchDTO.map());
        match.setTimeMatchMap(matchDTO.timeMatchMap());
        match.setListPlayer(playerList);

        return match;
    }

    public Match updateMatchMap(UUID id, GameMap gameMap){
        Match match = matchValidator.validateSource(id);
        match.setMap(gameMap);
        return match;
    }

    public Match updateMatchState(UUID id, MatchState nextState){
        return matchValidator.validateState(id, nextState);
    }

    public Match deleteMatch(UUID id){
        return matchValidator.validateSource(id);
    }

    public Match findByIdMatch(UUID id){
        return matchValidator.validateSource(id);
    }
}
