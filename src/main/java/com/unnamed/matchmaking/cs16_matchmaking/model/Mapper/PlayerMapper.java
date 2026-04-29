package com.unnamed.matchmaking.cs16_matchmaking.model.Mapper;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    public static PlayerDTO fromEntity(Player player){
        if(player == null) return null;

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
