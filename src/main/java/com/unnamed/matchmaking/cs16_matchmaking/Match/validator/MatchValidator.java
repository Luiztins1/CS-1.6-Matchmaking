package com.unnamed.matchmaking.cs16_matchmaking.Match.validator;

import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ChangeStateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
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

    public Match validateIdForReturnNullMapper(PlayerResponseDTO playerResponseDTO){
         if(playerResponseDTO.matchId() == null){
             return null;
         }

         return this.validateSource(playerResponseDTO.matchId());
    }


    public Match validateState(UUID id, MatchState nextState){
        Match match = validateSource(id);

        if(match.getMatchState().currentState(nextState)){
            match.setMatchState(nextState);
            return matchRepository.save(match);
        }

        throw new ChangeStateException("Transição de " + match.getMatchState() + " para " + nextState + " não é possível.");
    }

}
