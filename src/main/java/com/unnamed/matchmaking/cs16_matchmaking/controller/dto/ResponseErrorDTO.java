package com.unnamed.matchmaking.cs16_matchmaking.controller.dto;

import java.util.List;

public record ResponseErrorDTO(int error, String messageError, List<FieldErrorDTO> errorDTOList) {
}
