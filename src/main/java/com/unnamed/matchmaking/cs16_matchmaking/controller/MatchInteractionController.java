package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchInteractionDTO;
import com.unnamed.matchmaking.cs16_matchmaking.service.MatchInteractionService;
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
    public ResponseEntity<Boolean> handlerMatchInteraction(@RequestBody MatchInteractionDTO matchInteractionDTO){
        boolean success = matchInteractionService.handlerMatchInteraction(matchInteractionDTO);
        return ResponseEntity.ok(success);
    }
}
