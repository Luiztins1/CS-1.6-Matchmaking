package com.unnamed.matchmaking.cs16_matchmaking.exceptions;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException(String message){
        super(message);
    }
}
