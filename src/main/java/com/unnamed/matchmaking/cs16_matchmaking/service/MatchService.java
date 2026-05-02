package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.GameMap;
import com.unnamed.matchmaking.cs16_matchmaking.model.enums.MatchState;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import com.unnamed.matchmaking.cs16_matchmaking.validator.MatchValidator;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchValidator matchValidator;
    private final PlayerValidator playerValidator;

    @Transactional
    public Match saveMatch(MatchDTO matchDTO) {
        List<Player> playerList = matchDTO.listPlayer() != null ? matchDTO.listPlayer()
                .stream()
                .map(playerValidator::validateSource)
                .toList()
                : List.of();

        Match match = new Match(
                null,
                matchDTO.nameMatch(),
                matchDTO.map(),
                matchDTO.matchState(),
                matchDTO.timeMatchMap(),
                null,
                playerList
        );

        Lobby lobby = new Lobby(
                null,
                matchDTO.nameMatch(),
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
