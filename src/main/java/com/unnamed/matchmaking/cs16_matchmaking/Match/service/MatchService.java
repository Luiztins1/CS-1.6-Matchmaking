package com.unnamed.matchmaking.cs16_matchmaking.Match.service;

import com.unnamed.matchmaking.cs16_matchmaking.Match.dto.MatchResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Match.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.MatchNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public Match saveMatch(MatchResponseDTO matchResponseDTO) {
        List<Player> playerList = playerRepository.findAll();

        if(playerList.isEmpty())
            throw new ResourceNotFoundException("Lista de players vazia.");

        Match match = new Match(
                matchResponseDTO.id(),
                matchResponseDTO.nameMatch(),
                matchResponseDTO.map(),
                matchResponseDTO.matchState(),
                matchResponseDTO.timeMatchMap(),
                null,
                playerList
        );

        Lobby lobby = new Lobby(
                UUID.randomUUID(),
                matchResponseDTO.nameMatch(),
                match,
                playerList
        );

        match.setLobbyMatch(lobby);

        playerList.forEach(player -> {
            player.setMatch(match);
            player.setLobby(lobby);
        });

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

        if (match.getListPlayer() != null) {
            match.getListPlayer().forEach(player -> {
                player.setMatch(null);
                player.setLobby(null);
            });
        }
        matchRepository.delete(match);
    }

    public Optional<Match> findByIdMatch(UUID id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException("Partidade não encontrado."));

        return Optional.of(match);
    }

}
