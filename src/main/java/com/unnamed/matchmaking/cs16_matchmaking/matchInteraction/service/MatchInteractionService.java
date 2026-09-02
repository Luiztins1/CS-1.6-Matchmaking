package com.unnamed.matchmaking.cs16_matchmaking.matchInteraction.service;

import com.unnamed.matchmaking.cs16_matchmaking.lobby.service.LobbyService;
import com.unnamed.matchmaking.cs16_matchmaking.match.service.MatchService;
import com.unnamed.matchmaking.cs16_matchmaking.matchInteraction.dto.MatchInteractionRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.player.service.PlayerService;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.MatchNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchInteractionService {

    private final LobbyService lobbyService;
    private final MatchService matchService;
    private final PlayerService playerService;

    @Transactional
    public boolean handlerMatchInteraction(MatchInteractionRequestDTO matchInteractionRequestDTO){
        Match match = matchService.findByIdMatch(matchInteractionRequestDTO.matchId())
                .orElseThrow(() -> new MatchNotFoundException("Match não encontrado."));

        if(match.getMatchState() == MatchState.READY_MATCH){
            return false;
        }

        boolean success =  switch (matchInteractionRequestDTO.event()){
            case ENTER -> enterInMatch(matchInteractionRequestDTO.matchId(), matchInteractionRequestDTO.playerId());
            case EXIT -> exitMatch(matchInteractionRequestDTO.matchId(), matchInteractionRequestDTO.playerId());
            default -> false;
        };

        if(success){
            this.updateMatchStateBasedOnLobbySize(match);
        }

        return success;
    }

    @Transactional
    public boolean enterInMatch(UUID matchId, UUID playerId){
        Optional<Match> match = matchService.findByIdMatch(matchId);
        Optional<Player> player = playerService.findByIdPlayer(playerId);

        if(match.isPresent() && player.isPresent()){
            lobbyService.addListLobbyPlayer(matchId, playerId);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean exitMatch(UUID matchId, UUID playerId){
        Optional<Match> match = matchService.findByIdMatch(matchId);
        Optional<Player> player = playerService.findByIdPlayer(playerId);

        if(match.isPresent() && player.isPresent()){
            lobbyService.removeListLobbyPlayer(matchId, playerId);
            return true;
        }
        return false;
    }

    public void updateMatchStateBasedOnLobbySize(Match match){
        int currentPlayer = match.getLobbyMatch().getListLobbyPlayer().size();
        int maxPlayer = match.getTypeMatch().getValue();

        MatchState nowState;

        if(currentPlayer == 0){
            nowState = MatchState.COLD;
        } else if(currentPlayer < maxPlayer){
            nowState = MatchState.WAITING;
        }else {
            nowState = MatchState.READY_MATCH;
        }

        if(match.getMatchState() != nowState)
            matchService.updateMatchState(match.getId(), nowState);

    }
}
