package com.unnamed.matchmaking.cs16_matchmaking.client.controller;

import com.unnamed.matchmaking.cs16_matchmaking.client.dto.ClientRequestDto;
import com.unnamed.matchmaking.cs16_matchmaking.client.dto.ClientResponseDto;
import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;
import com.unnamed.matchmaking.cs16_matchmaking.client.mapper.ClientMapper;
import com.unnamed.matchmaking.cs16_matchmaking.client.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> registerClient (@RequestBody @Valid ClientRequestDto clientRequestDto){
        Client client = clientService.registerClient(clientRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();

        return ResponseEntity.created(location).body(ClientMapper.toDto(client));
    }

    @GetMapping("/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> findByClientLogin(@PathVariable String login){
        Client client = clientService.findClientByLogin(login);

        if(client == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok().build();
    }
}
