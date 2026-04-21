package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.ResponseErrorDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.model.Player;
import com.unnamed.matchmaking.cs16_matchmaking.service.PlayerService;
import com.unnamed.matchmaking.cs16_matchmaking.validator.PlayerValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerValidator playerValidator;

    @PostMapping
    public ResponseEntity<PlayerDTO> save(@RequestBody @Valid PlayerDTO playerDTO){
        Player player1 = playerService.savePlayer(playerDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(player1.getId())
                .toUri();
        return ResponseEntity.created(location).body(PlayerDTO.fromEntity(player1));
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> findAll(){
        List<PlayerDTO> playerList = playerService.findAllPlayer()
                .stream()
                .map(PlayerDTO::fromEntity)
                .toList();

        if(playerList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(playerList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid PlayerDTO playerDTO){
        Optional<Player> playerOptional = playerService.updatePlayer(id, playerDTO);

        if(playerOptional.isPresent()){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}/update-relationships")
    public ResponseEntity<PlayerDTO> updateRelationships(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID matchId,
            @RequestParam(required = false) UUID lobbyId){

        return playerService.updateRelationships(id, matchId, lobbyId)
                .map(PlayerDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> findById(@PathVariable UUID id){
        return playerService.findByIdPlayer(id)
                .map(PlayerDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
