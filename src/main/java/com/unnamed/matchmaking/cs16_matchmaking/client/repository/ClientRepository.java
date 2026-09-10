package com.unnamed.matchmaking.cs16_matchmaking.client.repository;

import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByClientLogin(String clientLogin);
    boolean existsByClientLoginOrId(String clientLogin, UUID id);
}
