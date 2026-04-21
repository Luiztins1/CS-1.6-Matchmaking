package com.unnamed.matchmaking.cs16_matchmaking.validator;

import com.unnamed.matchmaking.cs16_matchmaking.model.Match;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MatchValidator {

    private final MatchRepository matchRepository;

    public void valitade(Match match){

    }

}
