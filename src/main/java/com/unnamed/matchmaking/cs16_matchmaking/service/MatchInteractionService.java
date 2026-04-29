package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchInteractionDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.TypeMatchEvent;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchInteractionService {

    private final LobbyService lobbyService;
    private final MatchValidator matchValidator;
    private final MatchService matchService;

    @Transactional
    public synchronized boolean handlerMatchInteraction(MatchInteractionDTO matchInteractionDTO){
        boolean success =  switch (matchInteractionDTO.event()){
            case ENTER -> enterInMatch(matchInteractionDTO.matchId(), matchInteractionDTO.playerId());
            case EXIT -> exitMatch(matchInteractionDTO.matchId(), matchInteractionDTO.playerId());
            default -> false;
        };

        if(success){
            this.enterMatchState(matchInteractionDTO.matchId());
        }

        return success;
    }

    @Transactional
    public boolean enterInMatch(UUID matchId, UUID playerId){
        Match match = matchValidator.validateSource(matchId);
        if(match.getMatchState() == MatchState.READY_MATCH){
            return false;
        }

        lobbyService.addListLobbyPlayer(matchId, playerId);
        return true;
    }

    @Transactional
    public boolean exitMatch(UUID matchId, UUID playerId){
        Match match = matchValidator.validateSource(matchId);
        if(match.getMatchState() == MatchState.READY_MATCH){
            return false;
        }
        lobbyService.removeListLobbyPlayer(matchId, playerId);
        return true;
    }

    @Transactional
    public void enterMatchState(UUID matchId){
        Match match = matchValidator.validateSource(matchId);
        List<Player> playerSize = match.getLobbyMatch().getListLobbyPlayer();

        if(playerSize.isEmpty()){
            matchService.updateMatchState(match.getId(), MatchState.COLD);
        }

        else if(playerSize.size() < TypeMatchEvent.COMPETITIVE.getValue()){
            if(playerSize.size() > 1){
                return;
            }
            matchService.updateMatchState(match.getId(), MatchState.WAITING);
        }

        else if(playerSize.size() == TypeMatchEvent.COMPETITIVE.getValue()){
            matchService.updateMatchState(match.getId(), MatchState.READY_MATCH);
        }
    }


}
