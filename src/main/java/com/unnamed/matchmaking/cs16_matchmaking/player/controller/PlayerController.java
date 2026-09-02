package com.unnamed.matchmaking.cs16_matchmaking.player.controller;

import com.unnamed.matchmaking.cs16_matchmaking.player.dto.PlayerRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.player.mapper.PlayerMapper;
import com.unnamed.matchmaking.cs16_matchmaking.player.entity.Player;
import com.unnamed.matchmaking.cs16_matchmaking.player.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> save(@RequestBody @Valid PlayerRequestDTO playerRequestDTO){
        Player savedPlayer = playerService.savePlayer(playerRequestDTO);
        PlayerResponseDTO response = PlayerMapper.toDto(savedPlayer);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPlayer.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> findAll(){
        List<PlayerResponseDTO> playerList = playerService.findAllPlayer()
                .stream()
                .map(PlayerMapper::toDto)
                .toList();

        if(playerList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(playerList);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<PlayerResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid PlayerRequestDTO playerRequestDTO){
        return playerService.updatePlayer(id, playerRequestDTO)
                .map(PlayerMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/update-relationships")
    public ResponseEntity<PlayerResponseDTO> updateRelationships(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID matchId,
            @RequestParam(required = false) UUID lobbyId){

        return playerService.updateRelationships(id, matchId, lobbyId)
                .map(PlayerMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> findById(@PathVariable UUID id){
        return playerService.findByIdPlayer(id)
                .map(PlayerMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
