package com.unnamed.matchmaking.cs16_matchmaking.client.service;

import com.unnamed.matchmaking.cs16_matchmaking.client.dto.ClientRequestDto;
import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;
import com.unnamed.matchmaking.cs16_matchmaking.client.mapper.ClientMapper;
import com.unnamed.matchmaking.cs16_matchmaking.client.repository.ClientRepository;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ClientNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Client registerClient(ClientRequestDto clientRequestDto){
        Client client = ClientMapper.toEntity(clientRequestDto);

        if(client == null)
            throw new ResourceNotFoundException("Dto vazio.");

        if(clientRepository.existsByClientLoginOrId(client.getClientLogin(), client.getId()))
            throw new DuplicateException("Cliente já registrado.");

        var password = client.getClientPassword();
        client.setClientPassword(passwordEncoder.encode(password));

        return clientRepository.save(client);
    }

    public Client findClientByLogin(String login){
        return clientRepository.findByClientLogin(login)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado."));
    }
}
