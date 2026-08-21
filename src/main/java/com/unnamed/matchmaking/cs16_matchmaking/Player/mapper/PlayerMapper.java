package com.unnamed.matchmaking.cs16_matchmaking.Player.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    public static PlayerResponseDTO fromEntity(Player player){
        if(player == null) return null;

        return new PlayerResponseDTO(
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
