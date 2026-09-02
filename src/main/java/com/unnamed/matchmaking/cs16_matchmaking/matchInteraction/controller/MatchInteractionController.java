package com.unnamed.matchmaking.cs16_matchmaking.matchInteraction.controller;

import com.unnamed.matchmaking.cs16_matchmaking.matchInteraction.dto.MatchInteractionRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.matchInteraction.service.MatchInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/match-interactions")
@RequiredArgsConstructor
public class MatchInteractionController {

    private final MatchInteractionService matchInteractionService;

    @PostMapping
    public ResponseEntity<Boolean> handlerMatchInteraction(@RequestBody MatchInteractionRequestDTO matchInteractionRequestDTO){
        boolean success = matchInteractionService.handlerMatchInteraction(matchInteractionRequestDTO);

        if(success)
            return ResponseEntity.ok(success);

        return ResponseEntity.badRequest().build();
    }
}
