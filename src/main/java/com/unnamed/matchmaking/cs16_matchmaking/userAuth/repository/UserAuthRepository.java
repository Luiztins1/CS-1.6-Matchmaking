package com.unnamed.matchmaking.cs16_matchmaking.userAuth.repository;

import com.unnamed.matchmaking.cs16_matchmaking.userAuth.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthRepository extends JpaRepository<UserAuth, UUID> {
    Optional<UserAuth> findByLogin(String login);
    boolean existsByLogin(String login);
    void deleteByLogin(String login);
}
