package com.unnamed.matchmaking.cs16_matchmaking.Match.service;

import com.unnamed.matchmaking.cs16_matchmaking.Match.dto.MatchResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.Match.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.Match.repository.MatchRepository;
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
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchValidator matchValidator;
    private final PlayerValidator playerValidator;

    @Transactional
    public Match saveMatch(MatchResponseDTO matchResponseDTO) {
        List<Player> playerList = matchResponseDTO.listPlayer() != null ? matchResponseDTO.listPlayer()
                .stream()
                .map(playerValidator::validateSource)
                .toList()
                : List.of();

        Match match = new Match(
                null,
                matchResponseDTO.nameMatch(),
                matchResponseDTO.map(),
                matchResponseDTO.matchState(),
                matchResponseDTO.timeMatchMap(),
                null,
                playerList
        );

        Lobby lobby = new Lobby(
                null,
                matchResponseDTO.nameMatch(),
                match,
                playerList
        );

        match.setLobbyMatch(lobby);

        playerList.forEach(player -> {
            player.setMatch(match);
            player.setLobby(lobby);
        });

        return matchRepository.save(match);
    }

    public List<Match> findAllMatch(){
        return matchRepository.findAll();
    }

    @Transactional
    public Optional<Match> updateMatchState(UUID id, MatchState nextState) {
       return Optional.of(matchRepository.save(matchValidator.validateState(id, nextState)));
    }


    @Transactional
    public void deleteMatch(UUID id) {
        Match match = matchValidator.validateSource(id);

        if(match.getListPlayer() != null){
            match.getListPlayer().forEach(player -> {
                player.setMatch(null);
                player.setLobby(null);
            });
        }
        matchRepository.delete(match);
    }

    public Optional<Match> findByIdMatch(UUID id) {
        return Optional.of(matchValidator.validateSource(id));
    }

}
