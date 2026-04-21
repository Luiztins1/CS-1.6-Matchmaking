package com.unnamed.matchmaking.cs16_matchmaking.exceptions;

import java.util.NoSuchElementException;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
