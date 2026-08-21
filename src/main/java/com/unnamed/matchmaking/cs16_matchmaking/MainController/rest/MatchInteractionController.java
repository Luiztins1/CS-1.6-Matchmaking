package com.unnamed.matchmaking.cs16_matchmaking.MainController.rest;

import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.dto.MatchInteractionResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.MatchInteraction.service.MatchInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/match-interactions")
@RequiredArgsConstructor
public class MatchInteractionController {

    private final MatchInteractionService matchInteractionService;

    @PostMapping
    public ResponseEntity<Boolean> handlerMatchInteraction(@RequestBody MatchInteractionResponseDTO matchInteractionResponseDTO){
        boolean success = matchInteractionService.handlerMatchInteraction(matchInteractionResponseDTO);
        return ResponseEntity.ok(success);
    }
}
