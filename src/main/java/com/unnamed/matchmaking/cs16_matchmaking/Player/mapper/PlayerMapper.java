package com.unnamed.matchmaking.cs16_matchmaking.Player.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerRequestDTO;
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

    public static Player toEntity(PlayerRequestDTO playerRequestDTO){
        if(playerRequestDTO == null) return null;

        Player player = new Player();

        player.setId(playerRequestDTO.id());
        player.setNickname(playerRequestDTO.nickname());
        player.setRank(playerRequestDTO.rank());
        player.setKills(playerRequestDTO.kills());
        player.setDeaths(playerRequestDTO.deaths());
        player.setCountry(playerRequestDTO.country());
        player.setLastConnection(playerRequestDTO.lastConnection());

        if(playerRequestDTO.matchId() != null){
            Match match = new Match();
            match.setId(playerRequestDTO.matchId());
            player.setMatch(match);
        }

        if(playerRequestDTO.lobbyId() != null){
            Lobby lobby = new Lobby();
            lobby.setId(playerRequestDTO.lobbyId());
            player.setLobby(lobby);
        }

        return player;
    }
}
