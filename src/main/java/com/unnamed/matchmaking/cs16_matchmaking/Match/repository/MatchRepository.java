package com.unnamed.matchmaking.cs16_matchmaking.Match.repository;

import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.enums.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {
    List<Match> findByMapEquals(GameMap map);
}
