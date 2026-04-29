package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.Mapper.LobbyMapper;
import com.unnamed.matchmaking.cs16_matchmaking.model.Mapper.PlayerMapper;
import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final MatchValidator matchValidator;
    private final PlayerValidator playerValidator;
    private final LobbyValidator lobbyValidator;

    public List<Lobby> findAllLobby(){
        return lobbyRepository.findAll();
    }

    @Transactional
    public Optional<List<PlayerDTO>> addListLobbyPlayer(UUID matchId, UUID playerId){
        Match match = matchValidator.validateSource(matchId);
        Player player = playerValidator.validateSource(playerId);

        Lobby lobby = match.getLobbyMatch();
        List<Player> listLobbyPlayer = lobby.getListLobbyPlayer();

        if(!listLobbyPlayer.contains(player)){
            player.setLobby(lobby);
            player.setMatch(match);
            listLobbyPlayer.add(player);
        }

        List<PlayerDTO> playerDTOS = listLobbyPlayer
                .stream()
                .map(PlayerMapper::fromEntity)
                .toList();

        return Optional.of(playerDTOS);
    }

    @Transactional
    public void removeListLobbyPlayer(UUID matchId, UUID playerId){
        Match match = matchValidator.validateSource(matchId);
        Player player = playerValidator.validateSource(playerId);

        Lobby lobby = match.getLobbyMatch();
        List<Player> listLobbyPlayer = lobby.getListLobbyPlayer();

        listLobbyPlayer.remove(player);

        player.setLobby(null);
        player.setMatch(null);
        lobby.setMatchLobby(match);
    }

    public Optional<Lobby> findByIdLobby(UUID id){
        return Optional.of(lobbyValidator.validateSource(id));
    }
}
