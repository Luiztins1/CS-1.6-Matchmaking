package com.unnamed.matchmaking.cs16_matchmaking.player.repository;

import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    boolean existsByIdOrNickname(UUID id, String nickname);
}
