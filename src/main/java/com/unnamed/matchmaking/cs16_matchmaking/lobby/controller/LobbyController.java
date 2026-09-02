package com.unnamed.matchmaking.cs16_matchmaking.lobby.controller;

import com.unnamed.matchmaking.cs16_matchmaking.lobby.dto.LobbyResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.entity.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.mapper.LobbyMapper;
import com.unnamed.matchmaking.cs16_matchmaking.lobby.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lobbies")
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

    @PostMapping
    public ResponseEntity<LobbyResponseDTO> addListLobbyPlayer(
            @RequestParam(required = false) UUID matchId,
            @RequestParam(required = false) UUID playerId){

        Lobby lobby = lobbyService.addListLobbyPlayer(matchId, playerId);

        if(lobby == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeListLobbyPlayer(
            @RequestParam UUID matchId,
            @RequestParam UUID playerId
            ){

        lobbyService.removeListLobbyPlayer(matchId, playerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LobbyResponseDTO> findById(@PathVariable UUID id){
        return lobbyService.findByIdLobby(id)
                .map(LobbyMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
