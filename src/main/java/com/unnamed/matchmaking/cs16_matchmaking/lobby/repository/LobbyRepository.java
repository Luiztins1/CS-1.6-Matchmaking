package com.unnamed.matchmaking.cs16_matchmaking.lobby.repository;

import com.unnamed.matchmaking.cs16_matchmaking.lobby.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LobbyRepository extends JpaRepository<Lobby, UUID> {
}
