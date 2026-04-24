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
        Match match = matchRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada. " + uuid));

        List<Player> playerList = matchDTO.listPlayer() != null ?
                matchDTO.listPlayer()
                        .stream()
                        .map(id -> playerRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Lista de players não encontrado. " + id)))
                        .toList()
                :List.of();

        match.setMap(matchDTO.map());
        match.setTimeMatchMap(matchDTO.timeMatchMap());
        match.setListPlayer(playerList);

        return Optional.of(matchRepository.save(match));
    }

    @Transactional
    public Optional<Match> updateMatchMap(UUID id, GameMap gameMap){
        return Optional.of(matchRepository.findById(id)
                .map(mt -> {
                    mt.setMap(gameMap);
                    return matchRepository.save(mt);
                }).orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada. " + id)));
    }

    @Transactional
    public Optional<Match> updateMatchState(UUID uuid, MatchState nextState) {
        Match match = matchRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada. "));

        if(match.getMatchState().currentState(nextState)){
            match.setMatchState(nextState);
            return Optional.of(matchRepository.save(match));
        }

        throw new ChangeStateException("Transição de " + match.getMatchState() + " para " + nextState + " não é possível.");
    }

    @Transactional
    public void deleteMatch(UUID uuid) {
        Match match = matchRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrado." + uuid));
        matchRepository.delete(match);
    }

    public Optional<Match> findByIdMatch(UUID uuid) {
        return Optional.of(matchRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrado." + uuid)));
    }

}
