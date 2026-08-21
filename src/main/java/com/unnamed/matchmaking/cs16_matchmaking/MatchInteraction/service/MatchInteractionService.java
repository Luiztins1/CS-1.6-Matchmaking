package com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.service;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.service.LobbyService;
import com.unnamed.matchmaking.cs16_matchmaking.Match.service.MatchService;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.dto.MatchInteractionResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.enums.TypeMatch;
import com.unnamed.matchmaking.cs16_matchmaking.Match.validator.MatchValidator;
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
    public synchronized boolean handlerMatchInteraction(MatchInteractionResponseDTO matchInteractionResponseDTO){
        boolean success =  switch (matchInteractionResponseDTO.event()){
            case ENTER -> enterInMatch(matchInteractionResponseDTO.matchId(), matchInteractionResponseDTO.playerId());
            case EXIT -> exitMatch(matchInteractionResponseDTO.matchId(), matchInteractionResponseDTO.playerId());
            default -> false;
        };

        if(success){
            this.enterMatchState(matchInteractionResponseDTO.matchId());
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

        else if(playerSize.size() < TypeMatch.COMPETITIVE.getValue()){
            if(playerSize.size() > 1){
                return;
            }
            matchService.updateMatchState(match.getId(), MatchState.WAITING);
        }

        else if(playerSize.size() == TypeMatch.COMPETITIVE.getValue()){
            matchService.updateMatchState(match.getId(), MatchState.READY_MATCH);
        }
    }


}
