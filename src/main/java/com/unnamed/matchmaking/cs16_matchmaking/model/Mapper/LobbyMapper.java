package com.unnamed.matchmaking.cs16_matchmaking.model.Mapper;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Component
@RequiredArgsConstructor
public class LobbyMapper {

    private final PlayerValidator playerValidator;
    private final LobbyValidator lobbyValidator;
    private final MatchValidator matchValidator;

    @Deprecated
    public Lobby createLobby(LobbyDTO lobbyDTO){
        List<Player> lobbyList = lobbyDTO.listLobbyPlayer() != null ?
                lobbyDTO.listLobbyPlayer()
                        .stream()
                        .map(playerValidator::validateSource)
                        .toList()
                : List.of();

        Lobby lobby = new Lobby(
                null,
                null,
                null,
                null
        );

        lobbyList.forEach(player -> player.setLobby(lobby));

        return  lobby;
    }

    @Deprecated
    public Lobby updateLobby(UUID id, LobbyDTO lobbyDTO){
        Lobby lobby = lobbyValidator.validateSource(id);

        List<Player> playerList = lobbyDTO.listLobbyPlayer() != null ? lobbyDTO.listLobbyPlayer()
                .stream()
                .map(playerValidator::validateSource)
                .toList()
                :List.of();

        lobby.setId(lobbyDTO.id());
        lobby.setListLobbyPlayer(playerList);

        return lobby;
    }


    public List<PlayerDTO> updateListLobbyPlayer(UUID matchId, UUID playerId){
        Match match = matchValidator.validateSource(matchId);
        Player player = playerValidator.validateSource(playerId);

        Lobby lobby = match.getLobbyMatch();
        List<Player> listLobbyPlayer = lobby.getListLobbyPlayer();

        if(!listLobbyPlayer.contains(player)){
            player.setLobby(lobby);
            player.setMatch(match);
            listLobbyPlayer.add(player);
        }

        return listLobbyPlayer
                .stream()
                .map(PlayerDTO::fromEntity)
                .toList();
    }

    public void deleteListLobbyPlayer(UUID matchId, UUID playerId){
        Match match = matchValidator.validateSource(matchId);
        Player player = playerValidator.validateSource(playerId);

        Lobby lobby = match.getLobbyMatch();
        List<Player> listLobbyPlayer = lobby.getListLobbyPlayer();

        listLobbyPlayer.remove(player);

        player.setLobby(null);
        player.setMatch(null);
        lobby.setMatchLobby(match);


    }

    @Deprecated
    public Lobby deleteLobby(UUID id){
        return lobbyValidator.validateSource(id);
    }

    public Lobby findByIdLobby(UUID id){
        return lobbyValidator.validateSource(id);
    }

}
