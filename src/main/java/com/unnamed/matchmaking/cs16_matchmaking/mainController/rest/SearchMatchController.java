package com.unnamed.matchmaking.cs16_matchmaking.mainController.rest;

import com.unnamed.matchmaking.cs16_matchmaking.match.dto.MatchResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.match.mapper.MatchMapper;
import com.unnamed.matchmaking.cs16_matchmaking.match.repository.MatchRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search-matchs")
@RequiredArgsConstructor
public class SearchMatchController {

    private final MatchRepository matchRepository;

    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> searchMatch(
            @RequestParam @Valid GameMap map) {

        List<MatchResponseDTO> matches = matchRepository.findByMapEquals(map)
                .stream()
                .map(MatchMapper::toDto)
                .toList();
        return matches.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(matches);
    }
}
