package com.unnamed.matchmaking.cs16_matchmaking.client.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ClientRequestDto(
        UUID id,

        @NotBlank(message = "Client Login doesn't can null or empty.")
        String clientLogin,

        @NotBlank(message = "Client Password doesn't can null or empty.")
        String clientPassword,

        @NotBlank(message = "Redirect Uri doesn't can null or empty.")
        String redirectUri,

        @NotBlank(message = "Scope doesn't can null or empty.")
        String scope) {
}
