package com.unnamed.matchmaking.cs16_matchmaking.controller.common;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.FieldErrorDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.ResponseErrorDTO;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ChangeStateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorDTO HttpMessageNotReadableException(HttpMessageNotReadableException e){
        return new ResponseErrorDTO(HttpStatus.BAD_REQUEST, "Erro de leitura de Json ou valor inválido", List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorDTO handleValidationException(MethodArgumentNotValidException e){
        List<FieldErrorDTO> fieldErrorDTOList =
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(er -> new FieldErrorDTO(er.getField(), er.getDefaultMessage()))
                        .toList();

        return new ResponseErrorDTO(HttpStatus.BAD_REQUEST, "Campos vazios", fieldErrorDTOList);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorDTO duplicatePlayerException(DuplicateException e){
        return new ResponseErrorDTO(HttpStatus.CONFLICT, e.getMessage(), List.of());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseErrorDTO notFound(ResourceNotFoundException e){
        return new ResponseErrorDTO(HttpStatus.NOT_FOUND, e.getMessage(), List.of());
    }

    @ExceptionHandler(ChangeStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorDTO changeState(ChangeStateException e){
        return new ResponseErrorDTO(HttpStatus.CONFLICT, e.getMessage(), List.of());
    }
}
