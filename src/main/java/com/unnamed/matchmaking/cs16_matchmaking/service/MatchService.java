package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Mapper.MatchMapper;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
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
    private final MatchMapper matchMapper;

    @Transactional
    public Match saveMatch(MatchDTO matchDTO) {
        return matchRepository.save(matchMapper.createMatch(matchDTO));
    }

    public List<Match> findAllMatch(){
        return matchRepository.findAll();
    }

    @Transactional
    public Optional<Match> updateMatch(UUID id, MatchDTO matchDTO){
        return Optional.of(matchRepository.save(matchMapper.updateMatch(id, matchDTO)));
    }

    @Transactional
    public Optional<Match> updateMatchMap(UUID id, GameMap gameMap){
        return Optional.of(matchRepository.save(matchMapper.updateMatchMap(id, gameMap)));
    }

    @Transactional
    public Optional<Match> updateMatchState(UUID id, MatchState nextState) {
       return Optional.of(matchMapper.updateMatchState(id, nextState));
    }

    @Transactional
    public void deleteMatch(UUID id) {
        matchRepository.delete(matchMapper.deleteMatch(id));
    }

    public Optional<Match> findByIdMatch(UUID id) {
        return Optional.of(matchMapper.findByIdMatch(id));
    }

}
