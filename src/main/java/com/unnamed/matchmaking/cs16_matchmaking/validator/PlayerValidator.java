package com.unnamed.matchmaking.cs16_matchmaking.validator;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlayerValidator {

    private final PlayerRepository playerRepository;

    public void validateDuplicate(Player player){
        if(duplicatePlayer(player)){
            throw new DuplicateException("Player já cadastrado.");
        }
    }

    public Player validateSource(UUID id){
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player não encontrado: " + id));
    }


    public boolean duplicatePlayer(Player player){
        return playerRepository.existsByIdOrNickname(player.getId(), player.getNickname());
    }
}
