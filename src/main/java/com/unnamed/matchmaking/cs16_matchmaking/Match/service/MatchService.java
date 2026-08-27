package com.unnamed.matchmaking.cs16_matchmaking.Match.service;

import com.unnamed.matchmaking.cs16_matchmaking.Match.dto.MatchRequestDTO;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.MatchNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public Match saveMatch(MatchRequestDTO matchRequestDTO) {
        Match match = new Match(
                matchRequestDTO.id(),
                matchRequestDTO.nameMatch(),
                matchRequestDTO.map(),
                matchRequestDTO.matchState(),
                matchRequestDTO.typeMatch(),
                matchRequestDTO.timeMatchMap(),
                null
        );

        Lobby lobby = new Lobby(
                UUID.randomUUID(),
                matchRequestDTO.nameMatch(),
                match,
                new ArrayList<>()
        );

        match.setLobbyMatch(lobby);

        return matchRepository.save(match);
    }

    public List<Match> findAllMatch() {
        return matchRepository.findAll();
    }

    @Transactional
    public Optional<Match> updateMatchState(UUID id, MatchState nextState) {
        Match match = findByIdMatch(id)
                .orElseThrow(() -> new MatchNotFoundException("Match não encontrado."));

        match.setMatchState(nextState);

        return Optional.of(matchRepository.save(match));
    }


    @Transactional
    public void deleteMatch(UUID id) {
        Match match = findByIdMatch(id)
                .orElseThrow(() -> new MatchNotFoundException("Match não encontrado."));

        matchRepository.delete(match);
    }

    public Optional<Match> findByIdMatch(UUID id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException("Partidade não encontrado."));

        return Optional.of(match);
    }

}
