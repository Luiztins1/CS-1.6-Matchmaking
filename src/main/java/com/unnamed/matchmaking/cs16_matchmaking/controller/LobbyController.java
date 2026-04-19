package com.unnamed.matchmaking.cs16_matchmaking.controller;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.LobbyDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.MatchDTO;
import com.unnamed.matchmaking.cs16_matchmaking.model.Lobby;
import com.unnamed.matchmaking.cs16_matchmaking.service.LobbyService;
import jakarta.persistence.Lob;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/lobby")
@RequiredArgsConstructor
public class LobbyController {

    private final LobbyService lobbyService;

    @PostMapping
    public ResponseEntity<LobbyDTO> save(@RequestBody @Valid LobbyDTO lobbyDTO){
        Lobby lobby1 = lobbyService.saveLobby(lobbyDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(lobby1.getId())
                .toUri();

        return ResponseEntity.created(location).body(LobbyDTO.fromEntity(lobby1));
    }

    @GetMapping
    public ResponseEntity<List<LobbyDTO>> findAll(){
        List<LobbyDTO> lobbyList = lobbyService.findAllLobby()
                .stream()
                .map(LobbyDTO::fromEntity)
                .toList();

        if(lobbyList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lobbyList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody  @Valid LobbyDTO lobbyDTO){
        Optional<Lobby> lobbyOptional = lobbyService.updateLobby(id, lobbyDTO);

        if(lobbyOptional.isPresent()){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        lobbyService.deleteLobby(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LobbyDTO> findByid(@PathVariable UUID id){
        return lobbyService.findByIdLobby(id)
                .map(LobbyDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
