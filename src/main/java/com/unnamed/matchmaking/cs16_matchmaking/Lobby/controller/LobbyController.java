package com.unnamed.matchmaking.cs16_matchmaking.Lobby.controller;

import com.unnamed.matchmaking.cs16_matchmaking.Lobby.dto.LobbyResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Player.dto.PlayerResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.mapper.LobbyMapper;
import com.unnamed.matchmaking.cs16_matchmaking.Lobby.service.LobbyService;
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
    public ResponseEntity<List<LobbyResponseDTO>> findAll(){
        List<LobbyResponseDTO> lobbyList = lobbyService.findAllLobby()
                .stream()
                .map(LobbyMapper::toDto)
                .toList();

        if(lobbyList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lobbyList);
    }

    /*@PutMapping("/{id}/list-player")
    public ResponseEntity<List<PlayerResponseDTO>> updateListLobbyPlayer(
            @PathVariable UUID id,
            @RequestParam UUID matchId,
            @RequestParam UUID playerId){
        return lobbyService.addListLobbyPlayer(matchId, playerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/

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
    public ResponseEntity<LobbyResponseDTO> findByid(@PathVariable UUID id){
        return lobbyService.findByIdLobby(id)
                .map(LobbyMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
