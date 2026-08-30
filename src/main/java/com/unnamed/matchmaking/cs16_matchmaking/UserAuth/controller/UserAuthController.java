package com.unnamed.matchmaking.cs16_matchmaking.UserAuth.controller;

import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.dto.UserAuthRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.dto.UserAuthResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.entity.UserAuth;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.mapper.UserAuthMapper;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/users")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping
    public ResponseEntity<UserAuthResponseDTO> registerUserAuth(@RequestBody @Valid UserAuthRequestDTO userAuthRequestDTO) {
        UserAuth userAuth =  userAuthService.registerUserAuth(userAuthRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userAuth.getId())
                .toUri();

        return ResponseEntity.created(location).body(UserAuthMapper.toDto(userAuth));
    }

    @GetMapping
    public ResponseEntity<List<UserAuthResponseDTO>> findAll() {
        List<UserAuthResponseDTO> userAuthList = userAuthService.findAll()
                .stream()
                .map(UserAuthMapper::toDto)
                .toList();

        if (userAuthList.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(userAuthList);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<UserAuthResponseDTO> updateUserAuth(@PathVariable UUID id, @RequestBody @Valid UserAuthRequestDTO userAuthRequestDTO) {
        return userAuthService.updateUserAuth(id, userAuthRequestDTO)
                .map(UserAuthMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{login}/cancel")
    public ResponseEntity<Void> cancelUserAuth(@PathVariable @Valid String login) {
        userAuthService.cancelUserAuth(login);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/find")
    public ResponseEntity<UserAuthResponseDTO> findById(@PathVariable UUID id) {
        return userAuthService.findById(id)
                .map(UserAuthMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{login}/login")
    public ResponseEntity<UserAuthResponseDTO> findByLogin(@PathVariable String login) {
        return userAuthService.findByLogin(login)
                .map(UserAuthMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
