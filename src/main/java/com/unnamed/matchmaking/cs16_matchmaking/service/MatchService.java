package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ChangeStateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final MatchValidator matchValidator;
    private final PlayerValidator playerValidator;


    @Transactional
    public Match saveMatch(MatchDTO matchDTO) {
       List<Player> playerList = matchDTO.listPlayer() != null ? matchDTO.listPlayer()
                       .stream()
                       .map(id -> playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player não encontrado. " + id)))
                       .toList()
               : List.of();

        Match match = new Match(
                null,
                matchDTO.map(),
                matchDTO.matchState(),
                matchDTO.timeMatchMap(),
                playerList
        );
        return matchRepository.save(match);
    }

    public List<Match> findAllMatch(){
        return matchRepository.findAll();
    }

    @Transactional
    public Optional<Match> updateMatch(UUID uuid, MatchDTO matchDTO){
        Match match = matchValidator.validateSource(uuid);

        List<Player> playerList = matchDTO.listPlayer() != null ?
                matchDTO.listPlayer()
                        .stream()
                        .map(playerValidator::validateSource)
                        .toList()
                :List.of();

        match.setMap(matchDTO.map());
        match.setTimeMatchMap(matchDTO.timeMatchMap());
        match.setListPlayer(playerList);

        return Optional.of(matchRepository.save(match));
    }

    @Transactional
    public Optional<Match> updateMatchMap(UUID id, GameMap gameMap){
        Match match = matchValidator.validateSource(id);
        match.setMap(gameMap);
        return Optional.of(matchRepository.save(match));
    }

    @Transactional
    public Optional<Match> updateMatchState(UUID uuid, MatchState nextState) {
       return Optional.of(matchValidator.validateState(uuid, nextState));
    }

    @Transactional
    public void deleteMatch(UUID uuid) {
        Match match = matchValidator.validateSource(uuid);
        matchRepository.delete(match);
    }

    public Optional<Match> findByIdMatch(UUID uuid) {
        return Optional.of(matchValidator.validateSource(uuid));
    }

}
