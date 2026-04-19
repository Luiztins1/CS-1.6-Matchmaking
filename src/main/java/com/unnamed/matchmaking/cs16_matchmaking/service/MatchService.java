package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
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

    public Match saveMatch(MatchDTO matchDTO) {
       List<Player> playerList = matchDTO.listPlayer() != null ? matchDTO.listPlayer()
                       .stream()
                       .map(id -> playerRepository.findById(id).orElseThrow())
                       .toList()
               : List.of();

        Match match = new Match(
                null,
                matchDTO.map(),
                matchDTO.timeMatchMap(),
                playerList
        );
        return matchRepository.save(match);
    }

    public List<Match> findAllMatch(){
        return matchRepository.findAll();
    }

    public Optional<Match> updateMatch(UUID uuid, MatchDTO matchDTO){
        Match match = matchRepository.findById(uuid).orElseThrow();

        List<Player> playerList = matchDTO.listPlayer() != null ?
                matchDTO.listPlayer()
                        .stream()
                        .map(id -> playerRepository.findById(id).orElseThrow())
                        .toList()
                :List.of();

        match.setMap(matchDTO.map());
        match.setTimeMatchMap(matchDTO.timeMatchMap());
        match.setListPlayer(playerList);

        return Optional.of(matchRepository.save(match));
    }

    public void deleteMatch(UUID uuid){
        matchRepository.deleteById(uuid);
    }

    public Optional<Match> findByIdMatch(UUID uuid){
        return matchRepository.findById(uuid);
    }

}
