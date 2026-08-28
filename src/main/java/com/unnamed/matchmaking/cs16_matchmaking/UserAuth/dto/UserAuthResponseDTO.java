package com.unnamed.matchmaking.cs16_matchmaking.UserAuth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record UserAuthResponseDTO(
        UUID id,

        String login,
        String password,
        List<String> roles) {
}
