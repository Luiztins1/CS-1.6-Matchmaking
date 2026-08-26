package com.unnamed.matchmaking.cs16_matchmaking.Match.controller;

import com.unnamed.matchmaking.cs16_matchmaking.Match.dto.MatchRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Match.dto.MatchResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Match.mapper.MatchMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.Match.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchResponseDTO> save(@RequestBody @Valid MatchRequestDTO matchRequestDTO){
        Match match1 = matchService.saveMatch(matchRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(match1.getId())
                .toUri();

        return ResponseEntity.created(location).body(MatchMapper.toDto(match1));
    }

    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> findAll(){
        List<MatchResponseDTO> matchList = matchService.findAllMatch()
                .stream()
                .map(MatchMapper::toDto)
                .toList();

        if(matchList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(matchList);
    }

    @PutMapping("/{id}/match-state")
    public ResponseEntity<MatchResponseDTO> updateMatchState(@PathVariable UUID id, @RequestParam MatchState nextState) {
        return matchService.updateMatchState(id, nextState)
                .map(MatchMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> findById(@PathVariable UUID id){
         return matchService.findByIdMatch(id)
                 .map(MatchMapper::toDto)
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
    }
}
