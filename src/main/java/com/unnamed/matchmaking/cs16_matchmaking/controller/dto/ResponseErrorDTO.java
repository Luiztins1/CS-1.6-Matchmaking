package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseErrorDTO(
        HttpStatus error,
        String messageError,
        List<FieldErrorDTO> errorList) {

    public static ResponseErrorDTO duplicateError(String messageError){
        return new ResponseErrorDTO(HttpStatus.CONFLICT, messageError, List.of());
    }

    public static ResponseErrorDTO notFound(String messageError){
        return new ResponseErrorDTO(HttpStatus.BAD_REQUEST, messageError, List.of());
    }

}
