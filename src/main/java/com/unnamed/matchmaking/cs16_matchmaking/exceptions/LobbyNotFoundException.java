package com.unnamed.matchmaking.cs16_matchmaking.exceptions;

public class LobbyNotFoundException extends RuntimeException{
    public LobbyNotFoundException(String message){
        super(message);
    }
}
