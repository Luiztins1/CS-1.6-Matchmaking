package com.unnamed.matchmaking.cs16_matchmaking.lobby.service;

import com.unnamed.matchmaking.cs16_matchmaking.match.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.repository.LobbyRepository;
import com.unnamed.matchmaking.cs16_matchmaking.player.repository.PlayerRepository;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.LobbyNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.MatchNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.PlayerNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
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
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    public List<Lobby> findAllLobby(){
        return lobbyRepository.findAll();
    }

    @Transactional
    public Lobby addListLobbyPlayer(UUID matchId, UUID playerId){

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match não encontrada."));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player não encontrado."));

        //Receives Lobby that be in Match.
        Lobby lobby = match.getLobbyMatch();

        //Receives List of Players in Lobby.
        List<Player> listLobbyPlayer = lobby.getListLobbyPlayer();

        //Whether list of player not contains a player,
        //than an player that was recovered in database let's be add
        // in Lobby, Match and listLobbyPlayer.
        if(!listLobbyPlayer.contains(player)){
            player.setLobby(lobby);
            player.setMatch(match);
            listLobbyPlayer.add(player);
        }

        return lobbyRepository.save(lobby);
    }

    @Transactional
    public void removeListLobbyPlayer(UUID matchId, UUID playerId){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match não encontrado."));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player não encontrado."));

        Lobby lobby = match.getLobbyMatch();

        List<Player> listLobbyPlayer = lobby.getListLobbyPlayer();

        if(listLobbyPlayer.isEmpty())
            throw new ResourceNotFoundException("Lista de players está vazia.");

        listLobbyPlayer.remove(player);

        player.setLobby(null);
        player.setMatch(null);

        lobby.setMatchLobby(match);

        matchRepository.save(match);
    }

    public Optional<Lobby> findByIdLobby(UUID id){
        Lobby lobby = lobbyRepository.findById(id)
                .orElseThrow(() -> new LobbyNotFoundException("Lobby não encontrado."));

        return Optional.of(lobby);
    }
}
