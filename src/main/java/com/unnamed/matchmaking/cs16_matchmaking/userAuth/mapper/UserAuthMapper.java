package com.unnamed.matchmaking.cs16_matchmaking.userAuth.mapper;

import com.unnamed.matchmaking.cs16_matchmaking.userAuth.dto.UserAuthRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.userAuth.dto.UserAuthResponseDTO;
import com.unnamed.matchmaking.cs16_matchmaking.userAuth.entity.UserAuth;

public class UserAuthMapper {

    public static UserAuthResponseDTO toDto(UserAuth userAuth){
        if(userAuth == null) return null;

        return new UserAuthResponseDTO(
                userAuth.getId(),
                userAuth.getLogin(),
                userAuth.getPassword(),
                userAuth.getRoles()
        );
    }

    public static UserAuth toEntity(UserAuthRequestDTO userAuthRequestDTO){
        if(userAuthRequestDTO == null) return null;

        UserAuth userAuth = new UserAuth();

        userAuth.setId(userAuthRequestDTO.id());
        userAuth.setLogin(userAuthRequestDTO.login());
        userAuth.setPassword(userAuthRequestDTO.password());
        userAuth.setRoles(userAuthRequestDTO.roles())
        ;
        return userAuth;
    }
}
