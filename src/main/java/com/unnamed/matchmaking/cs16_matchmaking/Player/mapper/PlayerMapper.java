package com.unnamed.matchmaking.cs16_matchmaking.Player.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    public static PlayerResponseDTO toDto(Player player){
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

    public static Player toEntity(PlayerResponseDTO playerResponseDTO){
        if(playerResponseDTO == null) return null;

        Player player = new Player();

        player.setId(playerResponseDTO.id());
        player.setNickname(playerResponseDTO.nickname());
        player.setRank(playerResponseDTO.rank());
        player.setKills(playerResponseDTO.kills());
        player.setDeaths(playerResponseDTO.deaths());
        player.setCountry(playerResponseDTO.country());
        player.setLastConnection(playerResponseDTO.lastConnection());

        if(playerResponseDTO.matchId() != null){
            Match match = new Match();
            match.setId(playerResponseDTO.matchId());
            player.setMatch(match);
        }

        if(playerResponseDTO.lobbyId() != null){
            Lobby lobby = new Lobby();
            lobby.setId(playerResponseDTO.lobbyId());
            player.setLobby(lobby);
        }

        return player;
    }
}
