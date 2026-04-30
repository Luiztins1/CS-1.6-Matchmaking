package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.PlayerDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.mapper.LobbyMapper;
import com.unnamed.matchmaking.cs16_matchmaking.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/lobbies")
@RequiredArgsConstructor
public class LobbyController {

    private final LobbyService lobbyService;

    @GetMapping
    public ResponseEntity<List<LobbyDTO>> findAll(){
        List<LobbyDTO> lobbyList = lobbyService.findAllLobby()
                .stream()
                .map(LobbyMapper::fromEntity)
                .toList();

        if(lobbyList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lobbyList);
    }

    @PutMapping("/{id}/list-player")
    public ResponseEntity<List<PlayerDTO>> updateListLobbyPlayer(
            @PathVariable UUID id,
            @RequestParam UUID matchId,
            @RequestParam UUID playerId){
        return lobbyService.addListLobbyPlayer(matchId, playerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/delete-list-player")
    public ResponseEntity<Void> deleteListLobbyPlayer(
            @PathVariable UUID id,
            @RequestParam UUID matchId,
            @RequestParam UUID playerId
            ){

        lobbyService.removeListLobbyPlayer(matchId, playerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LobbyDTO> findByid(@PathVariable UUID id){
        return lobbyService.findByIdLobby(id)
                .map(LobbyMapper::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
