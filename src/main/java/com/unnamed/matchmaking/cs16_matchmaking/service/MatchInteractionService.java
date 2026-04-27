package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchInteractionDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Mapper.MatchInteractionMapper;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.InteractionEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchInteractionService {

    private final MatchInteractionMapper matchInteractionMapper;

    @Transactional
    public synchronized boolean handlerMatchInteraction(MatchInteractionDTO matchInteractionDTO){
        boolean success = matchInteractionMapper.processInteraction(matchInteractionDTO.event(),
                matchInteractionDTO.matchId(),
                matchInteractionDTO.playerId());
        if(success){
            this.matchInteractionMapper.enterMatchState(matchInteractionDTO.matchId());
        }
        return success;
    }
}
