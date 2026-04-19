package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.Rank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public record PlayerDTO(
        UUID id, @NotBlank(message = "It's not blank.")
        @Size(min = 4, max = 20, message = "About Me must be between 4 and 20 characters")
        String nickname,

        @NotNull(message = "It's not null.")
        Rank rank,

        @NotNull(message = "It's not null.")
        Integer kills,

        @NotNull(message = "It's not null.")
        Integer deaths,

        @NotBlank(message = "It's not blank.")
        @Size(min = 0, max = 30, message = "About Me must be between 0 and 20 characters")
        String country,

        @NotNull(message = "It's not null.")
        Instant lastConnection,

        UUID matchId,
        UUID lobbyId) {

        public static PlayerDTO fromEntity(Player player){
                return new PlayerDTO(
                        player.getId(),
                        player.getNickname(),
                        player.getRank(),
                        player.getKills(),
                        player.getDeaths(),
                        player.getCountry(),
                        player.getLastConnection(),
                        player.getMatch() != null ? player.getMatch().getId() : null,
                        player.getLobby() != null ? player.getLobby().getId() : null
                );
        }
}
