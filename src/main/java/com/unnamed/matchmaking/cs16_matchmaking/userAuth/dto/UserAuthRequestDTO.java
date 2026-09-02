package com.unnamed.matchmaking.cs16_matchmaking.userAuth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record UserAuthRequestDTO(
        UUID id,

        @NotBlank(message = "Your login can't be empty.")
        String login,

        @NotBlank(message = "Your login can't be empty.")
        String password,

        @NotNull(message = "Your list can't be null.")
        List<String> roles) {
}
