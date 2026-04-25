package com.unnamed.matchmaking.cs16_matchmaking.validator;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ChangeStateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MatchValidator {

    private final MatchRepository matchRepository;

    public Match validateSource(UUID id){
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada: " + id));
    }

    public Match validateIdForReturnNullMapper(PlayerDTO playerDTO){
         if(playerDTO.matchId() == null){
             return null;
         }

         return this.validateSource(playerDTO.matchId());
    }


    public Match validateState(UUID id, MatchState nextState){
        Match match = validateSource(id);

        if(match.getMatchState().currentState(nextState)){
            match.setMatchState(nextState);
            matchRepository.save(match);
        }

        throw new ChangeStateException("Transição de " + match.getMatchState() + " para " + nextState + " não é possível.");
    }

}
