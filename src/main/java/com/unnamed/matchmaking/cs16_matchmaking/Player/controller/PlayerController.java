package com.unnamed.matchmaking.cs16_matchmaking.Player.controller;

import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Player.mapper.PlayerMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.Player.service.PlayerService;
import com.unnamed.matchmaking.cs16_matchmaking.Player.validator.PlayerValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerValidator playerValidator;

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> save(@RequestBody @Valid PlayerResponseDTO playerResponseDTO){
        Player player1 = playerService.savePlayer(playerResponseDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(player1.getId())
                .toUri();
        return ResponseEntity.created(location).body(PlayerMapper.fromEntity(player1));
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> findAll(){
        List<PlayerResponseDTO> playerList = playerService.findAllPlayer()
                .stream()
                .map(PlayerMapper::fromEntity)
                .toList();

        if(playerList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(playerList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid PlayerResponseDTO playerResponseDTO){
        Optional<Player> playerOptional = playerService.updatePlayer(id, playerResponseDTO);

        if(playerOptional.isPresent()){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}/update-relationships")
    public ResponseEntity<PlayerResponseDTO> updateRelationships(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID matchId,
            @RequestParam(required = false) UUID lobbyId){

        return playerService.updateRelationships(id, matchId, lobbyId)
                .map(PlayerMapper::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> findById(@PathVariable UUID id){
        return playerService.findByIdPlayer(id)
                .map(PlayerMapper::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
