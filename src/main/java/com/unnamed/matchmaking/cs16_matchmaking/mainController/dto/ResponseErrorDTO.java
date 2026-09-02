package com.unnamed.matchmaking.cs16_matchmaking.mainController.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseErrorDTO(
        HttpStatus error,
        String messageError,
        List<FieldErrorDTO> errorList) {
}
