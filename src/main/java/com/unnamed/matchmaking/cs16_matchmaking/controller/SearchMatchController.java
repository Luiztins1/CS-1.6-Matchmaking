package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.mapper.MatchMapper;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/search-matchs")
@RequiredArgsConstructor
public class SearchMatchController {

    private final MatchRepository matchRepository;

    @GetMapping
    public ResponseEntity<List<MatchDTO>> searchMatch(
            @RequestParam @Valid GameMap map) {

        List<MatchDTO> matches = matchRepository.findByMapEquals(map)
                .stream()
                .map(MatchMapper::fromEntity)
                .toList();
        return matches.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(matches);
    }
}
