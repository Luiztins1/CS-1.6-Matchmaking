package com.unnamed.matchmaking.cs16_matchmaking.client.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.client.dto.ClientRequestDto;
import com.unnamed.matchmaking.cs16_matchmaking.client.dto.ClientResponseDto;
import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;

public class ClientMapper {

    public static ClientResponseDto toDto(Client client){
        if(client == null) return null;

        return new ClientResponseDto(
                client.getId(),
                client.getClientLogin(),
                client.getClientPassword(),
                client.getRedirectUri(),
                client.getScope()
        );
    }

    public static Client toEntity(ClientRequestDto clientRequestDto){
        if(clientRequestDto == null) return null;

        Client client = new Client();

        client.setId(clientRequestDto.id());
        client.setClientLogin(clientRequestDto.clientLogin());
        client.setClientPassword(clientRequestDto.clientPassword());
        client.setRedirectUri(clientRequestDto.redirectUri());
        client.setScope(clientRequestDto.scope());

        return client;
    }
}
