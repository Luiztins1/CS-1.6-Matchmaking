package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.service.MatchService;
import jakarta.persistence.Lob;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchDTO> save(@RequestBody @Valid MatchDTO matchDTO){
        Match match1 = matchService.saveMatch(matchDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(match1.getId())
                .toUri();

        return ResponseEntity.created(location).body(MatchDTO.fromEntity(match1));
    }

    @GetMapping
    public ResponseEntity<List<MatchDTO>> findAll(){
        List<MatchDTO> matchList = matchService.findAllMatch()
                .stream()
                .map(MatchDTO::fromEntity)
                .toList();

        if(matchList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(matchList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid MatchDTO matchDTO){
        Optional<Match> matchOptional = matchService.updateMatch(id, matchDTO);

        if(matchOptional.isPresent()){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> findById(@PathVariable UUID id){
         return matchService.findByIdMatch(id)
                 .map(MatchDTO::fromEntity)
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
    }
}
