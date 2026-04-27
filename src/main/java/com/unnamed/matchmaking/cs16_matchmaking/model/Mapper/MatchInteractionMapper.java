package com.unnamed.matchmaking.cs16_matchmaking.model.Mapper;

import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.InteractionEvent;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.TypeMatchEvent;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MatchInteractionMapper {

    private final LobbyMapper lobbyMapper;
    private final MatchValidator matchValidator;
    private final MatchMapper matchMapper;

    public boolean processInteraction(InteractionEvent event, UUID matchId, UUID playerId){
       return switch (event){
            case ENTER -> enterInMatch(matchId, playerId);
            case EXIT -> exitMatch(matchId, playerId);
            default -> false;
        };
    }

    public boolean enterInMatch(UUID matchId, UUID playerId){
        lobbyMapper.updateListLobbyPlayer(matchId, playerId);
        return true;
    }

    public boolean exitMatch(UUID matchId, UUID playerId){
        lobbyMapper.deleteListLobbyPlayer(matchId, playerId);
        return true;
    }

    public void enterMatchState(UUID matchId){
        Match match = matchValidator.validateSource(matchId);
        List<Player> playerSize = match.getLobbyMatch().getListLobbyPlayer();

        if(playerSize.isEmpty()){
            matchMapper.updateMatchState(match.getId(), MatchState.COLD);
        }

        else if(playerSize.size() < TypeMatchEvent.COMPETITIVE.getValue()){
            matchMapper.updateMatchState(match.getId(), MatchState.WAITING);
        }

        else if(playerSize.size() == TypeMatchEvent.COMPETITIVE.getValue()){
            matchMapper.updateMatchState(match.getId(), MatchState.READY_MATCH);
        }
    }
}
