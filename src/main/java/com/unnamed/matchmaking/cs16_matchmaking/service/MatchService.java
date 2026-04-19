package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
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

    public Match saveMatch(Match match){
        return matchRepository.save(match);
    }

    public List<Match> findAllMatch(){
        return matchRepository.findAll();
    }

    public Optional<Match> updateMatch(UUID uuid, Match match){
        return matchRepository.findById(uuid).map(existMatch ->{
            existMatch.setMap(match.getMap());
            existMatch.setTimeMatchMap(match.getTimeMatchMap());
            existMatch.setListPlayer(match.getListPlayer());
            return matchRepository.save(existMatch);
        });
    }

    public void deleteMatch(UUID uuid){
        matchRepository.deleteById(uuid);
    }

    public Optional<Match> findByIdMatch(UUID uuid){
        return matchRepository.findById(uuid);
    }

}
