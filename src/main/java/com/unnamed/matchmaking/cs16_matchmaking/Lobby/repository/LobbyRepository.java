package com.unnamed.matchmaking.cs16_matchmaking.Lobby.repository;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LobbyRepository extends JpaRepository<Lobby, UUID> {
}
