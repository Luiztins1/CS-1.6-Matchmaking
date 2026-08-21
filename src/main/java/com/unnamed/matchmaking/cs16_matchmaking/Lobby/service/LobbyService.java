package com.unnamed.matchmaking.cs16_matchmaking.Lobby.service;

import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Player.mapper.PlayerMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.validator.LobbyValidator;
import com.unnamed.matchmaking.cs16_matchmaking.Match.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.Player.validator.PlayerValidator;
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
    public Optional<List<PlayerResponseDTO>> addListLobbyPlayer(UUID matchId, UUID playerId){
        Match match = matchValidator.validateSource(matchId);
        Player player = playerValidator.validateSource(playerId);

        Lobby lobby = match.getLobbyMatch();
        List<Player> listLobbyPlayer = lobby.getListLobbyPlayer();

        if(!listLobbyPlayer.contains(player)){
            player.setLobby(lobby);
            player.setMatch(match);
            listLobbyPlayer.add(player);
        }

        List<PlayerResponseDTO> playerResponseDTOS = listLobbyPlayer
                .stream()
                .map(PlayerMapper::fromEntity)
                .toList();

        return Optional.of(playerResponseDTOS);
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
