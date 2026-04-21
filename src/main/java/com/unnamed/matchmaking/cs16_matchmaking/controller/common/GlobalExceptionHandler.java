package com.unnamed.matchmaking.cs16_matchmaking.controller.common;

import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.FieldErrorDTO;
import com.unnamed.matchmaking.cs16_matchmaking.controller.dto.ResponseErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
}
